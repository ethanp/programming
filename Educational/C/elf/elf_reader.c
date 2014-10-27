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

/**** STACK's AUXiliary Vector ****/
/* There are a bunch of these things def'd in include/uapi/linux/auxvec.h */
/* But I don't think I need them because it looks like libc does this for me */
#define AT_NULL   0 /* end of vector */
#define AT_IGNORE 1 /* entry should be ignored */
/* ... etc. */

typedef char bool;

/* this comes from the `man 2 mmap` page */
#define print_error(msg) \
    do { perror(msg); exit(EXIT_FAILURE); } while (0)

/* I believe that since this is a macro it will work an varargs too */
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
        char *post = strlen(*env) > 30 ? "..." : ""`;
        printf("|%d| (%x): %.30s%s\n", idx, env, *env, post);
    }
}
*/

int open_file_to_exec(int argc, const char* argv[])
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

void setup_the_stack(int argc, const char *argv[],
                     const char *envp[],
                     uint64_t entrypoint)
{
    /* zero-out the registers cleared in ELF_PLAT_INIT()
        (viz. all the "general purpose registers") */

    /* Figure out a stack location and mmap it. The way this is going to work
       is by mmapping a bunch of space, and since it will automagically hand
       me space that is not taken, that is what I'll use. */
    uint64_t *bp, *sp, *stack_bottom;
    stack_bottom = mmap( /* addr, size, prot, flgs, fd, offset */
        0, ((8 << 3 /*bits2bytes*/) << 20 /*mega*/), /* 8MB (seems standard) */
        PROT_READ | PROT_WRITE,                      /* non-executable stack */
        MAP_PRIVATE | MAP_ANONYMOUS | MAP_GROWSDOWN, /* COW, backed by zeros */
        0, 0
    );

    if ( stack_bottom == (uint64_t*)(-1) ) {
        print_error("mmap stack_bottom");
    } else {
        printf("stack bottom at address: 0x%lx\n", (uint64_t)stack_bottom);
    }

    // bus error at 0x0000000000437650 (this address is mmap'd, so it's
    // probably a null pointer from somewhere?)

    bp = sp = stack_bottom - 20; /* highest address inside stack area */

    *sp-- = NULL; /* very "bottom" of stack */

    /* go to the last envp */
    char** env; int idx = 0;
    for (env = envp; *env != 0; env++, idx++);

    /* load the envp's backwards */
    while (idx--) {
        *sp-- = *(--env);
    }

    *sp-- = NULL; /* envp/argv separator */

    /* load the argv backwards */
    int argcount = argc-1;
    while (argcount--) {
        *sp-- = argv[argcount+1];
    }

    *sp = argc-1; /* argc */

    printf("Watchout I'm jumping in!\n");

    /* this WORKS according to GDB "info registers" after its execution */
    __asm__ (
        "mov $0, %%rax\n"
        "mov $0, %%rbx\n"
        "mov $0, %%rcx\n"
        "mov $0, %%rdx\n"
        "mov $0, %%rsi\n"
        "mov $0, %%rdi\n"
        "mov $0, %%r8\n"
        "mov $0, %%r9\n"
        "mov $0, %%r10\n"
        "mov $0, %%r11\n"
        "mov $0, %%r12\n"
        "mov $0, %%r13\n"
        "mov $0, %%r14\n"
        "mov $0, %%r15\n"
        :::
        "rax", "rbx", "rcx", "rdx", "rsi", "rdi", "r8",
        "r9", "r10", "r11", "r12", "r13", "r14", "r15"
    );

    __asm__(
        "mov %0, %%rbp\n"
        "mov %1, %%rsp\n"
        "jmp %2\n"
        ::"r"(bp), "r"(sp), "r"(entrypoint)
        :/*"rbp", (not allowed in clobber-list?) */ "rsp"
    );

    printf("This should never print.\n");
}

int main(int argc, const char *argv[], const char *envp[])
{
    int fd, ret, idx;
    off_t filesize = 0;
    struct stat filestat;
    Elf64_Ehdr *hdr_p; /* in <elf.h> */

    fd = open_file_to_exec(argc, argv);

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

        /* copy file segment from filedesc to virtual memory segment */
        char* seg_addr = copy_program_segment(ph, fd);

        if (ph->p_filesz < ph->p_memsz) {
            unsigned long nbyte = ph->p_memsz - ph->p_filesz;
            printf("zero-out 0x%lx bytes for the .bss\n", nbyte);
            void* start = seg_addr + ALIGN_OFFSET(ph->p_vaddr, ph->p_align)
                                   + ph->p_filesz;
            memset(start, 0, nbyte);
        }
    }
    setup_the_stack(argc, argv, envp, (uint64_t)hdr_p->e_entry);

    return 0;
}
