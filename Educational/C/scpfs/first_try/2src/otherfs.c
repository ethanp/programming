/*
  "otherfs" is a modification by Ethan Petuchowski of the following
  tutorial file system:

  Big Brother File System
  Copyright (C) 2012 Joseph J. Pfeiffer, Jr., Ph.D. <pfeiffer@cs.nmsu.edu>

  This program can be distributed under the terms of the GNU GPLv3.
  See the file COPYING.

  This code is derived from function prototypes found /usr/include/fuse/fuse.h
  Copyright (C) 2001-2007  Miklos Szeredi <miklos@szeredi.hu>
  His code is licensed under the LGPLv2.
  A copy of that code is included in the file fuse.h

  gcc -Wall -o hello otherfs.c log.c scp.c -lssh2 `pkg-config fuse --cflags --libs`
*/

#include "params.h"
#include <ctype.h>
#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <libgen.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include "scp.h"
#include "log.h"

char *hostdir = "/home/ethan/Desktop/2lab/first_try/hostdir";

// Report errors to logfile and give -errno to caller
static int ot_error(char *str)
{
    int ret = -errno; // EP: errno is a global defined in <errno.h>
    log_msg("    ERROR %s: %s\n", str, strerror(errno));
    return ret;
}

/////////////////(((( OTHER FILE SYSTEM ))))//////////////////////////////
//
// Prototypes & comments for these functions come from /usr/include/fuse.h
//
/** Get file attributes. Similar to stat(). */
int ot_getattr(const char *path, struct stat *statbuf)
{
    char local_path[PATH_MAX];
    sprintf(local_path, "%s%s", hostdir, path);
    int retstat = 0;
    log_msg("\not_getattr(path=\"%s\", statbuf=0x%08x)\n", path, statbuf);

    // if file is not cached, download the stat info using sftp
    if (access(local_path, F_OK) == -1) {
        log_msg("\nfile doesn't exist\n");
        get_file_stat_struct(path, statbuf);
        if (retstat != 0)
            retstat = ot_error("ot_getattr sftp_stat");
    }

    // if it is cached, just call lstat() on the the cached file
    else {
        log_msg("\nfile exists\n");
        retstat = lstat(local_path, statbuf);
        if (retstat != 0)
            retstat = ot_error("ot_getattr lstat");
    }
    log_stat(statbuf);

    return retstat;
}

/** File open operation */
 int ot_open(const char *path, struct fuse_file_info *fi)
 {
    /* The struct fuse_file_info thing is defined in <fuse_common.h>.
     * It maintains info about open files.
     * The fh-field is the holder for a "file-handle"
     */

    int fd, retstat;
    char local_path[PATH_MAX];
    sprintf(local_path, "%s%s", hostdir, path);

    log_msg("\not_open(path\"%s\", fi=0x%08x)\n", path, fi);

    // retrieve the file IF it doesn't already exist
    if(access(local_path, F_OK) == -1) { // file doesn't exist
        fd = open(local_path, O_RDWR | O_CREAT);
        if (fd < 0) {
            log_msg("\nfile can't be opened to download into: %s\n", strerror(errno));
        } else {
            log_msg("file (%d) open to download into\n", fd);
        }
        log_msg("downloading file to %s\n", local_path);
        scp_retrieve(path, fd);
        log_msg("retrieved file\n");
        retstat = fchmod(fd, 0777);
        close(fd);
        if (retstat < 0)
            retstat = ot_error("ot_open chmod");
        fd = open(local_path, O_RDWR);
        if (fd < 0)
            log_msg("preliminary open failed: %s\n", strerror(errno));
        close(fd);
    } else {
        log_msg("\nfile seems to exist\n");
    }

    // open the cached file and set the externally-available file descriptor
    fd = open(local_path, fi->flags);
    if (fd < 0) {
        log_msg("\nopen failed: %s\n", strerror(errno));
    }
    fi->fh = fd;
    log_fi(fi);

    // not returning zero leads to problems
    return 0;
}

/** Read data from an open file
 * Read should return exactly the number of bytes requested */
 int ot_read(const char *path, char *buf, size_t size, off_t offset, struct fuse_file_info *fi)
 {
    int retstat = 0;
    log_msg("\not_read(path=\"%s\", buf=0x%08x, size=%d, offset=%lld, fi=0x%08x)\n",
            path, buf, size, offset, fi);
    log_fi(fi);

    // we know the file is cached (it was opened) so just read it
    retstat = pread(fi->fh, buf, size, offset);
    if (retstat < 0)
        retstat = ot_error("ot_read read");
    return retstat;
}

/** Write data to an open file
 * Write should return exactly the number of bytes requested except on error.
 */
int ot_write(const char *             path,
             const char *             buf,
             size_t                   size,
             off_t                    offset,
             struct fuse_file_info   *fi)
 {
    int retstat = 0;
    log_msg("\not_write(path=\"%s\", buf=0x%08x, size=%d, offset=%lld, fi=0x%08x)\n",
            path, buf, size, offset, fi);
    log_fi(fi);
    retstat = pwrite(fi->fh, buf, size, offset);
    if (retstat < 0)
        retstat = ot_error("ot_write pwrite");
    return retstat;
}

/** Possibly flush cached data */
int ot_flush(const char *path, struct fuse_file_info *fi)
{
    // don't need to do anything here, see
    // https://piazza.com/class/hz22ixrz44t64u?cid=82
    int retstat = 0;
    log_msg("\not_flush(path=\"%s\", fi=0x%08x)\n", path, fi);
    log_fi(fi);
    return retstat;
}

/** Release an open file
 * Release is called when there are no more references to an open file.
 * For every open() call there will be exactly one release() call.
 */
 int ot_release(const char *path, struct fuse_file_info *fi)
 {
    log_msg("\not_release(path=\"%s\", fi=0x%08x)\n", path, fi);
    log_fi(fi);
    // send the most current version back to the server
    // I can't go ahead and remove the file, unless I keep track
    // externally of whether anyone else is using it...which I'm not.
    scp_send(path, fi->fh);
    log_msg("back in ot_release after sending file\n");
    close(fi->fh);
    return 0;
}

/**
 * Called when fuse initializes the filesystem
 * The return value will passed in the private_data field of
 * fuse_context to all file operations and as a parameter to the
 * destroy() method.
 */
void *ot_init(struct fuse_conn_info *conn)
{
    log_msg("\not_init()\n");
    log_fuse_context(fuse_get_context());
    return OT_DATA;
}

/* EP: We're not defining a struct type, we're initializing a struct of
   an existing type. Then this thing is going to be passed to fuse_main(), to
   tell the fuse system what the names of all the functions are */
struct fuse_operations ot_oper = {

    // these seem to make up the minimal set
    .getattr = ot_getattr,
    .open = ot_open,
    .read = ot_read,
    .write = ot_write,
    .release = ot_release,
    .init = ot_init,

    // my implementations doesn't do anything useful right now
    .flush = ot_flush,

    /* these are recommended to be implemented as well
    .readdir = ot_readdir,  // for "ls"
    .unlink = ot_unlink,    // for "rm"
    */
};

void ot_usage()
{
    fprintf(stderr, "otfs always mounts to ethanp@almond-joy.cs.utexas.edu:libssh_eg\n");
    fprintf(stderr, "it will mount to your specified local mountPoint\n");
    fprintf(stderr, "usage:  otfs mountPoint\n");
    abort();
}

int main(int argc, char *argv[])
{
    int fuse_stat;
    struct ot_state *ot_data;

    // refuse users trying to run the file system as root.
    if ((getuid() == 0) || (geteuid() == 0)) {
        fprintf(stderr, "Running as root opens unnacceptable security holes\n");
        return 1;
    }

    // establish an ssh connection with ethanp@almond-joy.cs.utexas.edu
    if (scp_init(0, NULL) != 0) {
        fprintf(stderr, "scp_init failed\n");
        return 1;
    } else {
        fprintf(stderr, "\nconnected to UTexas\n");
    }

    // disallow mount options
    if ((argc < 2) || (argv[argc-1][0] == '-'))
        ot_usage();

    ot_data = malloc(sizeof(struct ot_state));
    if (ot_data == NULL) {
        perror("main calloc");
        abort();
    }

    // set the cache directory for files downloaded from the network
    ot_data->rootdir = hostdir;

    ot_data->logfile = log_open();
    fprintf(stderr, "\nstarting fuse...\n\n");

    // turn over control to fuse
    fuse_stat = fuse_main(argc, argv, &ot_oper, ot_data);
    fprintf(stderr, "fuse_main returned %d\n", fuse_stat);
    return fuse_stat;
}
