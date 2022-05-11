package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

class LuaElementType(@NonNls debugName: String) : IElementType(debugName, LuaLanguage)
