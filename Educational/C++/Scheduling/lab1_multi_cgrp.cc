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
#include "bg.h"
#include <ctime>        /* clock, clock_t, CLOCKS_PER_SEC */
#include <libcgroup.h>

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
    /* get desired number of hash procs */
    printf("Beginning...\n");
    int hash_procs = 0;
    if (argc >= 2) {
        hash_procs = atoi(argv[1]);
        if (hash_procs < 1) {
            printf("Must have at least one processor\n");
            exit(EXIT_FAILURE);
        }
    } else {
        printf("Enter number of processors\n");
        exit(EXIT_FAILURE);
    }
    printf("%d procs requested\n", hash_procs);

    /* get desired number of bg procs */
    int bg_procs = 0;
    if (argc >= 3) {
        bg_procs = atoi(argv[2]);
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

    int ret = cgroup_init();
    if (ret) {
        printf("cgroup_init failed with '%d': %s\n", ret, cgroup_strerror(ret));
        exit(EXIT_FAILURE);
    }
    /* prep for hash procs */
    char *stacks[hash_procs];
    pid_t pids[hash_procs];
    ready = 0; // don't let hashing start yet
    if (bg_procs > 0)
        start(bg_procs);  // start bg procs

    /* create clone procs for hashing */
    for (int proc = 0; proc < hash_procs; proc++) {
        int err;
        stacks[proc] = (char*) malloc(STACK_SIZE);
        if (stacks[proc] == NULL) {
            printf("malloc failed\n");
            exit(EXIT_FAILURE);
        }
        pids[proc] = clone(hash_stuff, stacks[proc]+STACK_SIZE, CLONE_VM, buffer_to_hash);
        /* assign hash procs to highest weight cpu-share cgroup */
        // this did not work when all tasks were added to the same cgroup
        struct cgroup *faster_group;
        struct cgroup_controller *controller;
        const int64_t HIGH_WEIGHT = 2048;
        char* group_name = (char*)malloc(10);
        sprintf(group_name, "faster%d", proc);
        faster_group = cgroup_new_cgroup(group_name);
        if (!faster_group) {
            printf("cannot create 'faster' cgroup\n");
            exit(EXIT_FAILURE);
        }
        controller = cgroup_add_controller(faster_group, "cpu");
        cgroup_add_value_int64(controller, "cpu.shares", HIGH_WEIGHT);
        int64_t upcasted_pid = (int64_t)pids[proc];
        cgroup_add_value_int64(controller, "tasks", upcasted_pid);
        err = cgroup_create_cgroup(faster_group, 0); // don't ignore errors
        if (err) {
            printf("create_cgroup failed with '%d': %s\n", err, cgroup_strerror(err));
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
