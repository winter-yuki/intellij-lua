package com.github.winteryuki.intellijlua.parser

import com.github.winteryuki.intellijlua.psi.LuaElementType
import com.intellij.lang.PsiBuilder

class LuaChunkParser(builder: PsiBuilder) : AbstractLuaParser(builder) {
    fun parse() {
        val mark = builder.mark()
        while (!builder.eof()) {
            builder.advanceLexer()
        }
        mark.done(LuaElementType.LUA_FILE)
    }
}
