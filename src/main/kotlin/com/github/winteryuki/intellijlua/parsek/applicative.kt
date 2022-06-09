package com.github.winteryuki.intellijlua.parsek

/**
 * Interrupts parsing if left parser failed.
 */
infix fun Parser.tryAnd(next: Parser) = tryAnd { next }

infix fun Parser.and(next: Parser) = and { next }

infix fun Parser.tryAnd(next: () -> Parser) = andHelper(next, rollback = true)

infix fun Parser.and(next: () -> Parser) = andHelper(next, rollback = false)

private fun Parser.andHelper(next: () -> Parser, rollback: Boolean) = Parser {
    val marker1 = it.mark()
    val lhs = invoke(it) ?: run { marker1.drop(); return@Parser null }
    when (lhs) {
        is Parser.Success -> marker1.drop()
        is Parser.Fail -> {
            if (!rollback) marker1.drop() else {
                marker1.rollbackTo()
                return@Parser null
            }
        }
    }
    val rhs = next()(it) ?: error("Parsing sequence cannot be interrupted from the right")
    lhs join rhs
}

fun many(parser: Parser) = Parser {
    var marker = it.mark()
    var status = parser(it)
    while (!it.eof() && status is Parser.Success) {
        marker.drop()
        marker = it.mark()
        status = parser(it)
    }
    if (status is Parser.Success) {
        marker.drop()
    } else {
        marker.rollbackTo()
    }
    Parser.Success()
}

fun some(parser: Parser) = parser and many(parser)