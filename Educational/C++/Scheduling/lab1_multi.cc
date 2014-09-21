//#define _GNU_SOURCE
#include "city.h"
#include <stdio.h>      /* printf */
#include <iostream>
#include <fstream>
#include <string>
#include <time.h>       /* time_t, struct tm, difftime, time, mktime */
#include <stdlib.h>     /* atoi */
#include <sys/wait.h>
#include <sched.h>
#include <unistd.h>
#include "bg.h"

#define FOUR_KB 1 << 12

using namespace std;

static volatile int ready;

int hash_stuff(void *buffer_loc)
{
    char *buffer_to_hash = (char*)buffer_loc;
    uint128 a;
    time_t  start, stop;
    double  seconds;
    int     REPEATS = 800000;
    while (!ready); // wait till all threads have been created
    printf("hash-go\n");
    // ------- HASH LOOP ---------- //
    time(&start);
    for (int i = 0; i < REPEATS; i++)
        a = CityHash128(buffer_to_hash, FOUR_KB);
    time(&stop);
    // ---------------------------- //
    seconds = difftime(stop, start);
    printf("%.f seconds elapsed doing %d hashes\n", seconds, REPEATS);
    return EXIT_SUCCESS;
}

#define STACK_SIZE (1024 * 1024)

int main(int argc, char const *argv[])
{
    printf("Something\n");
    int bg_procs = 0;
    if (argc >= 2) {
        bg_procs = atoi(argv[1]);
        if (bg_procs < 1) {
            printf("Must have at least one processor\n");
            exit(EXIT_FAILURE);
        }
    } else {
        printf("Enter number of processors\n");
        exit(EXIT_FAILURE);
    }
    printf("%d procs requested\n", bg_procs);
    void *buffer_to_hash = malloc(FOUR_KB);
    if (buffer_to_hash == NULL) {
        printf("Unable to allocate space for buffer-to-hash.\n");
        return -1;
    }
    ifstream urandom_file("/dev/urandom");
    if (!urandom_file.is_open()) {
        printf("Unable to open urandom\n");
        return -1;
    }
    urandom_file.read(reinterpret_cast<char*>(buffer_to_hash), FOUR_KB);
    char *stacks[bg_procs];
    pid_t pids[bg_procs];
    start(bg_procs);
    ready = 0; // don't let hashing start yet
    for (int proc = 0; proc < bg_procs; proc++) {
        stacks[proc] = (char*) malloc(STACK_SIZE);
        if (stacks[proc] == NULL) {
            printf("malloc failed\n");
            exit(EXIT_FAILURE);
        }
        pids[proc] = clone(hash_stuff, stacks[proc]+STACK_SIZE, CLONE_VM, buffer_to_hash);
    }
    ready = 1; // hashing should start now
    int status;
    for (int proc = 0; proc < bg_procs; proc++) {
        printf("collecting hash proc %d\n", pids[proc]);
        waitpid(pids[proc], &status, __WCLONE); // wait() *only* looks for "non-clone" children
        if (!WIFEXITED(status) || WEXITSTATUS(status) != EXIT_SUCCESS) {
            printf("problem with hash thread occurred\n");
        }
    }
    stop();
    printf("terminating\n");
    exit(EXIT_SUCCESS);
}
