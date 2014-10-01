#ifndef _SCP_H_
#define _SCP_H_

#include <libssh2.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <errno.h>
#include <stdio.h>
#include <ctype.h>

int scp_send(const char *path);
int scp_retrieve(const char *path, int fd);
void scp_shutdown();
int scp_init(int argc, char *argv[]);

#endif
