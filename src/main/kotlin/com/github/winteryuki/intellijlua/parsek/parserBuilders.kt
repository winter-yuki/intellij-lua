package com.github.winteryuki.intellijlua.parsek

import com.github.winteryuki.intellijlua.psi.LuaTokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

fun marked(element: IElementType? = null, parser: Parser) = Parser {
    it.skipBad()
    val marker = it.mark()
    val status = parser(it)
    when (status) {
        is Parser.Success -> {
            if (element == null) {
                marker.drop()
            } else {
                marker.done(element)
            }
        }
        is Parser.Interrupt -> marker.drop()
        is Parser.Failure -> marker.error(status.message)
    }
    status
}

fun IElementType.node(parser: Parser) = marked(this, parser)

fun IElementType.nodeChain(parser: Parser) = marked(this, parser.orFail())

fun LuaTokenType.token(element: IElementType? = null) = marked(element) {
    if (it.tokenType != this) Parser.Failure(name.toLowerCase()) else {
        it.advance()
        Parser.Success
    }
}

fun TokenSet.token(name: String, element: IElementType? = null) = marked(element) {
    it.skipBad()
    if (it.tokenType !in this) Parser.Failure(name) else {
        it.advance()
        Parser.Success
    }
}

@Suppress("FunctionName")
fun TODO(vararg stoppers: LuaTokenType) = Parser {
    while (!it.eof() && it.tokenType !in stoppers) {
        it.advance()
    }
    Parser.Success
}
