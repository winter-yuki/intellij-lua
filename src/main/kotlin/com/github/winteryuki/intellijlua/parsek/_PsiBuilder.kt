package com.github.winteryuki.intellijlua.parsek

import com.intellij.lang.PsiBuilder
import com.intellij.psi.TokenType

fun PsiBuilder.advance() {
    skipBad()
    advanceLexer()
    skipBad()
}

fun PsiBuilder.skipBad() {
    while (tokenType == TokenType.BAD_CHARACTER) {
        val badMark = mark()
        advanceLexer()
        badMark.error("Unexpected symbol")
    }
}
