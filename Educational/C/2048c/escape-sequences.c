// 4/6/14
// Ethan Petuchowski

#include <stdio.h>

int main()
{
    for (int i = 0; i < 4; ++i) {
        puts("asdf");
        puts("asdf");
        // puts("\e[2J");
        puts("\e[2J\eH");
        puts("asdf");
        puts("asdf");
        return 0;
    }
}
