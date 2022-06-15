package com.github.winteryuki.intellijlua.parsek

private infix fun Parser.or(other: Parser) = Parser {
    val marker1 = it.mark()
    val lhs = invoke(it)
    if (lhs is Parser.Interrupt) marker1.rollbackTo() else {
        marker1.drop()
        return@Parser lhs
    }
    val marker2 = it.mark()
    val rhs = other(it)
    if (rhs is Parser.Interrupt) marker2.rollbackTo() else {
        marker2.drop()
        return@Parser rhs
    }
    lhs join rhs
}

fun or(vararg parsers: Parser): Parser = parsers.reduce(Parser::or).orFail()

fun tryOr(vararg parsers: Parser): Parser = parsers.map(Parser::orInterrupt).reduce(Parser::or).orFail()

fun mb(parser: Parser) = (parser.orInterrupt() or Parser.EMPTY).orFail()
