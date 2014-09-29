#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <unistd.h>
#include <errno.h>
#include <sys/wait.h>
#include <stdlib.h>

void main() {
    pid_t pid;
    char *name[] = {
        "/bin/bash",
        "-c",
        "echo 'Hello World'",
        NULL
    };
    int i = 2;
    while (i--) {
        pid = fork();
        if (pid >= 0) {
            if (pid == 0) {
                execvp(name[0], name);
            }
        } else {
            printf("Fork failed, quitting\n");
            exit(1);
        }
    }
}
