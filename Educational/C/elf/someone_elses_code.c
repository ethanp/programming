/* I got this from
http://stackoverflow.com/questions/16176031/opening-elf-file-and-examining */
/* I got this from
http://stackoverflow.com/questions/16176031/opening-elf-file-and-examining */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <elf.h>
#include <sys/mman.h>
#include <stddef.h>

static void shdr_parse(void *ptr)
{
    Elf64_Shdr *shdr_ptr = NULL;
    Elf64_Ehdr *Elf64_ptr = (Elf64_Ehdr *)ptr;


    int n = Elf64_ptr->e_shnum;
    char *shstrtab = NULL;

    printf("e_shstrndx: 0x%x\n", Elf64_ptr->e_shstrndx);
    printf("e_shnum: 0x%x\n", Elf64_ptr->e_shnum);

    /*
     * first of all, let's get the shstrtab, we'll get
     * the name of each section with it
     */
     shdr_ptr = (Elf64_Shdr *)(ptr + Elf64_ptr->e_shoff);
     while (n--) {
        if (shdr_ptr->sh_type == SHT_STRTAB) {
            shstrtab = (char *)(ptr + shdr_ptr->sh_offset);
            if (!strcmp(&shstrtab[shdr_ptr->sh_name], ".shstrtab"))
                break;
        }
        shdr_ptr++;
    }

    /* list all of sections */
    shdr_ptr = (Elf64_Shdr *)(ptr + Elf64_ptr->e_shoff);
    n = Elf64_ptr->e_shnum;
    int i = 0;
    while (n--) {
        printf("[%02d]%s type: 0x%x, offset: 0x%x, size: 0x%x\n", i++,
            &shstrtab[shdr_ptr->sh_name], shdr_ptr->sh_type,
            shdr_ptr->sh_offset, shdr_ptr->sh_size);
        shdr_ptr++;
    }

    printf("=== sizeof(Elf64_Sym): 0x%x\n", sizeof(Elf64_Sym));

    /* list .gnu.hash */
    shdr_ptr = (Elf64_Shdr *)(ptr + Elf64_ptr->e_shoff);
    n = Elf64_ptr->e_shnum;
    i = 0;
    while (n--) {
        if (!strcmp(&shstrtab[shdr_ptr->sh_name], ".gnu.hash")) {
            Elf64_Dyn *dyn_ptr = (Elf64_Dyn *)(ptr + shdr_ptr->sh_offset);
            printf("GNU_HASH 0x%x\n", dyn_ptr->d_un.d_ptr);
            break;
        }
        shdr_ptr++;
    }

}

static void phdr_parse(void *ptr)
{
    Elf64_Ehdr *Elf64_ptr = NULL;

    Elf64_ptr = (Elf64_Ehdr *)ptr;
    Elf64_Phdr *elf64_phdr = NULL;
    int n, found = 0;

    n = Elf64_ptr->e_phnum;
    elf64_phdr = ptr + Elf64_ptr->e_phoff;
    while (n--) {
        if (elf64_phdr->p_type == PT_DYNAMIC) {
            found = 1;
            break;
        }
        elf64_phdr++;
    }

    if (found) {
        printf("=== PT_DYNAMIC: offset: 0x%x, size: 0x%x\n",
            elf64_phdr->p_offset, elf64_phdr->p_filesz);
        printf("sizeof(Elf64_Dyn): %d\n", sizeof(Elf64_Dyn));
    }

    /* list all of dynamic sections */
    Elf64_Dyn *dyn_entry = ptr + elf64_phdr->p_offset;
    while (dyn_entry->d_tag != DT_NULL) {
        if (dyn_entry->d_tag == 0x6ffffef5) {
            printf("DT_HASH: addr: 0x%x\n", dyn_entry->d_un.d_ptr);
            break;
        }
        printf("tag: 0x%x, value: 0x%x\n", dyn_entry->d_tag, dyn_entry->d_un.d_ptr);

        dyn_entry++;
    }

}

static void relocs_parse(void *ptr)
{
    Elf64_Shdr *shdr_ptr = NULL;
    Elf64_Ehdr *Elf64_ptr = (Elf64_Ehdr *)ptr;


    int n = Elf64_ptr->e_shnum;
    char *shstrtab = NULL;

    /*
     * first of all, let's get the shstrtab, we'll get
     * the name of each section with it
     */
     shdr_ptr = (Elf64_Shdr *)(ptr + Elf64_ptr->e_shoff);
     while (n--) {
        if (shdr_ptr->sh_type == SHT_STRTAB) {
            shstrtab = (char *)(ptr + shdr_ptr->sh_offset);
            if (!strcmp(&shstrtab[shdr_ptr->sh_name], ".shstrtab"))
                break;
        }
        shdr_ptr++;
    }

    /* list all of sections */
    shdr_ptr = (Elf64_Shdr *)(ptr + Elf64_ptr->e_shoff);
    n = Elf64_ptr->e_shnum;
    int i = 0;
    while (n--) {
        if (shdr_ptr->sh_type == SHT_REL || shdr_ptr->sh_type == SHT_RELA) {
            printf("[%02d]%s type: 0x%x, offset: 0x%x, size: 0x%x\n", i++,
                &shstrtab[shdr_ptr->sh_name], shdr_ptr->sh_type,
                shdr_ptr->sh_offset, shdr_ptr->sh_size);

            int n2 = (shdr_ptr->sh_size / sizeof(Elf64_Rel));
            Elf64_Rel *rel = (Elf64_Rel *)(ptr + shdr_ptr->sh_offset);
            while (n2--) {
                printf("==== offset: 0x%x, sizeof(Elf64_Rel): 0x%x\n",
                    rel->r_offset, sizeof(Elf64_Rel));
            }
        }
        shdr_ptr++;
    }
}

static void symtab_parse(void *ptr)
{
    Elf64_Shdr *shdr_ptr = NULL;
    Elf64_Ehdr *Elf64_ptr = (Elf64_Ehdr *)ptr;


    int n = Elf64_ptr->e_shnum;
    char *shstrtab = NULL;

    /*
     * first of all, let's get the shstrtab, we'll get
     * the name of each section with it
     */
     shdr_ptr = (Elf64_Shdr *)(ptr + Elf64_ptr->e_shoff);
     while (n--) {
        if (shdr_ptr->sh_type == SHT_STRTAB) {
            shstrtab = (char *)(ptr + shdr_ptr->sh_offset);
            if (!strcmp(&shstrtab[shdr_ptr->sh_name], ".shstrtab"))
                break;
        }
        shdr_ptr++;
    }

    /* list all of sections */
    shdr_ptr = (Elf64_Shdr *)(ptr + Elf64_ptr->e_shoff);
    n = Elf64_ptr->e_shnum;
    int i = 0;
    while (n--) {
        if (shdr_ptr->sh_type == SHT_SYMTAB) {
            printf("[%02d]%s type: 0x%x, offset: 0x%x, size: 0x%x\n", i++,
                &shstrtab[shdr_ptr->sh_name], shdr_ptr->sh_type,
                shdr_ptr->sh_offset, shdr_ptr->sh_size);

            int n2 = (shdr_ptr->sh_size / sizeof(Elf64_Sym));
            Elf64_Sym *sym = (Elf64_Sym *)(ptr + shdr_ptr->sh_offset);
            while (n2--) {
                printf("== value: 0x%x\n", sym->st_value);
                sym++;
            }
        }
        shdr_ptr++;
    }
}

static void dynsym_parse(void *ptr)
{
    Elf64_Shdr *shdr_ptr = NULL;
    Elf64_Ehdr *Elf64_ptr = (Elf64_Ehdr *)ptr;


    int n = Elf64_ptr->e_shnum;
    char *shstrtab = NULL;

    /*
     * first of all, let's get the shstrtab, we'll get
     * the name of each section with it
     */
     shdr_ptr = (Elf64_Shdr *)(ptr + Elf64_ptr->e_shoff);
     while (n--) {
        if (shdr_ptr->sh_type == SHT_STRTAB) {
            shstrtab = (char *)(ptr + shdr_ptr->sh_offset);
            if (!strcmp(&shstrtab[shdr_ptr->sh_name], ".shstrtab"))
                break;
        }
        shdr_ptr++;
    }

    /* list all of sections */
    shdr_ptr = (Elf64_Shdr *)(ptr + Elf64_ptr->e_shoff);
    n = Elf64_ptr->e_shnum;
    int i = 0;
    while (n--) {
        if (shdr_ptr->sh_type == SHT_SYMTAB) {
            printf("[%02d]%s type: 0x%x, offset: 0x%x, size: 0x%x\n", i++,
                &shstrtab[shdr_ptr->sh_name], shdr_ptr->sh_type,
                shdr_ptr->sh_offset, shdr_ptr->sh_size);

            int n2 = (shdr_ptr->sh_size / sizeof(Elf64_Sym));
            Elf64_Sym *sym = (Elf64_Sym *)(ptr + shdr_ptr->sh_offset);
            while (n2--) {
                printf("== value: 0x%x\n", sym->st_value);
                sym++;
            }
        }
        shdr_ptr++;
    }
}

/*
 * display the memory postion of members of hedr struct
 */
 static void ehdr_show_addr(void *ptr)
 {
    Elf64_Ehdr *Elf64_ptr = (Elf64_Ehdr *)ptr;

    printf("&e_ident: %p\n", &Elf64_ptr->e_ident);
    printf("&e_entry: %p\n", &Elf64_ptr->e_entry);
    printf("&e_flags: %p\n", &Elf64_ptr->e_flags);

}

static void ehdr_parse(void *ptr)
{

    // Elf header
    Elf64_Ehdr *Elf64_ptr = NULL;
    Elf64_ptr = (Elf64_Ehdr *)ptr;

    // Segment/Program header
    Elf64_Phdr *elf64_phdr = NULL;
    int n;


    printf("==================== EHDR ========================\n");
    printf("e_ident: %s\n", Elf64_ptr->e_ident);
    printf("e_entry: %p\n", (void *)Elf64_ptr->e_entry);
    printf("e_phoff: %ld\n", (unsigned long)Elf64_ptr->e_phoff);
    printf("e_shoff: %ld\n", (unsigned long)Elf64_ptr->e_shoff);
    printf("e_ehsize: %d\n", Elf64_ptr->e_ehsize & 0xffff);
    printf("sizeof(Elf64_Ehdr): %d\n", sizeof(Elf64_Ehdr));
    printf("e_phentsize: %d\n", Elf64_ptr->e_phentsize);
    printf("e_phnum: %d\n", Elf64_ptr->e_phnum);
    printf("e_shentsize: %d\n", Elf64_ptr->e_shentsize);
    printf("e_shnum: %d\n", Elf64_ptr->e_shnum);
    printf("e_shstrndx: %d\n", Elf64_ptr->e_shstrndx);
    printf("==================== EHDR ========================\n");

    /* next, parse shdr */
    shdr_parse(ptr + Elf64_ptr->e_shoff);
    /* pdhr */
    n = Elf64_ptr->e_phnum;
    elf64_phdr = ptr + Elf64_ptr->e_phoff;
    while (n--)
        phdr_parse(elf64_phdr++);


}


int main(int argc, char *argv[])
{
    int fd_src;
    //size_t len = 0;
    size_t filesize = 0;
    void *ptr = NULL;   /* ptr to binary file which mapped in memory */
    if (argc != 2) {
        printf("Usage: %s [ src bin ] ...\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    /*
     * we'll calculate the file size then map to memory
     */
     fd_src = open(argv[1], O_RDONLY);
     if (fd_src < 0) {
        printf("Failed to open %s!\n", argv[1]);
        exit(EXIT_FAILURE);
    }

    /* get file size with lseek SEEK_END */
    filesize = lseek(fd_src, 0, SEEK_END);
    if (filesize < 0) {
        perror("lseek failed!");
        close(fd_src);
        exit(EXIT_FAILURE);
    }

    /* addr, length, prot, flags, fd, offset */
    ptr = mmap(0, filesize, PROT_READ | PROT_WRITE, MAP_PRIVATE, fd_src, 0);
    if (ptr < 0) {
        perror("mmap failed!");
        close(fd_src);
        exit(EXIT_FAILURE);
    }
    ehdr_parse(ptr);
    printf("================================\n");
    ehdr_show_addr(ptr);
    printf("================================\n");
    shdr_parse(ptr);
    printf("================================\n");
    phdr_parse(ptr);
    printf("================================\n");
    relocs_parse(ptr);
    printf("================================\n");
    dynsym_parse(ptr);
    printf("================================\n");

    /* do the clean work */
    munmap(ptr, filesize);
    close(fd_src);

    return EXIT_SUCCESS;
}

