/*
 * Sample showing how to do a simple SCP transfer.
 */ 
 
//#include "libssh2_config.h"
#include <libssh2.h>
 
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#ifdef HAVE_ARPA_INET_H
#include <arpa/inet.h>
#endif
#include <sys/time.h>
 
#include <sys/types.h>
#include <fcntl.h>
#include <errno.h>
#include <stdio.h>
#include <ctype.h>
 
int sock, i, auth_pw = 0;
struct sockaddr_in sin;
const char *fingerprint;
LIBSSH2_SESSION *session;
LIBSSH2_CHANNEL *channel;
unsigned long hostaddr;
const char *username = "ethanp";
const char *password = "nuh-uh";
const char *scppath = "/u/ethanp/ech";
struct stat fileinfo;
int rc;
off_t got = 0;

int get(const char *path) {
    
}

int main(int argc, char *argv[]) {
    hostaddr = inet_addr("128.83.120.177");
    if (argc > 1) { password = argv[1]; }
    if (argc > 2) { username = argv[2]; }
    if (argc > 3) { hostaddr = inet_addr(argv[3]); }
    if (argc > 4) { scppath = argv[4]; }
    rc = libssh2_init(0);
    if (rc != 0) {
        fprintf (stderr, "libssh2 initialization failed (%d)\n", rc);
        return 1;
    } 
    /* Ultra basic "connect to port 22 on localhost"
     * Your code must create the socket establishing the connection */ 
    sock = socket(AF_INET, SOCK_STREAM, 0); 
    sin.sin_family = AF_INET;
    sin.sin_port = htons(22);
    sin.sin_addr.s_addr = hostaddr;
    if (connect(sock, (struct sockaddr*)(&sin),
            sizeof(struct sockaddr_in)) != 0) {
        fprintf(stderr, "failed to connect!\n");
        return -1;
    } 
    /* Create a session instance */ 
    session = libssh2_session_init(); 
    if(!session) {
        fprintf(stderr, "couldn't create a libssh2_session\n");
        return -1;
    } 
    /* ... start it up. This will trade welcome banners, exchange keys,
     * and setup crypto, compression, and MAC layers */ 
    rc = libssh2_session_handshake(session, sock); 
    if(rc) {
        fprintf(stderr, "Failure establishing SSH session: %d\n", rc);
        return -1;
    } 
    /* At this point we havn't yet authenticated.  The first thing to do
     * is check the hostkey's fingerprint against our known hosts Your app
     * may have it hard coded, may go to a file, may present it to the
     * user, that's your call */ 
    fingerprint = libssh2_hostkey_hash(session, LIBSSH2_HOSTKEY_HASH_SHA1); 
    if (auth_pw) {  /* We could authenticate via password */ 
        if (libssh2_userauth_password(session, username, password)) { 
            fprintf(stderr, "Authentication by password failed.\n");
            goto shutdown;
        }
    } else {        /* Or by public key */ 
        if (libssh2_userauth_publickey_fromfile(session, username, 
                            "/home/ethan/.ssh/id_rsa.pub",
                            "/home/ethan/.ssh/id_rsa",
                            password)) {
            fprintf(stderr, "\tAuthentication by public key failed\n");
            goto shutdown;
        }
    } 
    /* Request a file via SCP */ 
    channel = libssh2_scp_recv(session, scppath, &fileinfo);
    if (!channel) {
        fprintf(stderr, "Unable to open a session: %d\n",
                libssh2_session_last_errno(session));
        char *err_msg;
        libssh2_session_last_error(session, &err_msg, NULL, 0);
        fprintf(stderr, "Error info: %s\n", err_msg); 
        goto shutdown;
    } 
    while(got < fileinfo.st_size) {
        char mem[1024];
        int amount=sizeof(mem); 
        if((fileinfo.st_size - got) < amount) { amount = fileinfo.st_size - got; } 
        rc = libssh2_channel_read(channel, mem, amount); 
        if(rc > 0) { write(1, mem, rc); }
        else if(rc < 0) {
            fprintf(stderr, "libssh2_channel_read() failed: %d\n", rc); 
            break;
        }
        got += rc;
    } 
    libssh2_channel_free(channel); 
    channel = NULL; 
 shutdown: 
    libssh2_session_disconnect(session, "Normal Shutdown, Thank you for playing"); 
    libssh2_session_free(session); 
    close(sock); 
    fprintf(stderr, "connection shutdown\n"); 
    libssh2_exit(); 
    return 0;
}
