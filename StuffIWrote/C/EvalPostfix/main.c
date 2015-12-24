w/*********************************************
 * Ethan Petuchowski
 * Dec 21 2015
 *
 * A crappy postfix-expression evaluator
 * Works on whatever I've tried it on so far
 *
 * e.g. 952+-3* ==> 6
 *
 * As stated in Compilers (ch 2), you just
 * scan until you find the first operator,
 * then you apply it to the previous two
 * numbers, and repeat until there are no
 * operators. We implement this using an
 * endless for-loop.
 * ********************************************/

#include <stdio.h>
#include <string.h>

#define BAD_CHAR_ERROR 1
#define BAD_OP_ERROR 2

int is_num(char c) {
    return c >= '0' && c <= '9';
}

int end_input(char c) {
    return c == '\n' || c == EOF;
}

int parse_char(char c) {
    if (c < '0' || c > '9') {
        fprintf(stderr, "Bad char input: %c\n", c);
        return BAD_CHAR_ERROR;
    }
    return c - '0';
}

char to_char(int i) {
    return (char)(i + '0');
}

/* an example is "952+-" => 2 */
int main() {
    char postfix_expr[80];

    printf("enter the clue\n$ ");

    /* read input */
    int done;
    char in;
    int i = 0;
    do {
        scanf("%c", &in);
        done = end_input(in);
        if (!done) postfix_expr[i++] = in;
    } while (!done);
    postfix_expr[i] = '\0';

    printf("rcvd: %s\n", postfix_expr);

    /* evaluate */
    for (;;) {
        int loc = 0;
        while (is_num(postfix_expr[loc++]));
        int nextLoc = loc--;
        char op = postfix_expr[loc--];
        if (op == '\0') {
            printf("evaluated: %s\n", postfix_expr);
            return 0;
        }
        int r = parse_char(postfix_expr[loc--]);
        if (r == BAD_CHAR_ERROR) {
            return BAD_CHAR_ERROR;
        }
        int l = parse_char(postfix_expr[loc]);
        if (l == BAD_CHAR_ERROR) {
            return BAD_CHAR_ERROR;
        }
        switch (op) {
            case '+': postfix_expr[loc] = to_char(l + r); break;
            case '-': postfix_expr[loc] = to_char(l - r); break;
            case '*': postfix_expr[loc] = to_char(l * r); break;
            case '/': postfix_expr[loc] = to_char(l / r); break;

            default:
                fprintf(stderr, "I'm labeled as a bad character: %c", op);
                return BAD_OP_ERROR;
        }
        do { postfix_expr[++loc] = postfix_expr[nextLoc]; }
        while (postfix_expr[nextLoc++] != '\0');
    }
}
