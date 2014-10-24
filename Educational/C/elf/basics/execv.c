#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>

main() {
execve("hello",NULL,NULL);
printf("Error");
}

