package com.github.winteryuki.intellijlua.parser

import com.github.winteryuki.intellijlua.parsek.Parser
import com.github.winteryuki.intellijlua.parsek.advance
import com.github.winteryuki.intellijlua.parsek.and
import com.github.winteryuki.intellijlua.parsek.many
import com.github.winteryuki.intellijlua.parsek.mb
import com.github.winteryuki.intellijlua.parsek.node
import com.github.winteryuki.intellijlua.parsek.or
import com.github.winteryuki.intellijlua.parsek.skipBad
import com.github.winteryuki.intellijlua.parsek.some
import com.github.winteryuki.intellijlua.parsek.todo
import com.github.winteryuki.intellijlua.parsek.token
import com.github.winteryuki.intellijlua.parsek.tryAnd
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
            // TODO and mb(retStmt)
            LuaElementType.BLOCK.node(many(stmt))
        }

        val stmt: Parser by lazy {
            or(
                LuaElementType.SEMICOLON_STMT.node(semicolon),
                LuaElementType.BREAK_STMT.node(LuaTokenType.BREAK.token()),
                LuaElementType.LABEL_STMT.node(label),
                LuaElementType.GOTO_STMT.node(LuaTokenType.GOTO.token() tryAnd name),
                LuaElementType.IF_STMT.node(
                    iff tryAnd todo(LuaTokenType.THEN) // TODO expr
                            and then and { block }
                            and many(elseif tryAnd todo(LuaTokenType.THEN) and then and { block })
                            and end
                ),
                LuaElementType.DO_STMT.node(doo tryAnd { block } and end),
                LuaElementType.WHILE_STMT.node(whilee tryAnd todo(LuaTokenType.DO) and doo and { block } and end),
//            (repeat and { block } and until and expr),
                LuaElementType.FOR_STMT.node(
                    forr tryAnd name and assign and todo(LuaTokenType.COMMA) and todo(LuaTokenType.DO)
                            and { block } and end
                ),
                LuaElementType.FOR_STMT.node(
                    forr tryAnd nameList and inn and todo(LuaTokenType.DO) and doo and { block } and end
                ),
                LuaElementType.EXPR.node(expr and semicolon), // TODO test

//            (forr and name and assign and expr and comma and expr
//                    and mb(comma and expr) and doo
//                    and { block } and end).node(LuaElementType.FOR_STMT),
//            (forr and nameList and inn and exprList and doo and { block } and end).node(LuaElementType.FOR_STMT),
//            (function and funcName and funcBody).node(LuaElementType.FUNCTION_STMT),
//            (localFunction and name and funcBody).node(LuaElementType.FUNCTION_STMT),
//            (local tryAnd attNameList and mb(assign and exprList)).node(LuaElementType.LOCAL_ASSIGNMENT_STMT),
//            (varList and assign and exprList).node(LuaElementType.LOCAL_ASSIGNMENT_STMT),
//            functionCall.node(LuaElementType.FUNCTION_CALL_STMT),

            )
        }

        val garbage by lazy {
            Parser {
                while (!it.eof()) {
                    val marker = it.mark()
                    if (it.tokenType == null) {
                        it.advance()
                        return@Parser null
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
                Parser.Success()
            }
        }

        val attNameList by lazy {
            name and attrib and many(comma and name and attrib)
        }

        val attrib by lazy {
            mb(lt and name and gt)
        }

        val retStmt by lazy {
            ret tryAnd mb(exprList) and mb(semicolon)
        }

        val label by lazy {
            doubleColon and name and doubleColon
        }

        val funcName by lazy {
            name tryAnd many(dot and name) and mb(colon and name)
        }

        val varList by lazy {
            var_ tryAnd many(comma and var_)
        }

        val nameList by lazy {
            name tryAnd many(comma and name)
        }

        val exprList: Parser by lazy {
            expr tryAnd many(comma and { expr })
        }

        val expr: Parser by lazy {
            LuaElementType.EXPR.node(
                or(
                    expr1 tryAnd opPow tryAnd { expr },
                    expr1
                )
            )
        }

        val expr1: Parser by lazy {
            or(
                opUnary tryAnd { expr },
                expr2 tryAnd opMulDivMod tryAnd { expr },
                expr2
            )
        }

        val expr2 by lazy {
            or(
                expr3 tryAnd opAddSub tryAnd { expr },
                expr3
            )
        }

        val expr3 by lazy {
            or(
                expr4 tryAnd opStrcat tryAnd { expr },
                expr4
            )
        }

        val expr4 by lazy {
            or(
                expr5 tryAnd opCmp tryAnd { expr },
                expr5
            )
        }

        val expr5 by lazy {
            or(
                expr6 tryAnd LuaTokenType.AND.token() tryAnd { expr },
                expr6
            )
        }

        val expr6 by lazy {
            or(
                expr7 tryAnd LuaTokenType.OR.token() tryAnd { expr },
                expr7
            )
        }

        val expr7 by lazy {
            or(
                expr8 tryAnd opBitewise tryAnd { expr },
                expr8
            )
        }

        val expr8 by lazy {
            or(
                LuaTokenType.NIL.token(),
                LuaTokenType.FALSE.token(),
                LuaTokenType.TRUE.token(),
                number,
                string,
                ellipsis,
//                functionDef,
//                prefixExp,
//                tableConstructor,
            )
        }

        val expr_ = Parser {
            it.skipBad()
            while (
                !it.eof()
                && it.tokenType !in LuaTokenType.beginStmt
                && it.tokenType !in LuaTokenType.endStmt
                && it.tokenType !in LuaTokenType.exprFirst
            ) {
                it.advance()
            }
            Parser.Success(LuaElementType.EXPR)
        }

        val prefixExp by lazy {
            varOrExpr tryAnd many(nameAndArgs)
        }

        val functionCall by lazy {
            varOrExpr tryAnd some(nameAndArgs)
        }

        val varOrExpr: Parser by lazy {
            or(var_, lParen tryAnd { expr } and rParen)
        }

        val var_ by lazy {
            or(name, lBracket tryAnd { expr } and rBracket and varSuffix) and many(varSuffix)
        }

        val varSuffix by lazy {
            many(nameAndArgs) and or(lBracket and { expr } and rBracket, dot and name)
        }

        val nameAndArgs by lazy {
            mb(colon tryAnd name) and args
        }

        val args by lazy {
            or(
                lParen tryAnd { mb(exprList) } and rParen,
                tableConstructor,
                string
            )
        }

        val functionDef by lazy {
            function tryAnd funcBody
        }

        val funcBody by lazy {
            lParen tryAnd mb(parList) and rParen and { block } and end
        }

        val parList by lazy {
            or(
                nameList tryAnd mb(comma and ellipsis),
                ellipsis
            )
        }

        val tableConstructor by lazy {
            lBrace tryAnd { mb(fieldList) } and rBrace
        }

        val fieldList by lazy {
            field tryAnd many(fieldSep and field) and mb(fieldSep)
        }

        val field: Parser by lazy {
            or(
                lBracket tryAnd expr and rBracket and assign and { expr },
                name tryAnd assign and { expr },
                expr
            )
        }

        val fieldSep = LuaTokenType.fieldSep.token("Field separator")

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
        val doo = LuaTokenType.DO.token()
        val whilee = LuaTokenType.WHILE.token()
        val repeat = LuaTokenType.REPEAT.token()
        val until = LuaTokenType.UNTIL.token()
        val iff = LuaTokenType.IF.token()
        val then = LuaTokenType.THEN.token()
        val elsee = LuaTokenType.ELSE.token()
        val elseif = LuaTokenType.ELSEIF.token()
        val forr = LuaTokenType.FOR.token()
        val inn = LuaTokenType.IN.token()
        val localFunction = LuaTokenType.LOCAL_FUNCTION.token()
    }
}
