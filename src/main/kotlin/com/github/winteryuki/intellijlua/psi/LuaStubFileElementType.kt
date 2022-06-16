package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaLanguage
import com.intellij.psi.stubs.PsiFileStub
import com.intellij.psi.tree.IStubFileElementType

class LuaStubFileElementType : IStubFileElementType<PsiFileStub<LuaFile>>(LuaLanguage)
