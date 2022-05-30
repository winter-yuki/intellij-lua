package com.github.winteryuki.intellijlua.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static com.intellij.psi.TokenType.BAD_CHARACTER;

import com.github.winteryuki.intellijlua.psi.LuaTokenType;

%%
%{
    public LuaLexerGenerated() {
        this((java.io.Reader) null);
    }
%}

%public
%class LuaLexerGenerated
%implements FlexLexer
%function advance
%type IElementType
%unicode

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]
Alpha          = [a-zA-Z]
Digit          = [0-9]
AlphaDigit     = {Alpha} | {Digit}

Identifier = [:jletter:] [:jletterdigit:]*
DecIntegerLiteral = 0 | [1-9][0-9]*

LineComment  = "--" {InputCharacter}* {LineTerminator}?
BlockComment = "--[[" ({InputCharacter} | {WhiteSpace})* "]]"

SingleQuotedString = \'([^\'\r\n])*\'
DoubleQuotedString = \"([^\"\r\n])*\"
LongQuotedString = "[[" ({InputCharacter} | {WhiteSpace})* "]]"

DecInt = 0 | [1-9] {Digit}*
HexInt = 0 | 0 [xX] [1-9A-D] ({Digit} | [A-D])*
OctInt = 0 | [1-7] [0-7]*
Real = ([0-9]+\.[0-9]*|[0-9]*\.[0-9]+) ([eE] [+-]? [0-9]+)?

%%

"("                 { return LuaTokenType.Companion.getL_PAREN(); }
"("                 { return LuaTokenType.Companion.getR_PAREN(); }
"{"                 { return LuaTokenType.Companion.getL_BRACE(); }
"}"                 { return LuaTokenType.Companion.getR_BRACE(); }
"["                 { return LuaTokenType.Companion.getL_BRACKET(); }
"]"                 { return LuaTokenType.Companion.getR_BRACKET(); }

";"                 { return LuaTokenType.Companion.getSEMICOLON(); }
":"                 { return LuaTokenType.Companion.getCOLON(); }
"::"                { return LuaTokenType.Companion.getDOUBLE_COLON(); }
","                 { return LuaTokenType.Companion.getCOMMA(); }
"."                 { return LuaTokenType.Companion.getDOT(); }
"..."               { return LuaTokenType.Companion.getDOTDOTDOT(); }

".."                { return LuaTokenType.Companion.getDOTDOT(); }
"#"                 { return LuaTokenType.Companion.getSHARP(); }

"<<"                { return LuaTokenType.Companion.getSHL(); }
">>"                { return LuaTokenType.Companion.getSHR(); }
"|"                 { return LuaTokenType.Companion.getBOR(); }
"&"                 { return LuaTokenType.Companion.getBAND(); }
"~"                 { return LuaTokenType.Companion.getBNOT(); }

"+"                 { return LuaTokenType.Companion.getPLUS(); }
"-"                 { return LuaTokenType.Companion.getMINUS(); }
"*"                 { return LuaTokenType.Companion.getMUL(); }
"/"                 { return LuaTokenType.Companion.getDIV(); }
"//"                { return LuaTokenType.Companion.getIDIV(); }
"%"                 { return LuaTokenType.Companion.getMOD(); }
"^"                 { return LuaTokenType.Companion.getPOW(); }

"<"                 { return LuaTokenType.Companion.getLT(); }
"<="                { return LuaTokenType.Companion.getLE(); }
">"                 { return LuaTokenType.Companion.getGT(); }
">="                { return LuaTokenType.Companion.getGE(); }
"=="                { return LuaTokenType.Companion.getEQ(); }
"~="                { return LuaTokenType.Companion.getNEQ(); }

"="                 { return LuaTokenType.Companion.getASSIGN(); }

"true"              { return LuaTokenType.Companion.getTRUE(); }
"false"             { return LuaTokenType.Companion.getFALSE(); }

"nil"               { return LuaTokenType.Companion.getNIL(); }

"not"               { return LuaTokenType.Companion.getNOT(); }
"or"                { return LuaTokenType.Companion.getOR(); }
"and"               { return LuaTokenType.Companion.getAND(); }

"if"                { return LuaTokenType.Companion.getIF(); }
"then"              { return LuaTokenType.Companion.getTHEN(); }
"else"              { return LuaTokenType.Companion.getELSE(); }
"elseif"            { return LuaTokenType.Companion.getELSEIF(); }
"end"               { return LuaTokenType.Companion.getEND(); }
"goto"              { return LuaTokenType.Companion.getGOTO(); }

"while"             { return LuaTokenType.Companion.getWHILE(); }
"repeat"            { return LuaTokenType.Companion.getREPEAT(); }
"for"               { return LuaTokenType.Companion.getFOR(); }
"do"                { return LuaTokenType.Companion.getDO(); }
"until"             { return LuaTokenType.Companion.getUNTIL(); }
"break"             { return LuaTokenType.Companion.getBREAK(); }
"in"                { return LuaTokenType.Companion.getIN(); }

"local"             { return LuaTokenType.Companion.getLOCAL(); }
"function"          { return LuaTokenType.Companion.getFUNCTION(); }

"return"            { return LuaTokenType.Companion.getRETURN(); }

{LineComment}       { return LuaTokenType.Companion.getLINE_COMMENT(); }
{BlockComment}      { return LuaTokenType.Companion.getBLOCK_COMMENT(); }

{Identifier}        { return LuaTokenType.Companion.getIDENTIFIER(); }

{SingleQuotedString} { return LuaTokenType.Companion.getSINGLE_QUOTED_STRING(); }
{DoubleQuotedString} { return LuaTokenType.Companion.getDOUBLE_QUOTED_STRING(); }
{LongQuotedString}   { return LuaTokenType.Companion.getLONG_BRACKETS_STRING(); }

{DecInt}            { return LuaTokenType.Companion.getDEC_INT_NUMBER(); }
{HexInt}            { return LuaTokenType.Companion.getHEX_INT_NUMBER(); }
{OctInt}            { return LuaTokenType.Companion.getOCT_INT_NUMBER(); }
{Real}              { return LuaTokenType.Companion.getREAL_NUMBER(); }

{WhiteSpace}        { return LuaTokenType.Companion.getWHITE_SPACE(); }

[^]                 { return BAD_CHARACTER; }
