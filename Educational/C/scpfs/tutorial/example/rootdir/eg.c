#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <assert.h>

int
main() {
   int fd0 = open("foo", O_RDWR);
   int fd1 = open("foo", O_RDONLY);
   char buf[100];
   buf[0] = 9;
   buf[1] = 81;
   buf[2] = 'A';
   buf[3] = 'q';
   buf[4] = '0';
   int nb0 = write(fd0, buf, 100);
   int nb1;
   close(fd0);
   nb1 = read(fd1, buf, 100);
   assert(buf[0] == 9);
   assert(buf[1] == 81);
   assert(buf[2] == 'A');
   assert(buf[3] == 'q');
   assert(buf[4] == '0');
   close(fd1);
   printf("Wrote %d, then %d bytes\n", nb0, nb1);
   return 0;
}


