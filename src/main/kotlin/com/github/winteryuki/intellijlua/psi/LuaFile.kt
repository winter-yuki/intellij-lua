package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaFileType
import com.github.winteryuki.intellijlua.LuaLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class LuaFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LuaLanguage) {
    override fun getFileType(): FileType = LuaFileType
    override fun toString(): String = "Lua File"
}
