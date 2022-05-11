package com.github.winteryuki.intellijlua

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import java.nio.charset.StandardCharsets
import javax.swing.Icon

object LuaLanguage : Language("Lua")

object LuaFileType : LanguageFileType(LuaLanguage) {
    override fun getName(): String = "Lua File"
    override fun getDescription(): String = "Lua language file"
    override fun getDefaultExtension(): String = "lua"
    override fun getIcon(): Icon = LuaIcon.file
    override fun getCharset(file: VirtualFile, content: ByteArray?): String = StandardCharsets.UTF_8.name()
}

object LuaIcon {
    val file = IconLoader.getIcon("/icons/icon.png", LuaIcon.javaClass)
}
