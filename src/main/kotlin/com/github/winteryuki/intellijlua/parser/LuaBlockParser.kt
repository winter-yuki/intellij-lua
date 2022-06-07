package com.github.winteryuki.intellijlua.parser

import com.github.winteryuki.intellijlua.psi.LuaElementType
import com.github.winteryuki.intellijlua.psi.LuaTokenType
import com.intellij.lang.PsiBuilder

class LuaBlockParser(private val builder: PsiBuilder) : AbstractLuaParser(builder) {
    private val exprParser = LuaExprParser(builder)

    /**
     * block
     *      : stmt* retStmt?
     *      ;
     */
    fun parseBlock() {
        while (builder.tokenType != null) {
            builder.advanceLexer()
        }
    }

//    fun parseBlock(until: Set<LuaTokenType> = setOf(LuaTokenType.RETURN)) = marked(LuaElementType.BLOCK) {
//        while (builder.tokenType != null) {
//            builder.advanceLexer()
//        }
////        while (!eof && tokenType !in until) {
////            parseStmt()
////        }
////        if (tokenType == LuaTokenType.RETURN) {
////            parseRet()
////        }
//    }

//    private fun parseStmt() {
//        when (tokenType) {
//            LuaTokenType.SEMICOLON -> parseSemicolonStmt()
//            LuaTokenType.DOUBLE_COLON -> parseLabelStmt()
//            LuaTokenType.BREAK -> parseBreakStmt()
//            LuaTokenType.GOTO -> parseGotoStmt()
//            LuaTokenType.DO -> parseDoStmt()
//            LuaTokenType.WHILE -> parseWhileStmt()
//            LuaTokenType.REPEAT -> parseRepeatStmt()
//            LuaTokenType.IF -> parseIfStmt()
//            LuaTokenType.LOCAL_FUNCTION -> parseLocalFunction()
//            else -> parseGarbageStmt()
//        }
//    }
//
//    private fun parseSemicolonStmt() = marked(LuaElementType.SEMICOLON_STMT) {
//        require(tokenType == LuaTokenType.SEMICOLON)
//        advanceOne()
//    }
//
//    private fun parseBreakStmt() = marked(LuaElementType.BREAK_STMT) {
//        require(tokenType == LuaTokenType.BREAK)
//        advanceOne()
//    }
//
//    /**
//     * label
//    : '::' NAME '::'
//    ;
//     */
//    private fun parseLabelStmt() {
//        require(tokenType == LuaTokenType.DOUBLE_COLON)
//        val mark = mark()
//        advance()
//        if (tokenType != LuaTokenType.IDENTIFIER) {
//            mark.error("Identifier expected")
//            return
//        }
//        advance() // NAME
//        if (tokenType != LuaTokenType.DOUBLE_COLON) {
//            mark.error("Double colon expected")
//            return
//        }
//        advanceOne()
//        mark.done(LuaElementType.LABEL_STMT)
//    }
//
//    private fun parseGotoStmt() {
//        require(tokenType == LuaTokenType.GOTO)
//        val mark = mark()
//        advance()
//        if (tokenType != LuaTokenType.IDENTIFIER) {
//            mark.error("Identifier expected")
//            return
//        }
//        advanceOne()
//        mark.done(LuaElementType.GOTO_STMT)
//    }
//
//    private fun parseDoStmt() {
//        require(tokenType == LuaTokenType.DO)
//        val mark = mark()
//        advance() // dp
//        parseBlock(setOf(LuaTokenType.END))
//        if (tokenType != LuaTokenType.END) {
//            mark.error("End expected")
//            return
//        }
//        mark.done(LuaElementType.DO_STMT)
//    }
//
//    private fun parseWhileStmt() {
//        require(tokenType == LuaTokenType.WHILE)
//        val mark = mark()
//        advance() // while
//        skipUntil(setOf(LuaTokenType.DO)) // expr
//        if (tokenType != LuaTokenType.DO) {
//            mark.error("Do expected")
//            return
//        }
//        parseBlock(setOf(LuaTokenType.END))
//        if (tokenType != LuaTokenType.END) {
//            mark.error("End expected")
//            return
//        }
//        advanceOne()
//        mark.done(LuaElementType.WHILE_STMT)
//    }
//
//    private fun parseRepeatStmt() {
//        require(tokenType == LuaTokenType.REPEAT)
//        val mark = mark()
//        advance() // repeat
//        parseBlock(setOf(LuaTokenType.UNTIL))
//        if (tokenType != LuaTokenType.UNTIL) {
//            mark.error("Until expected")
//            return
//        }
//        advance() // until
//        skipUntil(LuaTokenType.beginStmt) // expr
//    }
//
//    private fun parseIfStmt() {
//        require(tokenType == LuaTokenType.IF)
//        val mark = mark()
//        parseIfStmtHelper(mark)
//    }
//
//    private fun parseIfStmtHelper(mark: PsiBuilder.Marker) {
//        require(tokenType == LuaTokenType.IF || tokenType == LuaTokenType.ELSEIF)
//        advance() // if | elseif
//        skipUntil(setOf(LuaTokenType.THEN))
//        if (tokenType != LuaTokenType.THEN) {
//            mark.error("Then expected")
//            return
//        }
//        advance() // then
//        parseBlock(setOf(LuaTokenType.ELSE, LuaTokenType.ELSEIF, LuaTokenType.END))
//        when (tokenType) {
//            LuaTokenType.ELSE -> {
//                parseBlock(setOf(LuaTokenType.END))
//                if (tokenType != LuaTokenType.END) {
//                    mark.error("End expected")
//                    return
//                }
//                advanceOne() // end
//            }
//            LuaTokenType.ELSEIF -> parseIfStmtHelper(mark)
//            LuaTokenType.END -> {
//                advanceOne() // end
//                mark.done(LuaElementType.IF_STMT)
//            }
//            else -> mark.error("End expected")
//        }
//    }
//
//    private fun parseLocalFunction() {
//        require(tokenType == LuaTokenType.LOCAL_FUNCTION)
//        val mark = mark()
//        advance() // local function
//        if (tokenType != LuaTokenType.IDENTIFIER) {
//            mark.error("Function name expected")
//            return
//        }
//        advance() // name
//        parseBlock(setOf(LuaTokenType.END))
//        if (tokenType != LuaTokenType.END) {
//            mark.error("End expected")
//            return
//        }
//        advanceOne() // end
//    }
//
//    private fun parseGarbageStmt() = marked(LuaElementType.GARBAGE_STMT) {
//        advance()
//        while (!eof && tokenType !in LuaTokenType.beginStmt && tokenType !in LuaTokenType.endStmt) {
//            advance()
//        }
//    }
//
//    private fun parseRet() {
//        require(tokenType == LuaTokenType.RETURN)
//        advance() // return
//
//        // TODO
//    }
//
//    // TODO
//    private fun skipUntil(until: Set<LuaTokenType>) {
//        advance()
//        while (!eof && tokenType !in until) {
//            advance()
//        }
//    }
//
//    // TODO
//    private fun skipUntil(until: TokenSet) {
//        advance()
//        while (!eof && tokenType !in until) {
//            advance()
//        }
//    }
//
//
//    private fun parseExpr() {
//        TODO()
//    }
//
//
//    private fun parseTableConstructor() = marked(LuaElementType.TABLE_CONSTRUCTOR) {
//        expectAdvance(LuaTokenType.L_BRACE, "Left brace")
//
//    }
//
//    private fun parseFieldList() = marked(LuaElementType.FIELD_LIST) {
//        parseField()
//        while (tokenType in LuaTokenType.fieldSep) {
//            advance()
//            parseField()
//        }
//        if (tokenType in LuaTokenType.fieldSep) {
//            advance()
//        }
//    }
//
//    private fun parseField() = marked(LuaElementType.FIELD) {
//        when {
//            tryAdvance(LuaTokenType.L_BRACKET) -> {
//                parseExpr()
//                expectAdvance(LuaTokenType.R_BRACKET, "Right bracket")
//                expectAdvance(LuaTokenType.ASSIGN, "Assignment")
//                parseExpr()
//            }
//            tryAdvance(LuaTokenType.IDENTIFIER) -> {
//                expectAdvance(LuaTokenType.ASSIGN, "Assignment")
//                parseExpr()
//            }
//            else -> parseExpr()
//        }
//    }
//
//    private fun parseNumber(): PsiBuilder.Marker? {
//        val (exists, mark) = expectNumber()
//        return if (exists) mark else {
//            mark.error("Number expected")
//            null
//        }
//    }
//
//    private fun expectNumber(): Pair<Boolean, PsiBuilder.Marker> {
//        val mark = mark()
//        return if (tokenType in LuaTokenType.numbers) {
//            advance()
//            mark.done(LuaElementType.NUMBER)
//            return true to mark
//        } else {
//            false to mark
//        }
//    }
//
//    private fun parseString(): PsiBuilder.Marker? {
//        val (exists, mark) = expectString()
//        return if (exists) mark else {
//            mark.error("String expected")
//            null
//        }
//    }
//
//    private fun expectString(): Pair<Boolean, PsiBuilder.Marker> {
//        val mark = mark()
//        return if (tokenType in LuaTokenType.strings) {
//            advance()
//            mark.done(LuaElementType.STRING)
//            true to mark
//        } else {
//            false to mark
//        }
//    }
}
