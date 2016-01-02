/**
 * 1/1/16
 * This was my attempt at Compilers ex 3.5.1,
 * but I never actually tried it out.
 */

%{
    /* "definitions of manifest constants"

    Text in this section will be copied verbatim into
    Lex's output C-file, lex.yy.c.

        ...not sure what actually goes in here
        ...we need to #DEFINE constants, so maybe like...
    */

    #define LT      1
    #define LE      2
    #define EQ      3
    #define NE      4
    #define GT      5
    #define GE      6
    #define IF      7
    #define THEN    8
    #define ELSE    9
    #define ID      10
    #define NUMBER  11
    #define RELOP   12
    #define WHILE   13
%}

/* regular definitions */
delim   [ \t\n]
ws      {delim}+
letter  [A-Za-z_]
digit   [0-9]
id      {letter}({letter}|{digit})*
number  {digit}+(\.{digit}+)?(E[+-]?{digit}+)?

%%

/* yyval is a global variable accessible to Yacc (and beyond) */
/* the value we return is returned to the 'parser' (viz. Yacc) */

{ws}        { /* no action and no return */ }
if          { return(IF); }
then        { return(THEN); }
else        { return(ELSE); }
while       { return(WHILE); }
{id}        { yyval = (int) installID(); return(ID); }
{number}    { yyval = (int) installNum(); return(NUMBER); }
"<"         { yyval = LT; return(RELOP); }
"<="        { yyval = LE; return(RELOP); }
"=="        { yyval = EQ; return(RELOP); }
"!="        { yyval = NE; return(RELOP); }
">"         { yyval = GT; return(RELOP); }
">="        { yyval = GE; return(RELOP); }

%%

/* Functions in this section are copied verbatim into Lex's
   output C-file, lex.yy.c.

   These functions have access to two variables that Lex generates,
        1. yytext -- pointer to the begining of the current lexeme
        2. yyleng -- length of the current lexeme
*/

/**
 * Install the lexeme whose first char is pointed to by yytext,
 * and whose length is yyleng, into the symbol table;
 * and return a pointer thereto.
 */
int installID() { /* TODO */ }

/**
 * Similar to installID, but puts numerical constants into a
 * separate table.
 */
int installNum() { /* TODO */ }
