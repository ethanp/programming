#include <sys/mman.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char const *argv[])
{
    /*
       what's going on here is that I map a PAGE (even though I ask for way less
       than a page) and then I overwrite that ENTIRE page's mapping with the
       MAP_ANON (even though once again that is not at all what I asked for)
       then I segfault because the permissions have been changed from WR to
       just R
     */

    int fd = open("a.out", O_RDWR);
    char* file_backed = (char*)mmap((void*)0x800000, 300,
        PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_FIXED, fd, 0);

    char* anon = (char*)mmap((void*)0x800000, 30,
        PROT_READ, MAP_ANONYMOUS | MAP_SHARED | MAP_FIXED, 0, 0);

    int* first_200 = (int*)&anon[123];
    int* file_part = (int*)&anon[169];

    int i = 0;
    for (; i < 5; i++) {
        printf("%d: %8d, %8d\n", i, *first_200, *file_part);
        first_200++; file_part++;
    }

    *first_200 = 23; // segfault
    *file_part = 24; // segfault again
    printf("%d, %d\n", *(int*)first_200, *(int*)file_part);

    munmap(anon, 200);
    munmap(file_backed, 300);
    return 0;
}
