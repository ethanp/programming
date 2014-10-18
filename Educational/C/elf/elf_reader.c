#include <elf.h>
#include <stdio.h>
#include <sys/stat.h> 
#include <sys/types.h> 
#include <fcntl.h>
#include <errno.h>
#include <stdlib.h>

/* ssize_t read(int fd, void *buf, size_t num_bytes) */
/* ssize_t pread(int fd, void *buf, size_t nbytes, off_t offset) */
#include <unistd.h> 

int main(int argc, const char *argv[]) {

    const char *name_to_open;
    int fd, ret;
    off_t filesize = 0;
    struct stat filestat;
    Elf64_Ehdr elf_header; /* in <elf.h> */

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
        perror("open");
        exit(EXIT_FAILURE);
    }

    // get and print filesize
    ret = stat(name_to_open, &filestat);
    if (ret == -1) {
        perror("stat");
        exit(EXIT_FAILURE);
    }
    filesize = filestat.st_size;
    printf("filesize: %jd\n", (intmax_t)filesize);

    /* I'm doing this wrong. I should be reading it all into memory,
       then I can just index into the locations I want to access etc,
       and not have to worry about preads or segfaults or mallocs etc. */

    // read elf header
    ret = pread(fd, &elf_header, sizeof(Elf64_Ehdr), 0);
    if (ret == -1) {
        perror("pread elf_header");
        exit(EXIT_FAILURE);
    } else if (ret < sizeof(Elf64_Ehdr)) {
        fprintf(stderr, "couldn't read ELF header for some reason\n");
        exit(EXIT_FAILURE);
    }

    /* I'm doing this wrong. One doesn't just go to the Section Header /Table/
       offset and and expect to get a Shdr. Once I have this thing read
       into RAM, what I have to do is... TODO ... loop through each of the
       sections and /each/ of those is an individual "Section Header". */

    // pread section header
    int sechdr_offset = elf_header.e_shoff;
    Elf64_Shdr section_header;
    ret = pread(fd, &section_header, sizeof(Elf64_Shdr), sechdr_offset);
    if (ret == -1) {
        perror("pread section header");
        exit(EXIT_FAILURE);
    } else if (ret < sizeof(Elf64_Shdr)) {
        fprintf(stderr, "couldn't read section header for some reason\n");
        exit(EXIT_FAILURE);
    }

    return 0;
}
