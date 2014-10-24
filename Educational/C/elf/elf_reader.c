#include <elf.h>
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <errno.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h> /* sysconf(_SC_PAGESIZE) */

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
#include <unistd.h>
typedef char bool;
/* this comes from the `man 2 mmap` page */
#define print_error(msg) \
    do { perror(msg); exit(EXIT_FAILURE); } while (0)

/* I believe since this is a macro it will work an varargs too */
#define print_failure(msg) \
    do { fprintf(stderr, msg); exit(EXIT_FAILURE); } while (0)

/* http://linux.die.net/man/2/getpagesize */
#define PAGE_ALIGN(strt) ((strt) & ~(sysconf(_SC_PAGESIZE)-1))


int main(int argc, const char *argv[])
{
    const char *name_to_open;
    int fd, ret, idx;
    off_t filesize = 0;
    struct stat filestat;
    Elf64_Ehdr *hdr_p; /* in <elf.h> */

    if (argc == 1) {
        printf("reading e_copy\n");
        name_to_open = "e_copy";
    }
    else {
        printf("reading given file: %s\n", argv[1]);
        name_to_open = argv[1];
    }


    fd = open(name_to_open, O_RDONLY);

    if (fd == -1) {
        print_error("open");
    }

    /* get and print filesize */
    ret = stat(name_to_open, &filestat);

    if (ret == -1) {
        print_error("stat");
    }

    filesize = filestat.st_size;
    printf("filesize: %jd\n", (intmax_t)filesize);

    /* I'm doing this wrong. I should be reading it all into memory,
       then I can just index into the locations I want to access etc,
       and not have to worry about preads or segfaults or mallocs etc. */

    /* mmap(the whole executable) */
    char* exec_addr;
    exec_addr = mmap(
        (void*)0x200000,/* just put it out of the way */
        filesize,       /* length of image */
        PROT_READ,      /* pages may be read but not written or exec'd */
        MAP_PRIVATE, /* COW (shouldn't matter => it's prot_read) */
        fd,             /* the file opened above */
        0               /* start at the very beginning (good place to start) */
    );

    if ( exec_addr == (char*)(-1) ) {
        print_error("mmap exec_addr");
    }

    /* cast to the elf header */
    hdr_p = (Elf64_Ehdr *)exec_addr;

    /* ensure valid elf version number */
    assert(hdr_p->e_version == EV_CURRENT);

    /* ensure it's an executable file */
    assert(hdr_p->e_type == ET_EXEC);

    printf("Entry point: 0x%x\n", (unsigned int)hdr_p->e_entry);

    /* TEMPORARY (until I relocate THIS executable) */
    uint64_t temp_offset = 0x800000;

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
        printf("Loading segment at idx %d...\n", idx);

        printf( /* relevant LOAD segment metadata: */
            "offset in file:    0x%x\n"
            "virt addr:         0x%lx\n"
            "size in file:      0x%x\n"
            "size in RAM:       0x%x\n"
            "flags:             0x%x\n"
            "align constraint:  0x%x\n\n",
            (uint32_t) ph->p_offset,
            (uint64_t) ph->p_vaddr,
            (uint32_t) ph->p_filesz,
            (uint32_t) ph->p_memsz,
            (uint32_t) ph->p_flags,
            (uint32_t) ph->p_align);

        /* copy file segment from filedesc to virtual memory segment */

        int mmap_prot = ((ph->p_flags & PF_X) ? PROT_EXEC  : 0)
                      | ((ph->p_flags & PF_W) ? PROT_WRITE : 0)
                      | ((ph->p_flags & PF_R) ? PROT_READ  : 0);

        char* seg_addr = mmap(
            (void*) (ph->p_vaddr - ph->p_offset + temp_offset),
            ph->p_filesz, /* NOTE: this is just how long the raw data is
                             the in-RAM segment is bigger, so TODO I'll mmap
                             another chunk above this one?? */
            mmap_prot,
            MAP_PRIVATE | MAP_FIXED /* HAS to mmap to loc I request */,
            fd,
            PAGE_ALIGN(ph->p_offset)
        );

        if ( seg_addr == (char*)(-1) ) {
            print_error("mmap seg_addr");
        } else {
            printf("loaded into address: 0x%lx\n\n", (uint64_t)seg_addr);
        }

        /* MAP_ANON the .bss */
        if (ph->p_filesz < ph->p_memsz) {
            /* TODO */
        }
    }

    return 0;
}
