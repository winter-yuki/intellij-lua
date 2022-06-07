package com.github.winteryuki.intellijlua.parser

import com.github.winteryuki.intellijlua.psi.LuaElementType
import com.github.winteryuki.intellijlua.psi.LuaTokenType
import com.intellij.lang.PsiBuilder

class LuaBasicParser(builder: PsiBuilder) : AbstractLuaParser(builder) {
    private val exprParser by lazy { LuaExprParser(builder) }

    fun funcBody(): Parsed = TODO()

    fun parList(): Parsed = TODO()

    fun tableConstructor(): Parsed = TODO()

    fun fieldList(): Parsed = TODO()

    fun field(): Parsed =
        when {
            mb(LuaTokenType.L_BRACKET) is Element -> {
                expect(exprParser::expr)
                expect(LuaTokenType.R_BRACKET, "Right bracket")
                expect(LuaTokenType.ASSIGN, "Assignment")
                expect(exprParser::expr)
            }
            mb(LuaTokenType.IDENTIFIER) is Element -> {
                expect(LuaTokenType.ASSIGN, "Assignment")
                expect(exprParser::expr)
            }
            else -> expect(exprParser::expr)
        }

    fun number(): Parsed =
        if (tokenType !in LuaTokenType.numbers) Error("Number") else {
            advance()
            Element(LuaElementType.NUMBER)
        }

    fun string(): Parsed =
        if (tokenType !in LuaTokenType.strings) Error("String") else {
            advance()
            Element(LuaElementType.STRING)
        }
}
