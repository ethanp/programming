// all programs using FUSE must include the following (5) headers:
#include <sys/types.h> /* various datatypes (they all end in _t) */
#include <sys/stat.h> /* stat functions return info about files (not used?) */
#include <fcntl.h> /* file control: using `open()` */
#include <unistd.h> /* POSIX API: open, close, read, write */
#include <stdio.h> /* file I/O: printf */

#include <assert.h> /* assert */

int main()
{
    // open file as read-write
    // and open it as read-only
    int fd0 = open("foo", O_RDWR);
    int fd1 = open("foo", O_RDONLY);

    // create a buffer with some chars
    char buf[100];
    buf[0] = 9;
    buf[1] = 81;
    buf[2] = 'A';
    buf[3] = 'q';
    buf[4] = '0';

    // write the buffer to the read-write version
    // and close the file
    int nb0 = write(fd0, buf, 100);
    close(fd0);

    // read the read-only version and verify that
    // the char buffer's contents are actually there
    int nb1;
    nb1 = read(fd1, buf, 100);
    assert(buf[0] == 9);
    assert(buf[1] == 81);
    assert(buf[2] == 'A');
    assert(buf[3] == 'q');
    assert(buf[4] == '0');
    close(fd1);

    printf("Wrote %d, then read %d bytes\n", nb0, nb1);
    return 0;
}

