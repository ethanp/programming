// 4/6/14
// Ethan Petuchowski
//
// prints:
//      asdf
//      bteg
//      asdf
//      bteg
//      asdf
//      bteg

#include <stdio.h>

int main()
{
    for (int i = 3; i--; )
        puts("asdf"), // <<== means you still don't need braces around for-body
        puts("bteg");
    return 0;
}
