package com.github.winteryuki.intellijlua.parsek

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiBuilder.Marker
import com.intellij.psi.tree.IElementType

/**
 * Parser to be combined.
 */
fun interface Parser {

    /**
     * Invokes parser execution with [builder].
     * @return NUll result interrupts further sequence parsing with none tokens eaten.
     */
    operator fun invoke(builder: PsiBuilder): Status?

    /**
     * [Parser] result status.
     */
    sealed interface Status

    @JvmInline
    value class Fail(val expected: String) : Status {
        init {
            require(expected.isNotBlank())
        }
    }

    @JvmInline
    value class Success(val element: IElementType? = null) : Status

    companion object {
        val EMPTY = Parser { Success() }
    }
}

fun parser(parser: Parser) = Parser {
    it.skipBad()
    val marker = it.mark()
    val status = parser(it)
    when (status) {
        null -> marker.drop()
        is Parser.Success -> status.close(marker)
        is Parser.Fail -> marker.error(status.message)
    }
    status
}

/**
 * Converts interruption to failure.
 */
fun Parser.orFail(message: String) = Parser {
    invoke(it) ?: Parser.Fail(message)
}

/**
 * Converts failure to interruption.
 */
fun Parser.orInterrupt() = Parser {
    when (val status = invoke(it)) {
        null, is Parser.Fail -> null
        is Parser.Success -> status
    }
}

val Parser.Fail.message: String
    get() = "{$expected} expected"

infix fun Parser.Fail.join(fail: Parser.Fail) = Parser.Fail("$expected | ${fail.expected}")

infix fun Parser.Status.join(status: Parser.Status): Parser.Status =
    when (this) {
        is Parser.Success -> status
        is Parser.Fail -> if (status is Parser.Fail) join(status) else status
    }

fun Parser.Success.close(marker: Marker) {
    if (element == null) {
        marker.drop()
    } else {
        marker.done(element)
    }
}
