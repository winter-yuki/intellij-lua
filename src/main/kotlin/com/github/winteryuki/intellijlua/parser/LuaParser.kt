package com.github.winteryuki.intellijlua.parser

import com.github.winteryuki.intellijlua.parsek.Parser
import com.github.winteryuki.intellijlua.parsek.advance
import com.github.winteryuki.intellijlua.parsek.and
import com.github.winteryuki.intellijlua.parsek.chain
import com.github.winteryuki.intellijlua.parsek.many
import com.github.winteryuki.intellijlua.parsek.mb
import com.github.winteryuki.intellijlua.parsek.node
import com.github.winteryuki.intellijlua.parsek.or
import com.github.winteryuki.intellijlua.parsek.orInterrupt
import com.github.winteryuki.intellijlua.parsek.token
import com.github.winteryuki.intellijlua.parsek.tryAnd
import com.github.winteryuki.intellijlua.parsek.tryOr
import com.github.winteryuki.intellijlua.psi.LuaElementType
import com.github.winteryuki.intellijlua.psi.LuaTokenType
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class LuaParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        builder.setDebugMode(true);
        val mark = builder.mark()
        parser(builder)
        mark.done(LuaElementType.LUA_FILE)
        return builder.treeBuilt
    }

    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        val parser by lazy {
            block and garbage
        }

        val block by lazy {
            LuaElementType.BLOCK.node(many(stmt) and mb(retStmt))
        }

        val stmt: Parser by lazy {
            or(
                LuaElementType.SEMICOLON_STMT.node(semicolon.orInterrupt()),
                LuaElementType.BREAK_STMT.node(LuaTokenType.BREAK.token().orInterrupt()),
                LuaElementType.LABEL_STMT.node(doubleColon tryAnd decl(name) and doubleColon),
                LuaElementType.GOTO_STMT.node(LuaTokenType.GOTO.token() tryAnd ref(name)),
                LuaElementType.IF_STMT.node(
                    iff tryAnd expr and then and { block }
                            and many(elseif tryAnd expr and then and { block })
                            and mb(elseT tryAnd { block })
                            and end
                ),
                LuaElementType.DO_STMT.node(doT tryAnd { block } and end),
                LuaElementType.WHILE_STMT.node(whileT tryAnd expr and doT and { block } and end),
                LuaElementType.REPEAT_STMT.node(repeat tryAnd { block } and until and expr),
                LuaElementType.FOR_STMT.node(
                    forT tryAnd nameList and inT tryAnd exprList and doT and { block } and end
                ),
                LuaElementType.FOR_STMT.node(
                    forT tryAnd decl(name) and assign and expr and comma and expr and mb(comma and expr)
                            and doT and { block } and end
                ),
                LuaElementType.LOCAL_FUNCTION_STMT.node(
                    localFunction tryAnd decl(name) and lParen and paramList and rParen and { block } and end
                ),
                LuaElementType.FUNCTION_STMT.node(
                    function tryAnd decl(funcName) and lParen and paramList and rParen and { block } and end
                ),
                LuaElementType.LOCAL_ASSIGNMENT_STMT.node(
                    local tryAnd decl(attNameList) and mb(assign tryAnd exprList)
                ),
                LuaElementType.FUNCTION_CALL_STMT.node(
                    or(
                        ref(name).orInterrupt(),
                        lParen tryAnd { expr } and rParen
                    ) and lParen and mb(exprList) and rParen
                ),
            )
        }

        val retStmt by lazy {
            chain(ret tryAnd mb(exprList) and mb(semicolon))
        }

        val garbage by lazy {
            Parser {
                while (!it.eof()) {
                    val marker = it.mark()
                    if (it.tokenType == null) {
                        it.advance()
                        return@Parser Parser.Success
                    }
                    if (it.tokenType in LuaTokenType.beginStmt) {
                        it.advance()
                    }
                    while (
                        !it.eof()
                        && it.tokenType !in LuaTokenType.beginStmt
                        && it.tokenType !in LuaTokenType.endStmt
                    ) {
                        it.advance()
                    }
                    if (it.tokenType in LuaTokenType.endStmt) {
                        it.advance()
                    }
                    marker.error("Unknown statement")
                }
                Parser.Success
            }
        }

        val expr: Parser by lazy {
            LuaElementType.EXPR.node(
                or(
                    exprAtom and opPow tryAnd { expr },
                    opUnary tryAnd { expr },
                    exprAtom and opMulDivMod tryAnd { expr },
                    exprAtom and opAddSub tryAnd { expr },
                    exprAtom and opStrcat tryAnd { expr },
                    exprAtom and opCmp tryAnd { expr },
                    exprAtom and LuaTokenType.AND.token() tryAnd { expr },
                    exprAtom and LuaTokenType.OR.token() tryAnd { expr },
                    exprAtom and opBitewise tryAnd { expr },
                    exprAtom
                )
            )
        }

        val exprAtom by lazy {
            or(
                lParen tryAnd { expr } and rParen,
                tryOr(
                    LuaTokenType.NIL.token(),
                    LuaTokenType.FALSE.token(),
                    LuaTokenType.TRUE.token(),
                    number,
                    string,
                    ellipsis,
                    ref(name),
                )
            )
        }

        val nameList by lazy {
            chain(name tryAnd many(comma and name))
        }

        val exprList by lazy {
            chain(expr tryAnd many(comma and { expr }))
        }

        val paramList by lazy {
            or(
                nameList tryAnd mb(comma and ellipsis),
                mb(ellipsis)
            )
        }

        val funcName by lazy {
            chain(name tryAnd many(dot and name) and mb(colon and name))
        }

        val attNameList by lazy {
            name and mb(attrib) and many(comma and name and mb(attrib))
        }

        val attrib by lazy {
            lt and name and gt
        }

        val opCmp = TokenSet.create(
            LuaTokenType.LT,
            LuaTokenType.GT,
            LuaTokenType.GE,
            LuaTokenType.LE,
            LuaTokenType.EQ,
            LuaTokenType.NEQ,
        ).token("Op cmp")

        val opStrcat by lazy {
            LuaTokenType.DOTDOT.token()
        }

        val opAddSub by lazy {
            or(
                LuaTokenType.PLUS.token(),
                LuaTokenType.MINUS.token()
            )
        }

        val opMulDivMod = TokenSet.create(
            LuaTokenType.MUL,
            LuaTokenType.DIV,
            LuaTokenType.MOD,
            LuaTokenType.IDIV
        ).token("opMulDivMod")

        val opBitewise = TokenSet.create(
            LuaTokenType.BAND,
            LuaTokenType.BOR,
            LuaTokenType.BNOT,
            LuaTokenType.SHL,
            LuaTokenType.SHR
        ).token("Op bitewise")

        val opUnary = TokenSet.create(
            LuaTokenType.NOT,
            LuaTokenType.SHARP,
            LuaTokenType.MINUS,
            LuaTokenType.BNOT
        ).token("Op unary")

        val opPow = LuaTokenType.POW.token()

        val number = LuaTokenType.numbers.token(element = LuaElementType.NUMBER, name = "Number")
        val string = LuaTokenType.strings.token(element = LuaElementType.STRING, name = "String")

        val lBracket = LuaTokenType.L_BRACKET.token()
        val rBracket = LuaTokenType.R_BRACKET.token()
        val lBrace = LuaTokenType.L_BRACE.token()
        val lParen = LuaTokenType.L_PAREN.token()
        val rBrace = LuaTokenType.R_BRACE.token()
        val rParen = LuaTokenType.R_PAREN.token()
        val assign = LuaTokenType.ASSIGN.token()
        val name = LuaTokenType.IDENTIFIER.token()
        val comma = LuaTokenType.COMMA.token()
        val colon = LuaTokenType.COLON.token()
        val semicolon = LuaTokenType.SEMICOLON.token()
        val lt = LuaTokenType.LT.token()
        val gt = LuaTokenType.GT.token()
        val end = LuaTokenType.END.token()
        val dot = LuaTokenType.DOT.token()
        val ellipsis = LuaTokenType.DOTDOTDOT.token()
        val function = LuaTokenType.FUNCTION.token()
        val local = LuaTokenType.LOCAL.token()
        val doubleColon = LuaTokenType.DOUBLE_COLON.token()
        val ret = LuaTokenType.RETURN.token()
        val doT = LuaTokenType.DO.token()
        val whileT = LuaTokenType.WHILE.token()
        val repeat = LuaTokenType.REPEAT.token()
        val until = LuaTokenType.UNTIL.token()
        val iff = LuaTokenType.IF.token()
        val then = LuaTokenType.THEN.token()
        val elseT = LuaTokenType.ELSE.token()
        val elseif = LuaTokenType.ELSEIF.token()
        val forT = LuaTokenType.FOR.token()
        val inT = LuaTokenType.IN.token()
        val localFunction = LuaTokenType.LOCAL_FUNCTION.token()

        private fun ref(parser: Parser) = LuaElementType.NAME_REF.node(parser)
        private fun decl(parser: Parser) = LuaElementType.NAME_DECL.node(parser)
    }
}
