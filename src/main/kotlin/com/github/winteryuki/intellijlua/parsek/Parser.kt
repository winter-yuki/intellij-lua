package com.github.winteryuki.intellijlua.parsek

import com.intellij.lang.PsiBuilder

/**
 * Parser to be combined.
 */
fun interface Parser {

    /**
     * Invokes parser execution with [builder].
     */
    operator fun invoke(builder: PsiBuilder): Status

    /**
     * [Parser] result status.
     */
    sealed interface Status

    object Success : Status

    sealed interface NonSuccess : Status {
        val expected: String
    }

    @JvmInline
    value class Interrupt(override val expected: String) : NonSuccess {
        init {
            require(expected.isNotBlank())
        }
    }

    @JvmInline
    value class Failure(override val expected: String) : NonSuccess {
        init {
            require(expected.isNotBlank())
        }
    }

    companion object {
        val EMPTY = Parser { Success }
    }
}

/**
 * Converts interruption to failure.
 */
fun Parser.orFail() = Parser {
    when (val status = invoke(it)) {
        is Parser.Interrupt -> Parser.Failure(status.expected)
        else -> status
    }
}

/**
 * Converts failure to interruption.
 */
fun Parser.orInterrupt() = Parser {
    when (val status = invoke(it)) {
        is Parser.Failure -> Parser.Interrupt(status.expected)
        else -> status
    }
}

/**
 * Interrupting 'tryAnd/and' chain parser.
 */
fun chain(parser: Parser) = parser.orFail()

val Parser.NonSuccess.message: String
    get() = "{$expected} expected"

infix fun Parser.Failure.join(fail: Parser.Failure) = Parser.Failure("$expected | ${fail.expected}")
infix fun Parser.Interrupt.join(fail: Parser.Interrupt) = Parser.Interrupt("$expected | ${fail.expected}")
