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

/* this comes from the `man 2 mmap` page */
#define print_error(msg) \
    do { perror(msg); exit(EXIT_FAILURE); } while (0)

/* I believe since this is a macro it will work an varargs too */
#define print_failure(msg) \
    do { fprintf(stderr, msg); exit(EXIT_FAILURE); } while (0)

int main(int argc, const char *argv[])
{
    const char *name_to_open;
    int fd, ret;
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

    /* TODO mmap(the whole thing) */
    /* void *mmap(
            void*   addr,
            size_t  len,
            int     prot,
            int     flags,
            int     fildes,
            off_t   off); */
    char* addr;
    addr = mmap(
        NULL,           /* don't care where it goes in VAS */
        filesize,       /* len */
        PROT_READ,      /* pages may be read */
        MAP_PRIVATE,    /* copy on write (it shouldn't matter bc it's prot_read */
        fd,             /* the file opened above */
        0               /* start at the very beginning */
    );

    if (addr == -1) {
        print_error("mmap");
    }

    /* ssize_t pread(int fd, void *buf, size_t nbytes, off_t offset) */

    /* cast to the elf header */
    hdr_p = (Elf64_Ehdr *)addr;
    assert(hdr_p->e_version == EV_CURRENT);

    // ret = pread(fd, &hdr_p, sizeof(Elf64_Ehdr), 0);
    // if (ret == -1) {
    //     print_error("pread hdr_p");
    // } else if (ret < sizeof(Elf64_Ehdr)) {
    //     print_failure("couldn't read ELF header for some reason\n");
    // }

    /* I'm doing this wrong. One doesn't just go to the Section Header TABLE
       offset and and expect to get a Shdr.

       TODO ... loop through each of the sections and /each/ of those is an
       individual "Section Header". */

    /* pread section header */
    // int sechdr_offset = hdr_p.e_shoff;
    // Elf64_Shdr section_header;
    // ret = pread(fd, &section_header, sizeof(Elf64_Shdr), sechdr_offset);
    // if (ret == -1) {
    //     print_error("pread section header");
    // } else if (ret < sizeof(Elf64_Shdr)) {
    //     print_failure("couldn't read section header for some reason\n");
    // }

    return 0;
}
