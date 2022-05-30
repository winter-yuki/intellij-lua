package com.github.winteryuki.intellijlua.psi

import com.github.winteryuki.intellijlua.LuaLanguage
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.annotations.NonNls
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

class LuaTokenType private constructor(@NonNls debugName: String) : IElementType(debugName, LuaLanguage) {
    override fun toString(): String = "LuaTokenType.${super.toString()}"

    companion object {
        private interface Sort
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

        private val tokenSorts = mutableMapOf<LuaTokenType, Set<Sort>>()
        private fun token(vararg sorts: Sort) = PropertyDelegateProvider { _: Any?, property ->
            val token by lazy { LuaTokenType(property.name).also { tokenSorts[it] = sorts.toSet() } }
            ReadOnlyProperty<Any?, LuaTokenType> { _, _ -> token }
        }

        private fun tokenSetOf(sort: Sort): TokenSet {
            val filtered = tokens.filter { sort in tokenSorts[it].orEmpty() }
            return TokenSet.create(*filtered.toTypedArray())
        }

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

        val WHITE_SPACE by token(WhiteSpace)

        val FALSE by token(Constant)
        val TRUE by token(Constant)

        val NIL by token(Constant)

        val GOTO by token(Keyword)
        val END by token(Keyword)

        val IF by token(Keyword)
        val THEN by token(Keyword)
        val ELSE by token(Keyword)
        val ELSEIF by token(Keyword)

        val FOR by token(Keyword)
        val UNTIL by token(Keyword)
        val WHILE by token(Keyword)
        val BREAK by token(Keyword)
        val REPEAT by token(Keyword)
        val DO by token(Keyword)

        val AND by token(Keyword)
        val OR by token(Keyword)
        val NOT by token(Keyword)

        val FUNCTION by token(Keyword)
        val LOCAL by token(Keyword)
        val RETURN by token(Keyword)

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

        val SEMICOLON by token(Operator)     // ;
        val COLON by token(Operator)         // :
        val DOUBLE_COLON by token(Operator)  // ::
        val COMMA by token(Operator)         // ,
        val DOT by token(Operator)           // .
        val DOTDOTDOT by token(Operator)     // ...

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

        // Delegate providers are lazy so manual initialization is needed anyway
        private val tokens = listOf(
            WHITE_SPACE,
            AND, BREAK, DO, ELSE, ELSEIF, END, FALSE, FOR, FUNCTION, GOTO,
            IF, IN, LOCAL, NIL, NOT, OR, REPEAT, RETURN, THEN, TRUE, UNTIL, WHILE,
            PLUS, MINUS, MUL, DIV, MOD, POW, SHARP, BAND, BNOT, BOR, SHL, SHR, IDIV, EQ, NEQ, GE, LT, GT, ASSIGN,
            DOTDOT, SEMICOLON, COLON, DOUBLE_COLON, COMMA, DOT, DOTDOTDOT,
            LINE_COMMENT, BLOCK_COMMENT, IDENTIFIER, DEC_INT_NUMBER, HEX_INT_NUMBER, OCT_INT_NUMBER, REAL_NUMBER,
            SINGLE_QUOTED_STRING, DOUBLE_QUOTED_STRING, LONG_BRACKETS_STRING,
            L_PAREN, R_PAREN, L_BRACE, R_BRACE, L_BRACKET, R_BRACKET
        )
    }
}
