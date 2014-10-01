/* Based on libssh2's sample showing how to do SFTP transfers. */

#include <libssh2.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/time.h>
#include <sys/types.h>
#include <fcntl.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include "log.h"

const char *keyfile1="/home/ethan/ssh/id_rsa.pub";
const char *keyfile2="/home/ethan/.ssh/id_rsa";
const char *username="ethanp";
const char *password="nuh-uh";
const char *sftppath="/u/ethanp/ech";
LIBSSH2_SESSION *session;
LIBSSH2_SFTP *sftp_session;


// in which we fill the stat struct of sftppath using sftp
int get_file_stat_struct()
{
    int res;
    struct stat *buf = malloc(sizeof(struct stat));

    if (buf == NULL)
        fprintf(stderr, "malloc'ing struct stat returned NULL\n");

    LIBSSH2_SFTP_ATTRIBUTES attrs;
    res = libssh2_sftp_stat(sftp_session, sftppath, &attrs);
    do {
        if (res < 0 && res != LIBSSH2_ERROR_EAGAIN) {
            // there /is/ an errno in here and stuff
            fprintf(stderr, "sftp_stat failed 2\n");
            exit(1);
        }
    } while (res == LIBSSH2_ERROR_EAGAIN);

    buf->st_nlink = 1;
    buf->st_uid = attrs.uid;
    buf->st_gid = attrs.gid;
    buf->st_atime = attrs.atime;
    buf->st_mtime = attrs.mtime;
    buf->st_ctime = attrs.mtime;
    buf->st_size = attrs.filesize;
    buf->st_mode = attrs.permissions;
    return 0;
}

int main(int argc, char *argv[])
{
    unsigned long hostaddr = inet_addr("128.83.120.177");
    int sock, rc;
    struct sockaddr_in sin;
    LIBSSH2_SFTP_HANDLE *sftp_handle;
    rc = libssh2_init (0);
    if (rc != 0) {
        fprintf(stderr, "libssh2 initialization failed (%d)\n", rc);
        return 1;
    }
    sock = socket(AF_INET, SOCK_STREAM, 0);
    sin.sin_family = AF_INET;
    sin.sin_port = htons(22);
    sin.sin_addr.s_addr = hostaddr;
    if (connect(sock, (struct sockaddr*)(&sin),
                sizeof(struct sockaddr_in)) != 0) {
        fprintf(stderr, "failed to connect!\n");
        return -1;
    }
    session = libssh2_session_init();
    if (!session)
        return -1;

    /* Tell libssh2 we are blocking */
    libssh2_session_set_blocking(session, 1);

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
     * user, that's your call
     */
    libssh2_hostkey_hash(session, LIBSSH2_HOSTKEY_HASH_SHA1);
        if (libssh2_userauth_publickey_fromfile(session, username,
                                                "/home/ethan/.ssh/id_rsa.pub",
                                                "/home/ethan/.ssh/id_rsa",
                                                password)) {
            fprintf(stderr, "\tAuthentication by public key failed\n");
        goto shutdown;
    } else {
        fprintf(stderr, "\tAuthentication by public key succeeded.\n");
    }

    fprintf(stderr, "calling libssh2_sftp_init()...\n");
    sftp_session = libssh2_sftp_init(session);
    if (!sftp_session) {
        fprintf(stderr, "Unable to init SFTP session\n");
        goto shutdown;
    }
    fprintf(stderr, "calling libssh2_sftp_open()...\n");

    /* Request a file via SFTP */
    sftp_handle =
        libssh2_sftp_open(sftp_session, sftppath, LIBSSH2_FXF_READ, 0);

    get_file_stat_struct();

    if (!sftp_handle) {
        fprintf(stderr, "Unable to open file with SFTP: %ld\n",
                libssh2_sftp_last_error(sftp_session));
        goto shutdown;
    }
    fprintf(stderr, "libssh2_sftp_open() is done, now receiving data...\n");

    do {
        char mem[1024];
        fprintf(stderr, "calling libssh2_sftp_read()...\n");

        rc = libssh2_sftp_read(sftp_handle, mem, sizeof(mem));
        if (rc > 0) {
            write(1, mem, rc);
        } else {
            break;
        }
    } while (1); // continue until it fails
    libssh2_sftp_close(sftp_handle);
    libssh2_sftp_shutdown(sftp_session);

  shutdown:
    libssh2_session_disconnect(session, "Normal Shutdown, Thank you for playing");
    libssh2_session_free(session);
    close(sock);
    fprintf(stderr, "all done\n");
    libssh2_exit();
    return 0;
}
