package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaFileType
import com.github.winteryuki.intellijlua.LuaLanguage
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.ASTNode
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.descendants

class LuaFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LuaLanguage) {
    override fun getFileType(): FileType = LuaFileType
    override fun toString(): String = "Lua File"
}

abstract class LuaPsiElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getContainingFile(): LuaFile? = super.getContainingFile() as? LuaFile
}

abstract class LuaNamedPsiElement(node: ASTNode) : LuaPsiElement(node), PsiNamedElement

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

class LuaAssignmentStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaLocalFunctionStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaLocalAssignmentStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaFunctionCallStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaReturnStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaGarbageStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaRepeatStmtElement(node: ASTNode) : LuaPsiElement(node)

class LuaExprElement(node: ASTNode) : LuaPsiElement(node)

class LuaStringElement(node: ASTNode) : LuaPsiElement(node)

class LuaNumberElement(node: ASTNode) : LuaPsiElement(node)

class LuaFieldElement(node: ASTNode) : LuaPsiElement(node)

class LuaFieldListElement(node: ASTNode) : LuaPsiElement(node)

class LuaTableConstructorElement(node: ASTNode) : LuaPsiElement(node)

class LuaBinOpElement(node: ASTNode) : LuaPsiElement(node)

class LuaNameDeclarationElement(node: ASTNode) : LuaNamedPsiElement(node) {
    override fun setName(name: String): PsiElement = this

    override fun getName(): String = node.text
}

class LuaNameReferenceElement(node: ASTNode) : LuaPsiElement(node) {
    override fun getReference(): PsiReference? {
        val targetName = node.text
        var scope: PsiElement? = this.parent
        while (scope != null) {
            val target = scope.children
                .flatMap {
                    it
                        .descendants { it !is LuaBlockElement }
                        .filterIsInstance<LuaNameDeclarationElement>()
                }
                .find { it.name == targetName }
            if (target != null) {
                return object : PsiReferenceBase<PsiElement>(this, this.textRangeInParent) {
                    override fun resolve(): PsiElement = target
                }
            }
            scope = scope.parent
        }
        return null
    }
}
