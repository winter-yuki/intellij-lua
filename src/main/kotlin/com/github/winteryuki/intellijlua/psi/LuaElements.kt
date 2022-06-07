package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaFileType
import com.github.winteryuki.intellijlua.LuaLanguage
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.ASTNode
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class LuaFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LuaLanguage) {
    override fun getFileType(): FileType = LuaFileType
    override fun toString(): String = "Lua File"
}

abstract class LuaPsiElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getContainingFile(): LuaFile? = super.getContainingFile() as? LuaFile
}

class LuaBlockElement(node: ASTNode) : LuaPsiElement(node)

class LuaSemicolonStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaBreakStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaLabelStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaGotoStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaDoStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaWhileStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaIfStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaForStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaFunctionStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaLocalFunctionStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaLocalAssignmentStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaFunctionCallStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaReturnStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaGarbageStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaExprElement(node: ASTNode) : LuaPsiElement(node)

class LuaStringElement(node: ASTNode) : LuaPsiElement(node)

class LuaNumberElement(node: ASTNode) : LuaPsiElement(node)

class LuaFieldElement(node: ASTNode) : LuaPsiElement(node)

class LuaFieldListElement(node: ASTNode) : LuaPsiElement(node)

class LuaTableConstructorElement(node: ASTNode) : LuaPsiElement(node)
