package com.github.winteryuki.intellijlua.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class LuaParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        builder.setDebugMode(true);
        LuaChunkParser(builder).parse()
        return builder.treeBuilt
    }
}

//private class ParserException(override val message: String) : RuntimeException(message)
//
//private class Parser(private val builder: PsiBuilder) {
//
//    /**
//     * chunk
//     *      : block EOF
//     *      ;
//     */
//    fun parseChunk() = marked(LuaElementType.LUA_FILE) {
//        parseBlock()
//    }
//
//    /**
//     * block
//     *      : stat* retstat?
//     *      ;
//     */
//    private fun parseBlock(until: Set<IElementType> = emptySet()) = marked(LuaElementType.BLOCK) {
//        while (builder.tokenType != null && builder.tokenType !in until) {
//            parseStmt()
//        }
//        parseRetStmt()
//    }
//
//    /**
//     * stat
//     *   : ';'
//     *   | varlist '=' explist
//     *   | functioncall
//     *   | label
//     *   | 'break'
//     *   | 'goto' NAME
//     *   | 'do' block 'end'
//     *   | 'while' exp 'do' block 'end'
//     *   | 'repeat' block 'until' exp
//     *   | 'if' exp 'then' block ('elseif' exp 'then' block)* ('else' block)? 'end'
//     *   | 'for' NAME '=' exp ',' exp (',' exp)? 'do' block 'end'
//     *   | 'for' namelist 'in' explist 'do' block 'end'
//     *   | 'function' funcname funcbody
//     *   | 'local' 'function' NAME funcbody
//     *   | 'local' attnamelist ('=' explist)?
//     *   ;
//     */
//    private fun parseStmt() {
//        when (builder.tokenType) {
//            LuaTokenType.SEMICOLON -> parseSemicolonStmt()
//            LuaTokenType.BREAK -> parseBreakStmt()
//            LuaTokenType.IF -> parseIfStmt()
//            else -> parseGarbageStmt()
//        }
//    }
//
//    private fun parseSemicolonStmt() = marked(LuaElementType.SEMICOLON_STMT) {
//        builder.advanceLexer()
//    }
//
//    private fun parseBreakStmt() = marked(LuaElementType.BREAK_STMT) {
//        builder.advanceLexer()
//    }
//
//    private fun parseGotoStmt() {
//        TODO()
//    }
//
//    private fun parseDoStmt() {
//        TODO()
//    }
//
//    private fun parseWhileStmt() {
//        TODO()
//    }
//
//    private fun parseRepeatStmt() {
//        TODO()
//    }
//
//    private fun parseIfStmt() = marked(LuaElementType.IF_STMT) {
//        builder.advanceLexer()
//        parseExpr()
//        if (builder.tokenType != LuaTokenType.THEN) {
//            throw ParserException("then expected")
//        }
//        builder.advanceLexer()
//        parseBlock(setOf(LuaTokenType.END))
//        builder.advanceLexer()
//    }
//
//    private fun parseLocalFunctionStmt() {
//        TODO()
//    }
//
//    private fun parseFunctionStmt() {
//        TODO()
//    }
//
//    private fun parseGarbageStmt() = marked(LuaElementType.GARBAGE_STMT) {
//        builder.advanceLexer()
//        while (
//            builder.tokenType != null &&
//            builder.tokenType !in LuaTokenType.beginStmt &&
//            builder.tokenType !in LuaTokenType.endStmt
//        ) {
//            builder.advanceLexer()
//        }
//    }
//
////    private fun parseStmt() {
////        when (builder.tokenType) {
////            LuaTokenType.SEMICOLON -> marked(LuaElementType.SEMICOLON_STMT) {
////                builder.advanceLexer()
////            }
////            LuaTokenType.BREAK -> marked(LuaElementType.BREAK_STMT) {
////                builder.advanceLexer()
////            }
////            LuaTokenType.GOTO -> marked(LuaElementType.GOTO_STMT) {
////                builder.advanceLexer()
////                builder.advanceLexer()
////            }
////            LuaTokenType.DO -> marked(LuaElementType.DO_STMT) {
////                builder.advanceLexer()
////                parseBlock()
////                if (builder.tokenType != LuaTokenType.END) {
////                    throw ParserException("end expected")
////                }
////                builder.advanceLexer()
////            }
////            LuaTokenType.WHILE -> marked(LuaElementType.WHILE_STMT) {
////                builder.advanceLexer()
////                parseExpr()
////                if (builder.tokenType != LuaTokenType.DO) {
////                    throw ParserException("do expected")
////                }
////                builder.advanceLexer()
////                parseBlock()
////                if (builder.tokenType != LuaTokenType.END) {
////                    throw ParserException("end expected")
////                }
////                builder.advanceLexer()
////            }
////            LuaTokenType.IF -> marked(LuaElementType.IF_STMT) {
////                builder.advanceLexer()
////                parseExpr()
////                if (builder.tokenType != LuaTokenType.THEN) {
////                    throw ParserException("then expected")
////                }
////                builder.advanceLexer()
////                parseBlock()
////                if (builder.tokenType != LuaTokenType.END) {
////                    throw ParserException("end expected")
////                }
////                builder.advanceLexer()
////            }
////            LuaTokenType.FUNCTION -> marked(LuaElementType.FUNCTION_STMT) {
////                builder.advanceLexer()
////                if (builder.tokenType != LuaTokenType.IDENTIFIER) {
////                    throw ParserException("identifier expected")
////                }
////                builder.advanceLexer()
////                parseBlock()
////                if (builder.tokenType != LuaTokenType.END) {
////                    throw ParserException("end expected")
////                }
////                builder.advanceLexer()
////            }
////            LuaTokenType.LOCAL -> {
////                builder.advanceLexer()
////                when (builder.tokenType) {
////                    LuaTokenType.FUNCTION -> marked(LuaElementType.LOCAL_FUNCTION_STMT) {
////                        builder.advanceLexer()
////                        if (builder.tokenType != LuaTokenType.IDENTIFIER) {
////                            throw ParserException("function name expected")
////                        }
////                        builder.advanceLexer()
////                        parseBlock()
////                        if (builder.tokenType != LuaTokenType.END) {
////                            throw ParserException("end expected")
////                        }
////                        builder.advanceLexer()
////                    }
////                    LuaTokenType.IDENTIFIER -> marked(LuaElementType.LOCAL_ASSIGNMENT_STMT) {
////                        builder.advanceLexer()
////                        if (builder.tokenType != LuaTokenType.IDENTIFIER) {
////                            throw ParserException("identifier expected")
////                        }
////                        builder.advanceLexer()
////                        if (builder.tokenType == LuaTokenType.ASSIGN) {
////                            builder.advanceLexer()
////                            parseExpr()
////                        }
////                    }
////                    else -> throw ParserException("function declaration or assignment expected")
////                }
////            }
////            else -> marked(LuaElementType.GARBAGE_STMT) {
////                while (builder.tokenType != null) {
////                    builder.advanceLexer()
////                    if (
////                        builder.tokenType !in LuaTokenType.itemSetOf(LuaTokenType.Companion.StmtBegin) ||
////                        builder.tokenType !in LuaTokenType.itemSetOf(LuaTokenType.Companion.StmtEnd)
////                    ) {
////                        builder.advanceLexer()
////                        break
////                    }
////                }
////                if (builder.tokenType in LuaTokenType.itemSetOf(LuaTokenType.Companion.StmtEnd)) {
////                    builder.advanceLexer()
////                }
////            }
////        }
////    }
//
//    private fun parseRetStmt() = marked(LuaElementType.RETURN_STMT) {
//        if (builder.tokenType == LuaTokenType.RETURN) {
//            builder.advanceLexer()
//            parseExpr()
//            if (builder.tokenType == LuaTokenType.SEMICOLON) {
//                builder.advanceLexer()
//            }
//        }
//    }
//
//    private fun parseExpr() = marked(LuaElementType.EXPR) {
//        builder.advanceLexer()
//        while (
//            builder.tokenType != null &&
//            builder.tokenType != LuaTokenType.THEN &&
//            builder.tokenType !in LuaTokenType.beginStmt &&
//            builder.tokenType !in LuaTokenType.endStmt
//        ) {
//            builder.advanceLexer()
//        }
//    }
//
//    private fun marked(type: IElementType, block: (PsiBuilder.Marker) -> Unit) {
//        val mark = builder.mark()
//        try {
//            block(mark)
//            mark.done(type)
//        } catch (e: ParserException) {
//            mark.error(e.message)
//        }
//    }
//}
