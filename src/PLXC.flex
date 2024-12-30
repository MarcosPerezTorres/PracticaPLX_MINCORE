import java_cup.runtime.*;
import java.text.ParseException;

%%

%unicode
%cup
%line
%column

%{
    public int getLine() { return yyline; }
    public int getColumn() { return yycolumn; }
%}

ENTERO_DEC = 0|([1-9][0-9]*)
ENTERO_OCT = 0[1-7][0-7]*
ENTERO_HEX = 0x[0-9a-fA-F]+

EXP = (e|E)[\+\-]?{ENTERO_DEC}
REAL = {ENTERO_DEC}\.{ENTERO_DEC}{EXP}?

IDENT = [a-zA-Z]+[0-9]*

%%

"{" { return new Symbol(sym.ALL); }
"}" { return new Symbol(sym.CLL); }
"(" { return new Symbol(sym.AP); }
")" { return new Symbol(sym.CP); }
";" { return new Symbol(sym.PYC); }
"," { return new Symbol(sym.COMA); }
"=" { return new Symbol(sym.ASIG); }

"<"  { return new Symbol(sym.MENOR); }
"<=" { return new Symbol(sym.MENORIGUAL); }
">"  { return new Symbol(sym.MAYOR); }
">=" { return new Symbol(sym.MAYORIGUAL); }
"==" { return new Symbol(sym.IGUAL); }
"!=" { return new Symbol(sym.DIST); }

"+"  { return new Symbol(sym.MAS); }
"-"  { return new Symbol(sym.MENOS); }
"*"  { return new Symbol(sym.MULT); }
"/"  { return new Symbol(sym.DIV); }
"%"  { return new Symbol(sym.MOD); }
"++" { return new Symbol(sym.MASMAS); }
"--" { return new Symbol(sym.MENOSMENOS); }

"&&"  { return new Symbol(sym.AND); }
"||"  { return new Symbol(sym.OR); }
"!"   { return new Symbol(sym.NOT); }

print  { return new Symbol(sym.PRINT); }
do     { return new Symbol(sym.DO); }
while  { return new Symbol(sym.WHILE); }
for    { return new Symbol(sym.FOR); }
if     { return new Symbol(sym.IF); }
else   { return new Symbol(sym.ELSE); }

int   { return new Symbol(sym.INT); }
char  { return new Symbol(sym.CHAR); }
float { return new Symbol(sym.FLOAT); }

{ENTERO_DEC} { return new Symbol(sym.ENTERO, Integer.valueOf(yytext())); }
{ENTERO_OCT} { 
    String s = yytext().substring(1);
    return new Symbol(sym.ENTERO, Integer.valueOf(s, 8)); 
}
{ENTERO_HEX} {
    String s = yytext().substring(2);
    return new Symbol(sym.ENTERO, Integer.valueOf(s, 16));
}

{REAL} { return new Symbol(sym.REAL, Float.valueOf(yytext())); }

\'\\n\' {
    return new Symbol(sym.CARACTER, Integer.valueOf(Character.codePointAt("\n", 0)));
}

\'\\b\' {
    return new Symbol(sym.CARACTER, Integer.valueOf(Character.codePointAt("\b", 0)));
}

\'\\f\' {
    return new Symbol(sym.CARACTER, Integer.valueOf(Character.codePointAt("\f", 0)));
}

\'\\t\' {
    return new Symbol(sym.CARACTER, Integer.valueOf(Character.codePointAt("\t", 0)));
}

\'\\r\' {
    return new Symbol(sym.CARACTER, Integer.valueOf(Character.codePointAt("\r", 0)));
}

\'\\\'\' {
    return new Symbol(sym.CARACTER, Integer.valueOf(Character.codePointAt("\'", 0)));
}

\'\\\"\' {
    return new Symbol(sym.CARACTER, Integer.valueOf(Character.codePointAt("\"", 0)));
}

\'\\\\\' {
    return new Symbol(sym.CARACTER, Integer.valueOf(Character.codePointAt("\\", 0)));
}

\'\\u[0-9a-f]{4}\' {
    String s = yytext().substring(3, yytext().length()-1);
    return new Symbol(sym.CARACTER, Integer.valueOf(s, 16));
}

\'.\' {
    String s = yytext().substring(1, yytext().length()-1);
    return new Symbol(sym.CARACTER, Integer.valueOf(Character.codePointAt(s, 0)));
}

{IDENT} { return new Symbol(sym.IDENT, yytext()); }

\s {}
\R {}

[^] { throw new RuntimeException("Token inesperado: <" + yytext() + ">"); }