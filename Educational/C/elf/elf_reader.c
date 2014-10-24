#include <elf.h>
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <errno.h>
#include <stdlib.h>
#include <assert.h>

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
        NULL,           /* don't care where it goes in VAS */
        filesize,       /* len */
        PROT_READ,      /* pages may be read */
        MAP_PRIVATE,    /* copy on write (shouldn't matter => it's prot_read */
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

    printf("Entry point: 0x%x\n", (unsigned int)hdr_p->e_entry);

    /**
     * find LOAD segments in program header table
     * the 2 LOAD segments typically contain .text and .data
     */
    printf("the %d segment types:\n", hdr_p->e_phnum);
    Elf64_Phdr *ph = (Elf64_Phdr *)(exec_addr + hdr_p->e_phoff);
    for (idx = 0; idx < hdr_p->e_phnum; idx++, ph++) {
        uint32_t type = (uint32_t)ph->p_type;
        bool is_load = type == 1;
        char *load_str = is_load ? ": LOAD! (loading...)" : "";
        printf("%u%s\n", type, load_str);

        /* load it if it's a LOAD segment */
        if (is_load) {
            printf(
                "offset in file:    0x%x\n"
                "virt addr:         0x%lx\n"
                "phys addr:         0x%lx\n"
                "size in file:      0x%x\n"
                "size in RAM:       0x%x\n"
                "flags:             0x%x\n"
                "align constraint:  0x%x\n\n",
                (uint32_t)ph->p_offset,
                (uint64_t)ph->p_vaddr,
                (uint64_t)ph->p_paddr,
                (uint32_t)ph->p_filesz,
                (uint32_t)ph->p_memsz,
                (uint32_t)ph->p_flags,
                (uint32_t)ph->p_align);
        }
    }

    return 0;
}
