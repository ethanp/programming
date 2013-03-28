#include "header.h"
#include <stdio.h>

void doSomething() {
    doSomethingElse();
}

int main(int argc, const char *argv[])
{
    int i=2;
    float f=2.4;
    doSomething();
    printf("%f ; %d", i+f, i+f);
    return 0;
}
