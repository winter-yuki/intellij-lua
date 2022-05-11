package com.github.winteryuki.intellijlua

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object LuaLanguage : Language("Lua")

object LuaFileType : LanguageFileType(LuaLanguage) {
    override fun getName(): String = "Lua"
    override fun getDescription(): String = "Lua"
    override fun getDefaultExtension(): String = "lua"
    override fun getIcon(): Icon = IconLoader.getIcon("icons/logo.png", LuaFileType::class.java)
}
