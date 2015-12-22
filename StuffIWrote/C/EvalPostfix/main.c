#include <stdio.h>
#include <string.h>

#define BAD_CHAR_ERROR 1
#define BAD_OP_ERROR 2

typedef enum { false, true } bool;

bool is_op(char c) {
    return (bool) (
           c == '+'
        || c == '-'
        || c == '*'
        || c == '/'
        || c == '\0'
    );
}

bool is_num(char c) {
    return (bool)(
           c >= '0'
        && c <= '9'
    );
}

bool end_input(char c) {
    return (bool) (
           c == '\n'
        || c == EOF
    );
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

    /* read input */
    char in;
    int i = 0;
    bool done = false;

    printf("enter the clue\n$ ");
    do {
        scanf("%c", &in);
        done = end_input(in);
        if (!done) postfix_expr[i++] = in;
    } while (!done);
    postfix_expr[i] = '\0';

    printf("rcvd: %s\n", postfix_expr);


    /* evaluate */
    while (true) {
        int loc = 0;
        while (is_num(postfix_expr[loc++]));
        int nextLoc = loc;
        loc--;
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
                fprintf(stderr, "they label me as a bad character: %c", op);
                return BAD_OP_ERROR;
        }
        strcpy(&postfix_expr[++loc], &postfix_expr[nextLoc]);
    }
}
