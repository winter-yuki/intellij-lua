package com.github.winteryuki.intellijlua.parsek

import com.github.winteryuki.intellijlua.psi.LuaTokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

@OptIn(ExperimentalStdlibApi::class)
fun LuaTokenType.token(element: IElementType? = null) = parser {
    if (it.tokenType != this) Parser.Fail(name.lowercase()) else {
        it.advance()
        Parser.Success(element)
    }
}

fun TokenSet.token(name: String, element: IElementType? = null) = parser {
    it.skipBad()
    if (it.tokenType !in this) Parser.Fail(name) else {
        it.advance()
        Parser.Success(element)
    }
}

fun todo(vararg until: LuaTokenType) = Parser {
    while (!it.eof() && it.tokenType !in until) {
        it.advance()
    }
    Parser.Success()
}

fun IElementType.node(parser: Parser) = parser {
    val status = parser(it)
    if (status !is Parser.Success) status
    else Parser.Success(this@node)
}
