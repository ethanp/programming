#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>

// mega is ~1.1GB

int main() {
    int buf[0];
    int i;
    for (i = 0; i < 20; i++) {
        int fd = open("mega", O_RDWR);
        if (fd == -1)
            fprintf(stderr, "problem opening mega\n");
        read(fd, buf, 1);
        lseek(fd, 0, 0);
        buf[0]++;
        write(fd, buf, 1);
        fsync(fd);
        close(fd);
    }
    return 0;
}
