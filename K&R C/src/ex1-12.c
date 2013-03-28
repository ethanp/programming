// 1/13/13
// Exercise 1-12. Write a program that prints its input one word per line.

#include <stdio.h>

int main()
{
    // we must hold characters in an int because EOF is an int
    int c;
    while ((c = getchar()) != EOF)
        if (c != ' ')
            putchar(c);
        else
            putchar('\n');
    return 0;
}
