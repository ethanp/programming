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
// #include <fuse.h>
#include <libgen.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include "scp.h"
#include "log.h"

// Report errors to logfile and give -errno to caller
static int ot_error(char *str)
{
    int ret = -errno; // EP: errno is a global defined in <errno.h>
    log_msg("    ERROR %s: %s\n", str, strerror(errno));
    return ret;
}

/** All the paths I see are relative to the root of the mounted filesystem. **/
static void ot_fullpath(char fpath[PATH_MAX], const char *path)
{
    strcpy(fpath, OT_DATA->rootdir);
    strncat(fpath, path, PATH_MAX); // ridiculously long paths will break here
    log_msg("    ot_fullpath:  rootdir = \"%s\", path = \"%s\", fpath = \"%s\"\n",
            OT_DATA->rootdir, path, fpath);
}

/////////////////(((( OTHER FILE SYSTEM ))))//////////////////////////////
//
// Prototypes & comments for these functions come from /usr/include/fuse.h
//
/** Get file attributes.
 * Similar to stat().  The 'st_dev' and 'st_blksize' fields are
 * ignored.  The 'st_ino' field is ignored except if the 'use_ino'
 * mount option is given.
 */
int ot_getattr(const char *path, struct stat *statbuf)
{
    int retstat = 0;
    char fpath[PATH_MAX];
    log_msg("\not_getattr(path=\"%s\", statbuf=0x%08x)\n", path, statbuf);
    ot_fullpath(fpath, path);
    retstat = lstat(fpath, statbuf);
    if (retstat != 0)
        retstat = ot_error("ot_getattr lstat");
    log_stat(statbuf);
    return retstat;
}

/** Read the target of a symbolic link
 *
 * The buffer should be filled with a null terminated string.  The
 * buffer size argument includes the space for the terminating
 * null character.  If the linkname is too long to fit in the
 * buffer, it should be truncated.  The return value should be 0
 * for success.
 */
// Note the system readlink() will truncate and lose the terminating
// null.  So, the size passed to to the system readlink() must be one
// less than the size passed to ot_readlink()
// ot_readlink() code by Bernardo F Costa (thanks!)
 int ot_readlink(const char *path, char *link, size_t size)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("ot_readlink(path=\"%s\", link=\"%s\", size=%d)\n",
            path, link, size);
    ot_fullpath(fpath, path);

    retstat = readlink(fpath, link, size - 1);
    if (retstat < 0) {
        retstat = ot_error("ot_readlink readlink");
    } else {
        link[retstat] = '\0';
        retstat = 0;
    }

    return retstat;
}

/** Remove a file ("rm") */
int ot_unlink(const char *path)
{
    int retstat = 0;
    char fpath[PATH_MAX];
    log_msg("ot_unlink(path=\"%s\")\n", path);
    ot_fullpath(fpath, path);
    retstat = unlink(fpath);
    if (retstat < 0)
        retstat = ot_error("ot_unlink unlink");
   return retstat;
}

/** Create a symbolic link */
// The parameters here are a little bit confusing, but do correspond
// to the symlink() system call.  The 'path' is where the link points,
// while the 'link' is the link itself.  So we need to leave the path
// unaltered, but insert the link into the mounted directory.
int ot_symlink(const char *path, const char *link)
{
   //  int retstat = 0;
   //  char flink[PATH_MAX];

   //  log_msg("\not_symlink(path=\"%s\", link=\"%s\")\n",
   //     path, link);
   //  ot_fullpath(flink, link);

   //  retstat = symlink(path, flink);
   //  if (retstat < 0)
   //     retstat = ot_error("ot_symlink symlink");

   // return retstat;
    return 0;
}

/** Create a hard link to a file */
int ot_link(const char *path, const char *newpath)
{
   //  int retstat = 0;
   //  char fpath[PATH_MAX], fnewpath[PATH_MAX];

   //  log_msg("\not_link(path=\"%s\", newpath=\"%s\")\n",
   //     path, newpath);
   //  ot_fullpath(fpath, path);
   //  ot_fullpath(fnewpath, newpath);

   //  retstat = link(fpath, fnewpath);
   //  if (retstat < 0)
   //     retstat = ot_error("ot_link link");

   // return retstat;
   return 0;
}

/** File open operation
 *
 * No creation, or truncation flags (O_CREAT, O_EXCL, O_TRUNC)
 * will be passed to open().  Open should check if the operation
 * is permitted for the given flags.  Optionally open may also
 * return an arbitrary filehandle in the fuse_file_info structure,
 * which will be passed to all file operations.
 */
 int ot_open(const char *path, struct fuse_file_info *fi)
 {
    /* The struct fuse_file_info thing is defined in <fuse_common.h>.
     * It maintains info about open files.
     * The fh-field is the holder for a "file-handle" (duh)
     * The flags-field is the set of flags used in the user's call to open()
     */

    int retstat = 0;
    int fd;
    int file_len;

    log_msg("\not_open(path\"%s\", fi=0x%08x)\n", path, fi);

    // retrieve the file IF it doesn't already exist
    if( access( path, F_OK ) != -1 ) { // file exists
    } else { // file doesn't exist
        fd = open(path, O_RDWR); // open in pwd (TODO move this to /tmp, I guess?)
        file_len = scp_retrieve(path, fd);
        log_msg("\nretrieved file of size (%s)\n", file_len);
    }
    fd = open(path, fi->flags);
    if (fd < 0)
       retstat = ot_error("ot_open open");

   fi->fh = fd;
   log_fi(fi);

   /* EP: will be zero if open() succeeded,
          which is different from the original open()'s functionality.

        * This makes it so that FUSE will give the caller a file descriptor of
          its own choosing, and perform the mapping using the `fi->fh = fd;`.
    */
   return retstat;
}

/** Read data from an open file
 *
 * Read should return exactly the number of bytes requested except
 * on EOF or error, otherwise the rest of the data will be
 * substituted with zeroes.
 */
// I don't fully understand the documentation above -- it doesn't
// match the documentation for the read() system call which says it
// can return with anything up to the amount of data requested. nor
// with the fusexmp code which returns the amount of data also
// returned by read.
 int ot_read(const char *path, char *buf, size_t size, off_t offset, struct fuse_file_info *fi)
 {

    // TODO read from the cached file, this means I DO need to actually use
    //                                           the `fuse_file_info` thing

    int retstat = 0;

    log_msg("\not_read(path=\"%s\", buf=0x%08x, size=%d, offset=%lld, fi=0x%08x)\n",
            path, buf, size, offset, fi);

    // no need to get fpath on this one, since I work from fi->fh not the path
    log_fi(fi);

    /* EP: pread() works just like read() but reads from the specified
     *     position in the file without modifying the file pointer.
     *
     *     The atomicity of pread enables processes or threads that share file
     *     descriptors to read from a shared file at a particular offset
     *     without using a locking mechanism that would be necessary to
     *     achieve the same result in separate lseek and read system calls
     *
     *     On success, the number of bytes read or written is returned.
     */
    retstat = pread(fi->fh, buf, size, offset);

    if (retstat < 0)
       retstat = ot_error("ot_read read");

   return retstat;
}

/** Write data to an open file
 *
 * Write should return exactly the number of bytes requested
 * except on error.
 */
// As with read(), the documentation above is inconsistent with the
// documentation for the write() system call.
int ot_write(const char *             path,
             const char *             buf,
             size_t                   size,
             off_t                    offset,
             struct fuse_file_info   *fi)
 {

    // TODO write to the locally cached file
    // no need to send it back now because that will happen on ot_flush()

    int retstat = 0;
    log_msg("\not_write(path=\"%s\", buf=0x%08x, size=%d, offset=%lld, fi=0x%08x)\n",
       path, buf, size, offset, fi);
    // no need to get fpath on this one, since I work from fi->fh not the path
    log_fi(fi);

    retstat = pwrite(fi->fh, buf, size, offset);
    if (retstat < 0)
       retstat = ot_error("ot_write pwrite");

   return retstat;
}

/** Possibly flush cached data
 *
 * Flush is called on each close() of a file descriptor.  So if a
 * filesystem wants to return write errors in close() and the file
 * has cached dirty data, this is a good place to write back data
 * and return any errors.  Since many applications ignore close()
 * errors this is not always useful.
 */
int ot_flush(const char *path, struct fuse_file_info *fi)
{
    // TODO scp_send the latest version of the file
    // but don't necessarily delete it
    // https://piazza.com/class/hz22ixrz44t64u?cid=82

    int retstat = 0;
    log_msg("\not_flush(path=\"%s\", fi=0x%08x)\n", path, fi);
    // no need to get fpath on this one, since I work from fi->fh not the path
    log_fi(fi);

    return retstat;
}

/** Release an open file
 *
 * Release is called when there are no more references to an open file.
 * For every open() call there will be exactly one release() call.
 * The return value of release is ignored.
 */
 int ot_release(const char *path, struct fuse_file_info *fi)
 {
    // TODO remove the locally cached file

    int retstat = 0;
    log_msg("\not_release(path=\"%s\", fi=0x%08x)\n", path, fi);
    log_fi(fi);
    // We need to close the file.  Had we allocated any resources
    // (buffers etc) we'd need to free them here as well.
    retstat = close(fi->fh);
    return retstat;
}

/** Read directory ("ls")
 *
 * The filesystem may choose between two modes of operation:
 *
 * 1) Implementation ignores the offset parameter, and
 * passes zero to the filler function's offset.  The filler
 * function will not return '1' (unless an error happens), so the
 * whole directory is read in a single readdir operation.
 *
 * 2) The readdir implementation keeps track of the offsets of the
 * directory entries.  It uses the offset parameter and always
 * passes non-zero offset to the filler function.  When the buffer
 * is full (or an error happens) the filler function returns '1'.
 */
int ot_readdir(const char *             path,
               void *                   buf,
               fuse_fill_dir_t          filler,
               off_t                    offset,
               struct fuse_file_info *  fi)
{
   //  int retstat = 0;
   //  DIR *dp;
   //  struct dirent *de;

   //  log_msg("\not_readdir(path=\"%s\", buf=0x%08x, filler=0x%08x, offset=%lld, fi=0x%08x)\n",
   //     path, buf, filler, offset, fi);
   //  // once again, no need for fullpath -- but note that I need to cast fi->fh
   //  dp = (DIR *) (uintptr_t) fi->fh;

   //  // Every directory contains at least two entries: . and ..  If my
   //  // first call to the system readdir() returns NULL I've got an error.
   //  de = readdir(dp);
   //  if (de == 0) {
   //     retstat = ot_error("ot_readdir readdir");
   //     return retstat;
   // }

   //  // This will copy the entire directory into the buffer.  The loop exits
   //  // when either the system readdir() returns NULL, or filler()
   //  // returns something non-zero.  The first case just means I've
   //  // read the whole directory; the second means the buffer is full.
   // do {
   //     log_msg("calling filler with name %s\n", de->d_name);
   //     if (filler(buf, de->d_name, NULL, 0) != 0) {
   //         log_msg("    ERROR ot_readdir filler:  buffer full");
   //         return -ENOMEM;
   //     }
   // } while ((de = readdir(dp)) != NULL);
   // log_fi(fi);
   // return retstat;
    return 0;
}

/**
 * Initialize filesystem
 *
 * The return value will passed in the private_data field of
 * fuse_context to all file operations and as a parameter to the
 * destroy() method.
 */
// Undocumented but extraordinarily useful fact:  the fuse_context is
// set up before this function is called, and
// fuse_get_context()->private_data returns the user_data passed to
// fuse_main().  Really seems like either it should be a third
// parameter coming in here, or else the fact should be documented
// (and this might as well return void, as it did in older versions of
// FUSE).
void *ot_init(struct fuse_conn_info *conn)
{
    log_msg("\not_init()\n");
    log_fuse_context(fuse_get_context());
    return OT_DATA;
}

/**
 * Check file access permissions
 * This will be called for the access() system call.  If the
 * 'default_permissions' mount option is given, this method is not
 * called.
 */
int ot_access(const char *path, int mask)
{
    // int retstat = 0;
    // char fpath[PATH_MAX];
    // log_msg("\not_access(path=\"%s\", mask=0%o)\n", path, mask);
    // ot_fullpath(fpath, path);
    // retstat = access(fpath, mask);
    // if (retstat < 0)
    //     retstat = ot_error("ot_access access");
    // return retstat;
    return 0;
}

/* EP: oh I see, he's not defining a struct type, he's initializing a struct of
   an existing type. Then this thing is going to be passed to fuse_main(), to
   tell the fuse system what the names of all the functions are */
struct fuse_operations ot_oper = {

    // these seem to make up the minimal set
    .getattr = ot_getattr,
    .open = ot_open,
    .read = ot_read,
    .write = ot_write,
    .release = ot_release,
    .flush = ot_flush,

    // these are optional
    .init = ot_init,

    /* these are recommended to be implemented as well
    .readdir = ot_readdir,  // for "ls"
    .unlink = ot_unlink,    // for "rm"
    */

    /* this is a pared-down list of syscalls that are probably not useful
    .opendir = ot_opendir,
    .access = ot_access,
    .releasedir = ot_releasedir,
    .readlink = ot_readlink,
    .mkdir = ot_mkdir,
    .rmdir = ot_rmdir,
    .symlink = ot_symlink,
    .link = ot_link,
    .truncate = ot_truncate,
    .create = ot_create,
    .destroy = ot_destroy,
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
    if ((argc != 2) || (argv[argc-1][0] == '-'))
        ot_usage();

    ot_data = malloc(sizeof(struct ot_state));
    if (ot_data == NULL) {
        perror("main calloc");
        abort();
    }

    ot_data->logfile = log_open();
    fprintf(stderr, "\nstarting fuse...\n\n");

    // EP: returns 0 on success, nonzero on failure
    // turn over control to fuse
    fuse_stat = fuse_main(argc, argv, &ot_oper, ot_data);
    fprintf(stderr, "fuse_main returned %d\n", fuse_stat);

    return fuse_stat;
}
