package com.github.winteryuki.intellijlua.parser

import com.intellij.lang.PsiBuilder
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

sealed interface Parsed

@JvmInline
value class Error(val expected: String? = null) : Parsed

@JvmInline
value class Element(val element: IElementType? = null) : Parsed

typealias Parser = () -> Parsed

abstract class AbstractLuaParser(private val builder: PsiBuilder) {

    protected val tokenType: IElementType?
        get() {
            skipBad()
            return builder.tokenType
        }

    protected val eof: Boolean
        get() {
            skipBad()
            return builder.eof()
        }

    private fun skipBad() {
        while (builder.tokenType == TokenType.BAD_CHARACTER) {
            val badMark = builder.mark()
            builder.advanceLexer()
            badMark.error("Unexpected symbol")
        }
    }

    private fun mark(): PsiBuilder.Marker {
        skipBad()
        return builder.mark()
    }

    protected fun advance() {
        skipBad()
        builder.advanceLexer()
        skipBad()
    }

    private fun apply(parser: Parser): Parsed {
        val mark = mark()
        val parsed = parser()
        when (parsed) {
            is Error ->
                if (parsed.expected != null) {
                    mark.error(parsed.expected)
                } else {
                    mark.rollbackTo()
                }
            is Element ->
                if (parsed.element != null) {
                    mark.done(parsed.element)
                } else {
                    mark.drop()
                }
        }
        return parsed
    }

    protected fun mb(parser: Parser) = apply {
        when (val parsed = parser()) {
            is Error -> Error()
            is Element -> parsed
        }
    }

    protected fun mb(expected: IElementType, element: IElementType? = null) = apply {
        if (tokenType != expected) Error() else {
            advance()
            Element(element)
        }
    }

    protected fun mb(expected: TokenSet, element: IElementType? = null) = apply {
        if (tokenType !in expected) Error() else {
            advance()
            Element(element)
        }
    }

    protected fun expect(parser: Parser) = apply {
        when (val parsed = parser()) {
            is Error -> Error(requireNotNull(parsed.expected))
            is Element -> parsed
        }
    }

    protected fun expect(expected: IElementType, name: String, element: IElementType? = null) = apply {
        if (tokenType != expected) Error("$name expected") else {
            advance()
            Element(element)
        }
    }

    protected fun expect(expected: TokenSet, name: String, element: IElementType? = null) = apply {
        if (tokenType !in expected) Error("$name expected") else {
            advance()
            Element(element)
        }
    }

    protected fun marked(type: IElementType, block: () -> Unit) {
        val mark = builder.mark()
        block()
        mark.done(type)
    }
}
