package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaLanguage
import com.github.winteryuki.intellijlua.utils.AbstractSortContainer
import com.github.winteryuki.intellijlua.utils.Sort
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import org.jetbrains.annotations.NonNls

class LuaElementType(@NonNls debugName: String) : IElementType(debugName, LuaLanguage) {
    companion object : AbstractSortContainer<LuaElementType>(::LuaElementType) {
        val LUA_FILE = IFileElementType(LuaLanguage)
        val LUA_STUB_FILE = LuaStubFileElementType()

        object Stmt : Sort

        val BLOCK by item()

        val SEMICOLON_STMT by item(Stmt)
        val BREAK_STMT by item(Stmt)
        val GOTO_STMT by item(Stmt)
        val DO_STMT by item(Stmt)
        val WHILE_STMT by item(Stmt)
        val IF_STMT by item(Stmt)
        val FOR_STMT by item(Stmt)
        val FUNCTION_STMT by item(Stmt)
        val LOCAL_FUNCTION_STMT by item(Stmt)
        val LOCAL_ASSIGNMENT_STMT by item(Stmt)
        val FUNCTION_CALL_STMT by item(Stmt)
        val RETURN_STMT by item(Stmt)
        val GARBAGE_STMT by item(Stmt)

        val EXPR by item()

        fun createElement(node: ASTNode): PsiElement =
            when (val type = node.elementType) {
                BLOCK -> LuaBlockElement(node)
                SEMICOLON_STMT -> LuaSemicolonStmtElement(node)
                BREAK_STMT -> LuaBreakStmtElement(node)
                GOTO_STMT -> LuaGotoStmtElement(node)
                DO_STMT -> LuaDoStmtElement(node)
                WHILE_STMT -> LuaWhileStmtElement(node)
                IF_STMT -> LuaIfStmtElement(node)
                FOR_STMT -> LuaForStmtElement(node)
                FUNCTION_STMT -> LuaFunctionStmtElement(node)
                LOCAL_FUNCTION_STMT -> LuaLocalFunctionStmtElement(node)
                LOCAL_ASSIGNMENT_STMT -> LuaLocalAssignmentStmtElement(node)
                FUNCTION_CALL_STMT -> LuaFunctionCallStmtElement(node)
                RETURN_STMT -> LuaReturnStmtElement(node)
                GARBAGE_STMT -> LuaGarbageStmtElement(node)
                EXPR -> LuaExprElement(node)
                else -> error("Unknown element type $type")
            }

        override val items: List<LuaElementType> = listOf(
            BLOCK, SEMICOLON_STMT, IF_STMT, EXPR, RETURN_STMT, GARBAGE_STMT,
            BREAK_STMT, GOTO_STMT, DO_STMT, WHILE_STMT, FOR_STMT, FUNCTION_STMT,
            LOCAL_FUNCTION_STMT, LOCAL_ASSIGNMENT_STMT, FUNCTION_CALL_STMT
        )
    }
}
