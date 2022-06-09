package com.github.winteryuki.intellijlua.parsek

infix fun Parser.or(other: Parser) = Parser {
    val marker1 = it.mark()
    val lhs = invoke(it)
    when (lhs) {
        is Parser.Success -> {
            marker1.drop()
            return@Parser lhs
        }
        null, is Parser.Fail -> marker1.rollbackTo()
    }
    val marker2 = it.mark()
    val rhs = other(it)
    when (rhs) {
        is Parser.Success -> {
            marker2.drop()
            return@Parser rhs
        }
        null, is Parser.Fail -> marker2.rollbackTo()
    }
    if (lhs == null && rhs == null) Parser.Fail("alternative") else {
        if (lhs == null) rhs else if (rhs == null) lhs else {
            require(lhs is Parser.Fail)
            require(rhs is Parser.Fail)
            lhs join rhs
        }
    }
}

fun or(vararg parsers: Parser): Parser = parsers.reduce(Parser::or)

fun mb(parser: Parser) = parser or Parser.EMPTY
