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

struct memory_region {
    int mmap_prot;
    int mmap_flags;
    int fd;
    char *start;
    char *end;
};

struct memory_region* get_region_containing(void* loc) {
    return NULL;
}

void map_it(int sig, siginfo_t *si, void *unused) {
    static int cpt = 0;
    ++cpt;
    fprintf(stderr, "%d\n", cpt);
    if(cpt >= 10) {
        exit(0);
    }
}

int main(int argc, char** argv) {
    struct sigaction action;
    action.sa_handler = map_it;
    action.sa_flags = 0;

    sigaction(SIGSEGV, &action, NULL);

    int* a = (int*)0xa00000;
    (*a)++;

    return EXIT_SUCCESS;
}


// void map_it(int signum, siginfo_t* siginfo void* unused) {
//     if (signum == SIGSEGV) {
//         void* location = siginfo->si_addr;
//         struct memory_region *reg = get_region_containing(location);

//         /* if region not found */
//         if (reg == NULL) {
//             fprintf(stderr, "Invalid address %ul\n", (uint64_t)location);

//             /* generate core-dump */
//             signal(SIGSEGV, SIG_DFL);
//             kill(getpid(), SIGSEGV);
//         }

//          valid address: map it
//         mmap(
//             PAGE_ALIGN(location),
//             PAGE_SIZE,
//             reg->mmap_prot,
//             reg->mmap_flags,
//             reg->fd,
//             PAGE_ALIGN(location)
//         );
//     } else {
//         fprintf(stderr, "Unexpected signal!\n");
//         exit(EXIT_FAILURE);
//     }
//     /* now faulting instruction will be re-tried */
// }

// int main(int argc, const char *argv[], const char *envp[]) {
//     struct sigaction action;
//     sigemptyset(&action.sa_mask); /* clear all signals from set */
//     action.sa_handler = map_it;
//     action.sa_flags = 0; /* none needed */

//     /* instead of mmapping PT_LOAD program segments,
//        create struct memory_regions for them */
//     return 0;
// }
