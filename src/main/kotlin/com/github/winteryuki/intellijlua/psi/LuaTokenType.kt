package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaLanguage
import com.github.winteryuki.intellijlua.utils.AbstractTokenContainer
import com.github.winteryuki.intellijlua.utils.TokenSort
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

class LuaTokenType private constructor(@NonNls debugName: String) : IElementType(debugName, LuaLanguage) {
    override fun toString(): String = "LuaTokenType.${super.toString()}"

    companion object : AbstractTokenContainer<LuaTokenType>(::LuaTokenType) {
        private object Keyword : TokenSort
        private object Operator : TokenSort
        private object Constant : TokenSort
        private object Comment : TokenSort
        private object Number : TokenSort
        private object StringLiteral : TokenSort
        private object Parens : TokenSort
        private object Braces : TokenSort
        private object Brackets : TokenSort
        private object WhiteSpace : TokenSort
        private object StmtBegin : TokenSort
        private object StmtEnd : TokenSort

        val keywords by lazy { tokenSetOf(Keyword) }
        val operators by lazy { tokenSetOf(Operator) }
        val constants by lazy { tokenSetOf(Constant) }
        val comments by lazy { tokenSetOf(Comment) }
        val numbers by lazy { tokenSetOf(Number) }
        val strings by lazy { tokenSetOf(StringLiteral) }
        val parens by lazy { tokenSetOf(Parens) }
        val braces by lazy { tokenSetOf(Braces) }
        val brackets by lazy { tokenSetOf(Brackets) }
        val whiteSpaces by lazy { tokenSetOf(WhiteSpace) }
        val beginStmt by lazy { tokenSetOf(StmtBegin) }
        val endStmt by lazy { tokenSetOf(StmtEnd) }

        val WHITE_SPACE by token(WhiteSpace)

        val FALSE by token(Constant)
        val TRUE by token(Constant)

        val NIL by token(Constant)

        val GOTO by token(Keyword, StmtBegin)
        val END by token(Keyword, StmtEnd)

        val IF by token(Keyword, StmtBegin)
        val THEN by token(Keyword)
        val ELSE by token(Keyword)
        val ELSEIF by token(Keyword)

        val FOR by token(Keyword, StmtBegin)
        val UNTIL by token(Keyword, StmtBegin)
        val WHILE by token(Keyword, StmtBegin)
        val BREAK by token(Keyword, StmtBegin)
        val REPEAT by token(Keyword, StmtBegin)
        val DO by token(Keyword, StmtBegin)

        val AND by token(Keyword)
        val OR by token(Keyword)
        val NOT by token(Keyword)

        val LOCAL_FUNCTION by token(Keyword, StmtBegin)
        val FUNCTION by token(Keyword, StmtBegin)
        val LOCAL by token(Keyword, StmtBegin)
        val RETURN by token(Keyword, StmtBegin)

        val IN by token(Keyword)

        val PLUS by token(Operator)     // +
        val MINUS by token(Operator)    // -
        val MUL by token(Operator)      // *
        val DIV by token(Operator)      // /
        val MOD by token(Operator)      // %
        val POW by token(Operator)      // ^
        val IDIV by token(Operator)     // //

        val SHARP by token(Operator)    // #
        val DOTDOT by token(Operator)   // ..

        val BAND by token(Operator)     // &
        val BNOT by token(Operator)     // ~
        val BOR by token(Operator)      // |

        val SHL by token(Operator)      // <<
        val SHR by token(Operator)      // >>

        val EQ by token(Operator)       // ==
        val NEQ by token(Operator)      // ~=
        val LE by token(Operator)       // <=
        val GE by token(Operator)       // >=
        val LT by token(Operator)       // <
        val GT by token(Operator)       // >

        val ASSIGN by token(Operator)   // =

        val SEMICOLON by token(Operator, StmtEnd)    // ;
        val COLON by token(Operator)                 // :
        val DOUBLE_COLON by token(Operator)          // ::
        val COMMA by token(Operator)                 // ,
        val DOT by token(Operator)                   // .
        val DOTDOTDOT by token(Operator)             // ...

        val LINE_COMMENT by token(Comment)
        val BLOCK_COMMENT by token(Comment)

        val IDENTIFIER by token()

        val DEC_INT_NUMBER by token(Number)
        val HEX_INT_NUMBER by token(Number)
        val OCT_INT_NUMBER by token(Number)
        val REAL_NUMBER by token(Number)

        val SINGLE_QUOTED_STRING by token(StringLiteral)
        val DOUBLE_QUOTED_STRING by token(StringLiteral)
        val LONG_BRACKETS_STRING by token(StringLiteral)

        val L_PAREN by token(Parens)     // (
        val R_PAREN by token(Parens)     // )
        val L_BRACE by token(Braces)     // {
        val R_BRACE by token(Braces)     // }
        val L_BRACKET by token(Brackets) // [
        val R_BRACKET by token(Brackets) // ]

        override val tokens = listOf(
            WHITE_SPACE, AND, BREAK, DO, ELSE, ELSEIF, END, FALSE, FOR, LOCAL_FUNCTION, FUNCTION, GOTO,
            IF, IN, LOCAL, NIL, NOT, OR, REPEAT, RETURN, THEN, TRUE, UNTIL, WHILE,
            PLUS, MINUS, MUL, DIV, MOD, POW, SHARP, BAND, BNOT, BOR, SHL, SHR, IDIV, EQ, NEQ, GE, LT, GT, ASSIGN,
            DOTDOT, SEMICOLON, COLON, DOUBLE_COLON, COMMA, DOT, DOTDOTDOT,
            LINE_COMMENT, BLOCK_COMMENT, IDENTIFIER, DEC_INT_NUMBER, HEX_INT_NUMBER, OCT_INT_NUMBER, REAL_NUMBER,
            SINGLE_QUOTED_STRING, DOUBLE_QUOTED_STRING, LONG_BRACKETS_STRING,
            L_PAREN, R_PAREN, L_BRACE, R_BRACE, L_BRACKET, R_BRACKET
        )
    }
}
