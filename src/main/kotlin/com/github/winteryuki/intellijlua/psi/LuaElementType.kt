package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaLanguage
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import org.jetbrains.annotations.NonNls

class LuaElementType(@NonNls debugName: String) : IElementType(debugName, LuaLanguage) {
    companion object {
        val LUA_FILE = IFileElementType(LuaLanguage)
        val LUA_STUB_FILE = LuaStubFileElementType()

        // TODO
        fun createElement(node: ASTNode): PsiElement =
            when (val type = node.elementType) {
                else -> error("Unknown element type $type")
            }
    }
}
