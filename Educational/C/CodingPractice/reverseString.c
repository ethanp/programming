/** Ethan Petuchowski  9/21/13 */

#include <stdio.h>
#include <string.h>

/* prototypes */
int main();
void reverse(char*);
void testReverse();

int main() {testReverse();}

/* reverse a string */
void reverse(char* str)
{
    int len = strlen(str);
    int i = 0, j = len;
    char reversed[len+1];
    reversed[j--] = '\0';
    for (i = 0; i < len; ++i)
        reversed[j--] = str[i];
    strcpy(str, reversed);
}

void testReverse()
{
    char hello[] = "hello world";
    printf("%s\n", hello);
    printf("\nNow reversed:\n");
    reverse(hello);
    printf("%s\n", hello);
}
