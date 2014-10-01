#include "scp.h"
#include <string.h>
#include "log.h"
#include "params.h"

// connection
const char *username = "ethanp";
const char *password = "nuh-uh";
int sock, i, auth_pw = 0;
LIBSSH2_SESSION *session;
LIBSSH2_SFTP *sftp_session;
const char *fingerprint;
struct sockaddr_in sock_in;
unsigned long hostaddr;

// file transfer
const char *newpath = "/u/ethanp/ech2";
const char *scppath = "/u/ethanp/ech";
const char *utexas_dir = "/u/ethanp/libssh_eg";
const char *local_dir = "/tmp";
const char *local_file_path = "./ech";
LIBSSH2_CHANNEL *channel;
struct stat fileinfo;
char mem[1024];
size_t nread;
FILE *local;
char *ptr;
int rc;

void get_file_stat_struct(const char *path, struct stat *buf)
{
    char remote_path[PATH_MAX];
    sprintf(remote_path, "%s/%s", utexas_dir, path);
    int res;
    LIBSSH2_SFTP_ATTRIBUTES attrs;

    res = libssh2_sftp_stat(sftp_session, remote_path, &attrs);

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
}

int scp_send(const char *path, int fd) {
    if (fd == 0) {
        fprintf(stderr, "File descriptor for %s was 0, don't do that\n", path);
        return -1;
    }
    char remote_path[PATH_MAX];

    // append "123" to make it simple to verify that it actually worked
    sprintf(remote_path, "%s/%s123", utexas_dir, path);
    fstat(fd, &fileinfo);
    printf("sending file: %s, of size %d to: %s\n",
            path, (int)fileinfo.st_size, remote_path);

    /* The mode parameter must only have permissions */
    channel = libssh2_scp_send(session, remote_path, fileinfo.st_mode & 0777,
                               (unsigned long)fileinfo.st_size);
    if (!channel) {
        char *errmsg;
        int errlen;
        int err = libssh2_session_last_error(session, &errmsg, &errlen, 0);
        fprintf(stderr, "Unable to open a session: (%d) %s\n", err, errmsg);
        scp_shutdown();
    }
    fprintf(stderr, "SCP session waiting to send file\n");
    do {
        // params: (file_descriptor, *buffer, count_bytes)
        log_msg("sending (%d) bytes\n", (int)fileinfo.st_size);
        nread = read(fd, mem, (unsigned int)fileinfo.st_size);
        if (nread <= 0) {
            /* end of file */
            break;
        }
        ptr = mem; // use separate variable that we can advance independently
        do { /* write the same data over and over, until error or completion */
            rc = libssh2_channel_write(channel, ptr, nread);
            if (rc < 0) {
                fprintf(stderr, "ERROR %d\n", rc);
                break;
            }
            else { /* rc indicates how many bytes were written this time */
                log_msg("(%d) bytes sent\n", rc);
                ptr += rc;
                nread -= rc;
            }
        } while (nread);
    } while (1);
    log_msg("File sent\n");
    libssh2_channel_send_eof(channel);
    libssh2_channel_wait_eof(channel);
    libssh2_channel_wait_closed(channel);
    libssh2_channel_free(channel);
    channel = NULL;
    return 0;
}

int scp_retrieve(const char *path, int fd) {
    char remote_path[PATH_MAX];
    sprintf(remote_path, "%s/%s", utexas_dir, path);
    off_t got = 0, file_size;

    // open channel to file to download
    channel = libssh2_scp_recv(session, remote_path, &fileinfo);
    file_size = fileinfo.st_size;
    if (!channel) {
        fprintf(stderr, "Unable to open a session: %d\n",
                libssh2_session_last_errno(session));
        char *err_msg;
        libssh2_session_last_error(session, &err_msg, NULL, 0);
        fprintf(stderr, "Error info: %s\n", err_msg);
        scp_shutdown();
    }

    log_msg("\nfile contents: ");

    // write the file to the given descriptor
    while(got < file_size) {
        char mem[1024];
        int amount = sizeof(mem);
        if ((file_size - got) < amount) {
            amount = file_size - got;
        }
        rc = libssh2_channel_read(channel, mem, amount);
        if (rc > 0) {
            write(fd, mem, rc); // params: file_descriptor, buffer, nbyte
            log_msg(mem);
            log_msg("\nchunk done\n");
        } else if (rc < 0) {
            fprintf(stderr, "libssh2_channel_read() failed: %d\n", rc);
            break;
        }
        got += rc;
    }
    log_msg("\nfile transfer done\n");
    // close the channel
    libssh2_channel_free(channel);
    log_msg("\nchannel closed\n");
    channel = NULL;
    return (int)file_size;
}

int scp_init(int argc, char *argv[]) {
    hostaddr = inet_addr("128.83.120.177");
    rc = libssh2_init(0);
    if (rc != 0) {
        fprintf (stderr, "libssh2 initialization failed (%d)\n", rc);
        return 1;
    }
    /* Ultra basic "connect to port 22 on localhost"
     * Your code must create the socket establishing the connection */
    sock = socket(AF_INET, SOCK_STREAM, 0);
    sock_in.sin_family = AF_INET;
    sock_in.sin_port = htons(22);
    sock_in.sin_addr.s_addr = hostaddr;
    if (connect(sock, (struct sockaddr*)(&sock_in),
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
            scp_shutdown();
        }
    } else {        /* Or by public key */
        if (libssh2_userauth_publickey_fromfile(session, username,
                                                "/home/ethan/.ssh/id_rsa.pub",
                                                "/home/ethan/.ssh/id_rsa",
                                                password)) {
            fprintf(stderr, "\tAuthentication by public key failed\n");
            scp_shutdown();
        }
    }
    sftp_session = libssh2_sftp_init(session);
    if (!sftp_session) {
        fprintf(stderr, "Unable to init SFTP session\n");
        scp_shutdown();
    }
    return 0;
}

 void scp_shutdown() {
    libssh2_sftp_shutdown(sftp_session);
    libssh2_session_disconnect(session, "Normal Shutdown, Thank you for playing");
    libssh2_session_free(session);
    close(sock);
    fprintf(stderr, "connection scp_shutdown\n");
    libssh2_exit();
    exit(0);
}
