#include "city.h"
#include <stdio.h>      /* printf */
#include <iostream>
#include <fstream>
#include <string>
#include <sys/time.h>
#include <sys/types.h>
#include <stdlib.h>     /* atoi */
#include <sys/wait.h>
#include <sched.h>
#include <unistd.h>
#include <sys/sysinfo.h>/* get_nprocs */
#include "bg.h"
#include <ctime>        /* clock, clock_t, CLOCKS_PER_SEC */

#define FOUR_KB 1 << 12

using namespace std;

static volatile int ready; // don't optimize single-use in fctn to constant

int hash_stuff(void *buffer_loc)
{
    uint128         a;
    struct timeval  tp;
    double          sec, usec, start, end, cpu_start, cpu_ms, wall_ms;
    int             REPEATS = 8E5;
    char*           buffer_to_hash = (char*)buffer_loc;
    clock_t         wall_start;

    while (!ready); // wait till all threads have been created
    printf("hash-go\n");

    // ========== HASH LOOP ============= //

    // start wall & cpu timers
    cpu_start = clock();

    gettimeofday(&tp, 0);
    sec = static_cast<double>(tp.tv_sec)   * 1E3;
    usec = static_cast<double>(tp.tv_usec) / 1E3;
    start = sec + usec;


    // compute hashes
    for (int i = 0; i < REPEATS; i++)
        a = CityHash128(buffer_to_hash, FOUR_KB);


    // stop wall & cpu timers
    cpu_ms = (clock() - cpu_start) / (CLOCKS_PER_SEC / 1E3);

    gettimeofday(&tp, 0);
    sec = static_cast<double>(tp.tv_sec)   * 1E3;
    usec = static_cast<double>(tp.tv_usec) / 1E3;
    end = sec + usec;
    wall_ms = end - start;

    // ================================== //

    printf("%.f CPU milliseconds elapsed in %d\n", cpu_ms, getpid());
    printf("%.f WALL milliseconds elapsed in %d\n", wall_ms, getpid());
    return EXIT_SUCCESS;
}

#define STACK_SIZE (1024 * 1024)    // probably way more than enough space

int main(int argc, char const *argv[])
{
    printf("Beginning...\n");

    /* number of hash procs is determined by number of processors found */
    int hash_procs = get_nprocs();
    printf("%d available logical processors found\n", hash_procs);

    /* get desired number of bg procs */
    int bg_procs = 0;
    if (argc >= 2) {
        bg_procs = atoi(argv[1]);
        printf("using %d bg procs\n", bg_procs);
    } else {
        printf("defaulting to 0 bg procs\n");
        bg_procs = 0;
    }

    /* use urandom to initialize buffer to hash */
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

    /* prep for hash procs */
    char *stacks[hash_procs];
    pid_t pids[hash_procs];
    ready = 0; // don't let hashing start yet
    if (bg_procs > 0)
        start(bg_procs);  // start bg procs

    /* create clone procs for hashing & set the affinity of each */
    for (int proc = 0; proc < hash_procs; proc++) {
        stacks[proc] = (char*) malloc(STACK_SIZE);
        if (stacks[proc] == NULL) {
            printf("malloc failed\n");
            exit(EXIT_FAILURE);
        }
        pids[proc] = clone(hash_stuff, stacks[proc]+STACK_SIZE, CLONE_VM, buffer_to_hash);
        cpu_set_t my_set;
        CPU_ZERO(&my_set);
        CPU_SET(proc, &my_set);
        if (sched_setaffinity(pids[proc], sizeof(cpu_set_t), &my_set) != 0) {
            printf("problem with setaffinity for proc #%d, exiting\n", proc);
            exit(EXIT_FAILURE);
        }
    }

    // start global timer
    clock_t  start;
    double   seconds, diff_ms;
    start = clock();
    
    ready = 1; // hashing should start now

    /* don't stop the bg procs until all the hashing procs have been reaped */
    int status;
    for (int proc = 0; proc < hash_procs; proc++) {
        printf("collecting hash proc %d\n", pids[proc]);
        waitpid(pids[proc], &status, __WCLONE); // regular wait() *only* looks for "non-clone" children
        if (!WIFEXITED(status) || WEXITSTATUS(status) != EXIT_SUCCESS) {
            printf("problem with hash thread occurred\n");
        }
    }

    if (bg_procs > 0)
        stop();  // stop the bg procs
    printf("terminating\n");
    exit(EXIT_SUCCESS);
}
