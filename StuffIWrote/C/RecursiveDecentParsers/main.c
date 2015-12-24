/**
 * Ethan Petuchowski    December 24, 2015
 *
 * "Compilers" Ex. 2.4.1.a
 *
 * Write a recursive-descent parser for the grammar
 * 
 * S -> + S S | - S S | a
 */
#include <stdio.h>
#include <stdlib.h>

/* The terminology here is surprising, but in the book,
 * this term is used to denote the input non/terminal
 * currently under examination for expansion or finding
 * a parse-error
 */
char lookaheadSymbol;

char advanceLookahead() {
    int aChar = getchar();
    if (aChar == EOF || aChar == '\n') {
        printf("input was parsed successfully");
        exit(0);
    }
    lookaheadSymbol = (char) aChar;
    return lookaheadSymbol;
}

void match(char c) {
    if (lookaheadSymbol == c) {
        advanceLookahead();
    }
    else {
        fprintf(stderr, "found %c, expecting %c", lookaheadSymbol, c);
        abort();
    }
}

/** S -> + S S | - S S | a */
void sGrammar() {
    switch (lookaheadSymbol) {
        case '+':
            match('+');
            sGrammar();
            sGrammar();
            break;
        case '-':
            match('-');
            sGrammar();
            sGrammar();
            break;
        case 'a':
            match('a');
            break;
        default:
            fprintf(stderr, "parser error at %c", lookaheadSymbol);
            abort();
    }
}

int main() {
    printf("enter your string\n$ ");
    advanceLookahead();
    sGrammar();
    char c = advanceLookahead();
    fprintf(stderr, "expected end of input, found %c", c);
    return 0;
}
