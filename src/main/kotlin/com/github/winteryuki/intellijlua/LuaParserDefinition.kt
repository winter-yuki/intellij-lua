package com.github.winteryuki.intellijlua

import com.github.winteryuki.intellijlua.lexer.LuaLexer
import com.github.winteryuki.intellijlua.parser.LuaParser
import com.github.winteryuki.intellijlua.psi.LuaElementType
import com.github.winteryuki.intellijlua.psi.LuaFile
import com.github.winteryuki.intellijlua.psi.LuaTokenType
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class LuaParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = LuaLexer()
    override fun createParser(project: Project?): PsiParser = LuaParser()
    override fun getFileNodeType(): IFileElementType = LuaElementType.LUA_STUB_FILE
    override fun getCommentTokens(): TokenSet = LuaTokenType.comments
    override fun getStringLiteralElements(): TokenSet = LuaTokenType.strings
    override fun getWhitespaceTokens(): TokenSet = LuaTokenType.whiteSpaces
    override fun createElement(node: ASTNode): PsiElement = LuaElementType.createElement(node)
    override fun createFile(viewProvider: FileViewProvider): PsiFile = LuaFile(viewProvider)
}
