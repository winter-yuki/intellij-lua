package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaLanguage
import com.github.winteryuki.intellijlua.utils.AbstractTokenContainer
import com.github.winteryuki.intellijlua.utils.TokenSort
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import org.jetbrains.annotations.NonNls

class LuaElementType(@NonNls debugName: String) : IElementType(debugName, LuaLanguage) {
    companion object : AbstractTokenContainer<LuaElementType>(::LuaElementType) {
        val LUA_FILE = IFileElementType(LuaLanguage)
        val LUA_STUB_FILE = LuaStubFileElementType()

        private object Stmt : TokenSort

        val BLOCK by token()

        val SEMICOLON_STMT by token(Stmt)
        val BREAK_STMT by token(Stmt)
        val LABEL_STMT by token(Stmt)
        val GOTO_STMT by token(Stmt)
        val DO_STMT by token(Stmt)
        val WHILE_STMT by token(Stmt)
        val IF_STMT by token(Stmt)
        val FOR_STMT by token(Stmt)
        val FUNCTION_STMT by token(Stmt)
        val LOCAL_FUNCTION_STMT by token(Stmt)
        val LOCAL_ASSIGNMENT_STMT by token(Stmt)
        val FUNCTION_CALL_STMT by token(Stmt)
        val RETURN_STMT by token(Stmt)
        val GARBAGE_STMT by token(Stmt)

        val EXPR by token()

        val STRING by token()
        val NUMBER by token()
        val FIELD by token()
        val FIELD_LIST by token()
        val TABLE_CONSTRUCTOR by token()

        fun createElement(node: ASTNode): PsiElement =
            when (val type = node.elementType) {
                BLOCK -> LuaBlockElement(node)
                SEMICOLON_STMT -> LuaSemicolonStmtElement(node)
                BREAK_STMT -> LuaBreakStmtElement(node)
                LABEL_STMT -> LuaLabelStmtElement(node)
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
                STRING -> LuaStringElement(node)
                NUMBER -> LuaNumberElement(node)
                FIELD -> LuaFieldElement(node)
                FIELD_LIST -> LuaFieldListElement(node)
                TABLE_CONSTRUCTOR -> LuaTableConstructorElement(node)
                else -> error("Unknown element type $type")
            }

        override val tokens: List<LuaElementType> = listOf(
            BLOCK, SEMICOLON_STMT, IF_STMT, EXPR, RETURN_STMT, LABEL_STMT, GARBAGE_STMT,
            BREAK_STMT, GOTO_STMT, DO_STMT, WHILE_STMT, FOR_STMT, FUNCTION_STMT,
            LOCAL_FUNCTION_STMT, LOCAL_ASSIGNMENT_STMT, FUNCTION_CALL_STMT, STRING, NUMBER,
            FIELD, FIELD_LIST, TABLE_CONSTRUCTOR
        )
    }
}
