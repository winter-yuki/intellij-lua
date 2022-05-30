package com.github.winteryuki.intellijlua.parser

import com.github.winteryuki.intellijlua.psi.LuaElementType
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class LuaParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        Parser(builder).parse()
        return builder.treeBuilt
    }
}

private class Parser(private val builder: PsiBuilder) {
    fun parse() {
        val mark = builder.mark()

        var curr = builder.tokenType
        while (curr != null) {
            builder.advanceLexer()
            curr = builder.tokenType
        }
        // TODO

        mark.done(LuaElementType.LUA_FILE)
    }
}
