package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaLanguage
import com.github.winteryuki.intellijlua.utils.AbstractSortContainer
import com.github.winteryuki.intellijlua.utils.Sort
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

class LuaTokenType private constructor(@NonNls debugName: String) : IElementType(debugName, LuaLanguage) {
    override fun toString(): String = "LuaTokenType.${super.toString()}"

    companion object : AbstractSortContainer<LuaTokenType>(::LuaTokenType) {
        private object Keyword : Sort
        private object Operator : Sort
        private object Constant : Sort
        private object Comment : Sort
        private object Number : Sort
        private object StringLiteral : Sort
        private object Parens : Sort
        private object Braces : Sort
        private object Brackets : Sort
        private object WhiteSpace : Sort
        private object StmtBegin : Sort
        private object StmtEnd : Sort

        val keywords by lazy { itemSetOf(Keyword) }
        val operators by lazy { itemSetOf(Operator) }
        val constants by lazy { itemSetOf(Constant) }
        val comments by lazy { itemSetOf(Comment) }
        val numbers by lazy { itemSetOf(Number) }
        val strings by lazy { itemSetOf(StringLiteral) }
        val parens by lazy { itemSetOf(Parens) }
        val braces by lazy { itemSetOf(Braces) }
        val brackets by lazy { itemSetOf(Brackets) }
        val whiteSpaces by lazy { itemSetOf(WhiteSpace) }
        val beginStmt by lazy { itemSetOf(StmtBegin) }
        val endStmt by lazy { itemSetOf(StmtEnd) }

        val WHITE_SPACE by item(WhiteSpace)

        val FALSE by item(Constant)
        val TRUE by item(Constant)

        val NIL by item(Constant)

        val GOTO by item(Keyword, StmtBegin)
        val END by item(Keyword, StmtEnd)

        val IF by item(Keyword, StmtBegin)
        val THEN by item(Keyword)
        val ELSE by item(Keyword)
        val ELSEIF by item(Keyword)

        val FOR by item(Keyword, StmtBegin)
        val UNTIL by item(Keyword, StmtBegin)
        val WHILE by item(Keyword, StmtBegin)
        val BREAK by item(Keyword, StmtBegin)
        val REPEAT by item(Keyword, StmtBegin)
        val DO by item(Keyword, StmtBegin)

        val AND by item(Keyword)
        val OR by item(Keyword)
        val NOT by item(Keyword)

        val LOCAL_FUNCTION by item(Keyword, StmtBegin)
        val FUNCTION by item(Keyword, StmtBegin)
        val LOCAL by item(Keyword, StmtBegin)
        val RETURN by item(Keyword, StmtBegin)

        val IN by item(Keyword)

        val PLUS by item(Operator)     // +
        val MINUS by item(Operator)    // -
        val MUL by item(Operator)      // *
        val DIV by item(Operator)      // /
        val MOD by item(Operator)      // %
        val POW by item(Operator)      // ^
        val IDIV by item(Operator)     // //

        val SHARP by item(Operator)    // #
        val DOTDOT by item(Operator)   // ..

        val BAND by item(Operator)     // &
        val BNOT by item(Operator)     // ~
        val BOR by item(Operator)      // |

        val SHL by item(Operator)      // <<
        val SHR by item(Operator)      // >>

        val EQ by item(Operator)       // ==
        val NEQ by item(Operator)      // ~=
        val LE by item(Operator)       // <=
        val GE by item(Operator)       // >=
        val LT by item(Operator)       // <
        val GT by item(Operator)       // >

        val ASSIGN by item(Operator)   // =

        val SEMICOLON by item(Operator, StmtEnd)    // ;
        val COLON by item(Operator)                 // :
        val DOUBLE_COLON by item(Operator)          // ::
        val COMMA by item(Operator)                 // ,
        val DOT by item(Operator)                   // .
        val DOTDOTDOT by item(Operator)             // ...

        val LINE_COMMENT by item(Comment)
        val BLOCK_COMMENT by item(Comment)

        val IDENTIFIER by item()

        val DEC_INT_NUMBER by item(Number)
        val HEX_INT_NUMBER by item(Number)
        val OCT_INT_NUMBER by item(Number)
        val REAL_NUMBER by item(Number)

        val SINGLE_QUOTED_STRING by item(StringLiteral)
        val DOUBLE_QUOTED_STRING by item(StringLiteral)
        val LONG_BRACKETS_STRING by item(StringLiteral)

        val L_PAREN by item(Parens)     // (
        val R_PAREN by item(Parens)     // )
        val L_BRACE by item(Braces)     // {
        val R_BRACE by item(Braces)     // }
        val L_BRACKET by item(Brackets) // [
        val R_BRACKET by item(Brackets) // ]

        override val items = listOf(
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
