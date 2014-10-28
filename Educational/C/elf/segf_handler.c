#include <elf.h>
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <errno.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>     /* sysconf(_SC_PAGESIZE) */
#include <string.h>     /* strlen */
#include <sys/mman.h>   /* mmap */
#include <unistd.h>     /* pread, lseek */
#include <signal.h>

typedef struct {
    int mmap_prot;
    int mmap_flags;
    int fd;
    void* start;
    void* end;
} memory_region;

#define ARR_LEN 10
memory_region *mm_arr[ARR_LEN];

memory_region* get_region_containing(void* loc) {
    int idx;
    for (idx = 0; idx < ARR_LEN; idx++) {
        if (mm_arr[idx] == NULL) {
            fprintf(stderr, "hit end of the array at idx %d\n", idx);
            return NULL;
        }
        else if (loc && mm_arr[idx]->start <= loc && loc <= mm_arr[idx]->end) {
            fprintf(stderr, "Matching region found at idx %d\n", idx);
            return mm_arr[idx];
        }
        else {
            printf("Region idx %d with bounds 0x%lx -> 0x%lx didn't fit for addr 0x%lx\n",
                idx, (uint64_t)mm_arr[idx]->start, (uint64_t)mm_arr[idx]->end, (uint64_t)loc);
        }
    }
    fprintf(stderr, "no matching region found\n");
    return NULL;
}

void map_it(int sig, siginfo_t *si, void *unused) {
    static int cpt = 0;
    ++cpt;
    fprintf(stderr, "%dth error, loc = (0x%lx)\n", cpt, si->si_addr);
    memory_region* mm = get_region_containing(si->si_addr);
    if (mm != NULL) {
        printf("found something!\n");
    }
    if(cpt >= 10) {
        exit(0);
    }
}

int main(int argc, char** argv) {
    struct sigaction action;
    action.sa_handler = map_it;
    action.sa_flags = 0;

    sigaction(SIGSEGV, &action, NULL);

    memory_region* reg1 = (memory_region*)malloc(sizeof(memory_region));
    reg1->start = (void*)0xa00000;
    reg1->end = (void*)0xa20000;
    reg1->fd = 0;
    reg1->mmap_prot = PROT_READ | PROT_WRITE | PROT_EXEC;
    reg1->mmap_flags = MAP_PRIVATE | MAP_ANONYMOUS;

    mm_arr[0] = reg1;

    uint64_t a = *(uint64_t*)0xa00000;

    return EXIT_SUCCESS;
}
