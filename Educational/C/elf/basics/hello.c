#include <stdio.h>

int a(int b, int c) {
    return b + c;
}

main(int argc, const char *argv[]) {
    printf("\nIn hello.c:\n");
    printf("%d == 5\n\n", a(2, 3));
}
