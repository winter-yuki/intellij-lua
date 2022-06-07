package com.github.winteryuki.intellijlua.parser

import com.github.winteryuki.intellijlua.psi.LuaElementType
import com.intellij.lang.PsiBuilder

class LuaChunkParser(builder: PsiBuilder) : AbstractLuaParser(builder) {
    private val blockParser = LuaBlockParser(builder)

    /**
     * chunk
     *      : block EOF
     *      ;
     */
    fun parseChunk() = marked(LuaElementType.LUA_FILE) {
        blockParser.parseBlock()
    }
}
