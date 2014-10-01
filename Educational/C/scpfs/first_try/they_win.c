#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>

// "huge" was created on the server’s
// file-system like this
// $ head -n 100000 /dev/urandom > huge
// $ wc -c huge
// # => 25591417 huge (25.6 MB)

int main() {
    int buf[0];
    int i;
    for (i = 0; i < 3; i++) {
        int fd = open("huge", O_RDWR);
        read(fd, buf, sizeof(int));
        lseek(fd, 0, 0);
        buf[0]++;
        write(fd, buf, 1);
        close(fd);
    }
    return 0;
}
