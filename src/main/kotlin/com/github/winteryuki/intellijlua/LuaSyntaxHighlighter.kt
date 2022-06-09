package com.github.winteryuki.intellijlua

import com.github.winteryuki.intellijlua.lexer.LuaLexer
import com.github.winteryuki.intellijlua.psi.LuaTokenType
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.StringEscapesTokenTypes
import com.intellij.psi.tree.IElementType

class LuaSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = LuaLexer()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> =
        pack(attributes[tokenType])

    companion object {
        @Suppress("OPT_IN_IS_NOT_ENABLED")
        @OptIn(ExperimentalStdlibApi::class)
        val attributes = buildMap {
            fillMap(this, LuaTokenType.keywords, LuaTextAttributeKeys.KEYWORD.key)
            fillMap(this, LuaTokenType.operators, LuaTextAttributeKeys.OPERATOR.key)
            fillMap(this, LuaTokenType.numbers, LuaTextAttributeKeys.NUMBER.key)
            fillMap(this, LuaTokenType.comments, LuaTextAttributeKeys.COMMENT.key)
            fillMap(this, LuaTokenType.strings, LuaTextAttributeKeys.STRING.key)
            fillMap(this, LuaTokenType.constants, LuaTextAttributeKeys.CONSTANT.key)
            fillMap(this, LuaTokenType.braces, LuaTextAttributeKeys.BRACES.key)
            fillMap(this, LuaTokenType.brackets, LuaTextAttributeKeys.BRACKETS.key)
            fillMap(this, LuaTokenType.parens, LuaTextAttributeKeys.PARENTHESES.key)

            put(LuaTokenType.IDENTIFIER, LuaTextAttributeKeys.IDENTIFIER.key)

            put(LuaTokenType.DOUBLE_COLON, LuaTextAttributeKeys.DOUBLE_COLON.key)
            put(LuaTokenType.SEMICOLON, LuaTextAttributeKeys.SEMICOLON.key)
            put(LuaTokenType.COLON, LuaTextAttributeKeys.COLON.key)
            put(LuaTokenType.COMMA, LuaTextAttributeKeys.COMMA.key)
            put(LuaTokenType.DOT, LuaTextAttributeKeys.DOT.key)
            put(LuaTokenType.DOTDOTDOT, LuaTextAttributeKeys.DOTDOTDOT.key)

            put(StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN, LuaTextAttributeKeys.VALID_STRING_ESCAPE.key)
            put(StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN, LuaTextAttributeKeys.INVALID_STRING_ESCAPE.key)
        }
    }
}

class LuaSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter =
        LuaSyntaxHighlighter()
}

class LuaColorSettingsPage : ColorSettingsPage {
    override fun getDisplayName() = LuaLanguage.displayName
    override fun getIcon() = LuaFileType.icon
    override fun getAttributeDescriptors() = LuaTextAttributeKeys.values().map { it.descriptor }.toTypedArray()
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter() = LuaSyntaxHighlighter()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> =
        LuaTextAttributeKeys.values().associateBy({ it.name }, { it.key })

    override fun getDemoText(): String = "function hello" // TODO
}

enum class LuaTextAttributeKeys(humanName: String, fallback: TextAttributesKey) {
    COMMENT("Comment", DefaultLanguageHighlighterColors.DOC_COMMENT),
    STRING("String//String text", DefaultLanguageHighlighterColors.STRING),
    CONSTANT("Built in constants", DefaultLanguageHighlighterColors.CONSTANT),
    NUMBER("Number//Number", DefaultLanguageHighlighterColors.NUMBER),
    KEYWORD("Keyword", DefaultLanguageHighlighterColors.KEYWORD),

    DOUBLE_COLON("Braces and Operators//Double colon", DefaultLanguageHighlighterColors.DOT),
    SEMICOLON("Braces and Operators//Semicolon", DefaultLanguageHighlighterColors.SEMICOLON),
    COLON("Braces and Operators//Colon", DefaultLanguageHighlighterColors.DOT),
    COMMA("Braces and Operators//Comma", DefaultLanguageHighlighterColors.COMMA),
    DOT("Braces and Operators//Dot", DefaultLanguageHighlighterColors.DOT),
    DOTDOTDOT("Braces and Operators//Dot dot dot", DefaultLanguageHighlighterColors.DOT),

    OPERATOR("Braces and Operators//Operators", DefaultLanguageHighlighterColors.OPERATION_SIGN),

    PARENTHESES("Braces and Operators//Parentheses", DefaultLanguageHighlighterColors.PARENTHESES),
    BRACKETS("Braces and Operators//Brackets", DefaultLanguageHighlighterColors.BRACKETS),
    BRACES("Braces and Operators//Braces", DefaultLanguageHighlighterColors.BRACES),

    VALID_STRING_ESCAPE("String//Escape Sequence//Valid", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE),
    INVALID_STRING_ESCAPE("String//Escape Sequence//Invalid", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE),
    IDENTIFIER("Identifier", DefaultLanguageHighlighterColors.IDENTIFIER);

    val key = TextAttributesKey.createTextAttributesKey("Lua.$name", fallback)
    val descriptor = AttributesDescriptor(humanName, key)
}
