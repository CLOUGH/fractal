// user customisations
package fractal.syntax;
import java_cup.runtime.*;
import java.io.IOException;

// Jlex directives
%%
    
%cup
%public

%class FractalLexer

%type java_cup.runtime.Symbol

%eofval{
	return new Symbol(sym.EOF);
%eofval}

%eofclose false

%char
%column
%line

%{
    public int getChar() {
		return yychar + 1;
    }

    public int getColumn() {
    	return yycolumn + 1;
    }

    public int getLine() {
		return yyline + 1;
    }

    public String getText() {
		return yytext();
    }
%}

nl = [\n\r] 	
cc = ([\b\f]|{nl})
ws = {cc}|[\t ]
alpha = [a-zA-Z_"$""#""?""@""~"]
alphanum = {alpha}|[0-9]
floatnum = (-[0-9]+\.[0-9]+)|([0-9]+\.[0-9]+) 

%%
//OPERATORS
<YYINITIAL>	{ws}	{/* ignore whitespace */}
<YYINITIAL>	"+"	{return new Symbol(sym.PLUS);}
<YYINITIAL>	"-"	{return new Symbol(sym.MINUS);}
<YYINITIAL>	"*"	{return new Symbol(sym.MUL);}
<YYINITIAL>	"/"	{return new Symbol(sym.DIV);}
<YYINITIAL>	"%"	{return new Symbol(sym.MOD);}
<YYINITIAL>	"("	{return new Symbol(sym.LPAREN);}
<YYINITIAL>	")"	{return new Symbol(sym.RPAREN);}
<YYINITIAL>	","	{return new Symbol(sym.COMMA);}
<YYINITIAL>	"["	{return new Symbol(sym.RBRACE);}
<YYINITIAL>	"]"	{return new Symbol(sym.LBRACE);}
<YYINITIAL> 	"@"	{return new Symbol(sym.ATSYM);}
<YYINITIAL>	"!"	{return new Symbol(sym.EXCLAMATION);}
<YYINITIAL>	";"	{return new Symbol(sym.SEMI);}

//FRACTAL KEYWORDS
<YYINITIAL>	"fractal"	{return new Symbol(sym.FRACTAL);}
<YYINITIAL>	"render"	{return new Symbol(sym.RENDER);}
<YYINITIAL>	"level"	 	{return new Symbol(sym.LEVEL);}
<YYINITIAL>	"scale"		{return new Symbol(sym.SCALE);}
<YYINITIAL>	"save"		{return new Symbol(sym.SAVE);}
<YYINITIAL>	"restore"	{return new Symbol(sym.RESTORE);}
<YYINITIAL>	"def"		{return new Symbol(sym.DEF);}
<YYINITIAL>	"self"		{return new Symbol(sym.SELF);}
<YYINITIAL>	"end"		{return new Symbol(sym.END);}

//TURTLE KEYWORDS
<YYINITIAL>	"forward"|"fd"	{return new Symbol(sym.FORWARD);}
<YYINITIAL>	"back"|"bk"	{return new Symbol(sym.BACK);}
<YYINITIAL>	"left"|"lt"	{return new Symbol(sym.LEFT);}
<YYINITIAL>	"right"|"rt"	{return new Symbol(sym.RIGHT);}
<YYINITIAL>	"penup"|"pu"	{return new Symbol(sym.PENUP);}
<YYINITIAL>	"pendown"|"pd"	{return new Symbol(sym.PENDOWN);}
<YYINITIAL>	"home"		{return new Symbol(sym.HOME);}
<YYINITIAL>	"clear"		{return new Symbol(sym.CLEAR);}

//INTEGER
<YYINITIAL>    [0-9]+ {return new Symbol(sym.INTEGER, new Integer(yytext()));}

//Floating Numbers
<YYINITIAL>	{floatnum}	{return new Symbol(sym.DOUBLE, new Double(yytext()));}

// VARIABLE
<YYINITIAL>    {alpha}{alphanum}* {return new Symbol(sym.VARIABLE, yytext());}


