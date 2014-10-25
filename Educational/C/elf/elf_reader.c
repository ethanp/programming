#include <elf.h>
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <errno.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h> /* sysconf(_SC_PAGESIZE) */
#include <string.h> /* strlen */

/* void *mmap(
            void*   addr,
            size_t  len,
            int     prot,
            int     flags,
            int     fildes,
            off_t   off); */
#include <sys/mman.h>

/* ssize_t read(int fd, void *buf, size_t num_bytes) */
/* ssize_t pread(int fd, void *buf, size_t nbytes, off_t offset) */
/* off_t lseek(int fildes, off_t offset, int whence); */
#include <unistd.h>

typedef char bool;
/* this comes from the `man 2 mmap` page */
#define print_error(msg) \
    do { perror(msg); exit(EXIT_FAILURE); } while (0)

/* I believe since this is a macro it will work an varargs too */
#define print_failure(msg) \
    do { fprintf(stderr, msg); exit(EXIT_FAILURE); } while (0)

#define ALIGN(strt, align) ((strt) & ~((align) - 1))
#define ALIGN_OFFSET(strt, align) ((strt) & ((align) - 1))

/*
void print_envp(const char *envp[])
{
    printf("Here are the first 5 environment variables:\n");
    char** env; int idx;
    for (idx = 0, env = envp; *env != 0, idx < 5; env++, idx++) {
        char *post = strlen(*env) > 30 ? "..." : "";
        printf("%d| %x: %.30s%s\n", idx, env, *env, post);
    }
}
*/

int open_file(int argc, const char* argv[])
{
    const char *name_to_open;
    if (argc == 1) {
        printf("\nreading e_copy\n");
        name_to_open = "e_copy";
    }
    else {
        printf("reading given file: %s\n", argv[1]);
        name_to_open = argv[1];
    }
    int fd = open(name_to_open, O_RDONLY);
    if (fd == -1) {
        print_error("open");
    }
    return fd;
}

void print_segment_metadata(Elf64_Phdr *ph, int idx)
{
    printf( /* relevant LOAD segment metadata: */
        "Loading segment at idx %d...\n"
        "offset in file:    0x%x\n"
        "virt addr:         0x%lx, aligned to: 0x%lx, meaning offset: 0x%lx\n"
        "size in file:      0x%x\n"
        "size in RAM:       0x%x\n"
        "flags:             0x%x\n"
        "align constraint:  0x%x\n\n",
                   idx,
        (uint32_t) ph->p_offset,
        (uint64_t) ph->p_vaddr,
        (uint64_t) ALIGN(ph->p_vaddr, ph->p_align),
        (uint64_t) ALIGN_OFFSET(ph->p_vaddr, ph->p_align),
        (uint32_t) ph->p_filesz,
        (uint32_t) ph->p_memsz,
        (uint32_t) ph->p_flags,
        (uint32_t) ph->p_align);
}

char *copy_program_segment(Elf64_Phdr *ph, int fd)
{
    void* alloc_addr = (void*) ALIGN(ph->p_vaddr, ph->p_align);

    size_t alloc_size = ALIGN_OFFSET(ph->p_vaddr, ph->p_align) + ph->p_memsz;

    int mmap_prot = ((ph->p_flags & PF_R) ? PROT_READ  : 0)
                  | ((ph->p_flags & PF_W) ? PROT_WRITE : 0)
                  | ((ph->p_flags & PF_X) ? PROT_EXEC  : 0);

    int flags = MAP_PRIVATE | MAP_FIXED;
    off_t offset = ALIGN(ph->p_offset, ph->p_align);
    char* seg_addr = mmap(alloc_addr, alloc_size, mmap_prot, flags, fd, offset);

    if ( seg_addr == (char*)(-1) ) {
        print_error("mmap seg_addr");
    } else {
        printf("loaded into address: 0x%lx\n", (uint64_t)seg_addr);
    }
    return seg_addr;
}

int main(int argc, const char *argv[], const char *envp[/*whoa something new!*/])
{
    int fd, ret, idx;
    off_t filesize = 0;
    struct stat filestat;
    Elf64_Ehdr *hdr_p; /* in <elf.h> */

    fd = open_file(argc, argv);

    /* get and print filesize */
    filesize = lseek(fd, 0, SEEK_END);
    if (filesize == -1) {
        print_error("lseek");
    }

    printf("filesize: %jd\n", (intmax_t)filesize);

    /* mmap(the whole executable) */
    char* exec_addr;
    exec_addr = mmap((void*)0x200000, filesize, PROT_READ, MAP_PRIVATE, fd, 0);

    if ( exec_addr == (char*)(-1) ) {
        print_error("mmap exec_addr");
    }

    hdr_p = (Elf64_Ehdr *)exec_addr;

    /* ensure valid elf version number */
    assert(hdr_p->e_version == EV_CURRENT);

    /* ensure it's an executable file */
    assert(hdr_p->e_type == ET_EXEC);

    printf("Entry point: 0x%x\n", (unsigned int)hdr_p->e_entry);

    /**
     * find LOAD segments in program header table
     * the 2 LOAD segments typically contain .text and .data
     */
    printf("the %d segment types:\n", hdr_p->e_phnum);
    Elf64_Phdr *ph = (Elf64_Phdr *)(exec_addr + hdr_p->e_phoff);
    for (idx = 0; idx < hdr_p->e_phnum; idx++, ph++) {
        if (ph->p_type != PT_LOAD) {
            continue;
        }
        print_segment_metadata(ph, idx);

        /* copy file segment from filedesc to virtual memory segment
           see binfmt_elf.c: "Now use mmap to map the library into memory" */
        char* seg_addr = copy_program_segment(ph, fd);

        if (ph->p_filesz < ph->p_memsz) {
            unsigned long nbyte = ph->p_memsz - ph->p_filesz;
            printf("zero-out 0x%lx bytes for the .bss\n", nbyte);
            void* start = seg_addr + ALIGN_OFFSET(ph->p_vaddr, ph->p_align) + ph->p_filesz;
            memset(start, 0, nbyte);
        }
    }

    /********************** SETUP THE STACK **********************/

    /* zero-out the registers used for passing arguments
       the "clobber list" tells GCC to not assume that it knows in the register
       reference: eli.thegreenplace.net/2011/09/06/stack-frame-layout-on-x86-64 */

    __asm__ ( /* this compiles and works according to GDB */
        "mov $0, %%rdi\n"
        "mov $0, %%rsi\n"
        "mov $0, %%rdx\n"
        "mov $0, %%rcx\n"
        "mov $0, %%r8\n"
        "mov $0, %%r9\n"
        :::
        "rdx", "rsi", "rdx", "rcx", "r8", "r9"
    );

    int a = 0;
    int b = 0;

    /*  set ESP & EBP to point to stack start location (how do I choose that loc?)
      Recall:
        EBP holds steady pointing to a cell that contains the previous EBP (linked-list)
        ESP holds a pointer to the top of the stack (which is the lowest address)
    */

    /*  Load argc, argv, and envp (or the registers...not clear on how this works)
        For me, argc=1, argv[]={name_to_open [from above]}, envp="???"
        Maybe I could just snag envp from the pager's stack and dump it in there?

        Format:
        (dword argc)
        (dword [pointer to program name])
        (dword NULL) ; because I have no other argv's in my case
        (dword [pointer to env[0]])
        ...
        (dword [pointer to env[N-1]])
        (dword NULL) */


    return 0;
}
