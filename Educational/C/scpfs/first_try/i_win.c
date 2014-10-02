#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>

// "number" was created on the
// server’s file-system using
// $ echo '0' > number
// $ wc -c number
// # => 2 number (2 Bytes)

int main() {
    int buf[0];
    int i;
    int fd = open("number", O_RDWR);
    for (i = 0; i < 1E4; i++) {
        read(fd, buf, 1);
        lseek(fd, 0, 0);
        buf[0]++;
        write(fd, buf, 1);
        fsync(fd);
    }
    close(fd);
    return 0;
}
