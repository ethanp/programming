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

// for FUSE
#include <ctype.h>
#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <fuse.h>
#include <libgen.h>
#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include "scp.h"
#include "log.h"

// I believe this is unnecessary
// #ifdef HAVE_SYS_XATTR_H
// #include <sys/xattr.h>
// #endif

// Report errors to logfile and give -errno to caller
  static int ot_error(char *str)
  {
    // EP: I guess errno is a global defined in <errno.h>, which was #included
    int ret = -errno;

    log_msg("    ERROR %s: %s\n", str, strerror(errno));

    return ret;
}

//  All the paths I see are relative to the root of the mounted
//  filesystem.  In order to get to the underlying filesystem, I need to
//  have the mountpoint.  I'll save it away early on in main(), and then
//  whenever I need a path for something I'll call this to construct
//  it.
static void ot_fullpath(char fpath[PATH_MAX], const char *path)
{
    strcpy(fpath, OT_DATA->rootdir);

    // ridiculously long paths will break here
    strncat(fpath, path, PATH_MAX);

    log_msg("    ot_fullpath:  rootdir = \"%s\", path = \"%s\", fpath = \"%s\"\n",
       OT_DATA->rootdir, path, fpath);
}

///////////////////////////////////////////////////////////
//
// Prototypes for all these functions, and the C-style comments,
// come indirectly from /usr/include/fuse.h
//
// EP Says: "This is a good note"
//
/** Get file attributes.
 *
 * Similar to stat().  The 'st_dev' and 'st_blksize' fields are
 * ignored.  The 'st_ino' field is ignored except if the 'use_ino'
 * mount option is given.
 */
 int ot_getattr(const char *path, struct stat *statbuf)
 {
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_getattr(path=\"%s\", statbuf=0x%08x)\n",
     path, statbuf);
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

/** Create a file node
 *
 * There is no create() operation, mknod() will be called for
 * creation of all non-directory, non-symlink nodes.
 */
// shouldn't that comment be "if" there is no.... ?
 int ot_mknod(const char *path, mode_t mode, dev_t dev)
 {
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_mknod(path=\"%s\", mode=0%3o, dev=%lld)\n",
     path, mode, dev);
    ot_fullpath(fpath, path);

    // On Linux this could just be 'mknod(path, mode, rdev)' but this
    //  is more portable
    if (S_ISREG(mode)) {
        retstat = open(fpath, O_CREAT | O_EXCL | O_WRONLY, mode);
        if (retstat < 0)
           retstat = ot_error("ot_mknod open");
       else {
        retstat = close(retstat);
        if (retstat < 0)
          retstat = ot_error("ot_mknod close");
  }
} else
if (S_ISFIFO(mode)) {
   retstat = mkfifo(fpath, mode);
   if (retstat < 0)
      retstat = ot_error("ot_mknod mkfifo");
} else {
   retstat = mknod(fpath, mode, dev);
   if (retstat < 0)
      retstat = ot_error("ot_mknod mknod");
}

return retstat;
}

/** Create a directory */
int ot_mkdir(const char *path, mode_t mode)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_mkdir(path=\"%s\", mode=0%3o)\n",
       path, mode);
    ot_fullpath(fpath, path);

    retstat = mkdir(fpath, mode);
    if (retstat < 0)
       retstat = ot_error("ot_mkdir mkdir");

   return retstat;
}

/** Remove a file */
int ot_unlink(const char *path)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("ot_unlink(path=\"%s\")\n",
       path);
    ot_fullpath(fpath, path);

    retstat = unlink(fpath);
    if (retstat < 0)
       retstat = ot_error("ot_unlink unlink");

   return retstat;
}

/** Remove a directory */
int ot_rmdir(const char *path)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("ot_rmdir(path=\"%s\")\n",
       path);
    ot_fullpath(fpath, path);

    retstat = rmdir(fpath);
    if (retstat < 0)
       retstat = ot_error("ot_rmdir rmdir");

   return retstat;
}

/** Create a symbolic link */
// The parameters here are a little bit confusing, but do correspond
// to the symlink() system call.  The 'path' is where the link points,
// while the 'link' is the link itself.  So we need to leave the path
// unaltered, but insert the link into the mounted directory.
int ot_symlink(const char *path, const char *link)
{
    int retstat = 0;
    char flink[PATH_MAX];

    log_msg("\not_symlink(path=\"%s\", link=\"%s\")\n",
       path, link);
    ot_fullpath(flink, link);

    retstat = symlink(path, flink);
    if (retstat < 0)
       retstat = ot_error("ot_symlink symlink");

   return retstat;
}

/** Rename a file */
// both path and newpath are fs-relative
int ot_rename(const char *path, const char *newpath)
{
    int retstat = 0;
    char fpath[PATH_MAX];
    char fnewpath[PATH_MAX];

    log_msg("\not_rename(fpath=\"%s\", newpath=\"%s\")\n",
       path, newpath);
    ot_fullpath(fpath, path);
    ot_fullpath(fnewpath, newpath);

    retstat = rename(fpath, fnewpath);
    if (retstat < 0)
       retstat = ot_error("ot_rename rename");

   return retstat;
}

/** Create a hard link to a file */
int ot_link(const char *path, const char *newpath)
{
    int retstat = 0;
    char fpath[PATH_MAX], fnewpath[PATH_MAX];

    log_msg("\not_link(path=\"%s\", newpath=\"%s\")\n",
       path, newpath);
    ot_fullpath(fpath, path);
    ot_fullpath(fnewpath, newpath);

    retstat = link(fpath, fnewpath);
    if (retstat < 0)
       retstat = ot_error("ot_link link");

   return retstat;
}

/** Change the permission bits of a file */
int ot_chmod(const char *path, mode_t mode)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_chmod(fpath=\"%s\", mode=0%03o)\n",
       path, mode);
    ot_fullpath(fpath, path);

    retstat = chmod(fpath, mode);
    if (retstat < 0)
       retstat = ot_error("ot_chmod chmod");

   return retstat;
}

/** Change the owner and group of a file */
int ot_chown(const char *path, uid_t uid, gid_t gid)

{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_chown(path=\"%s\", uid=%d, gid=%d)\n",
       path, uid, gid);
    ot_fullpath(fpath, path);

    retstat = chown(fpath, uid, gid);
    if (retstat < 0)
       retstat = ot_error("ot_chown chown");

   return retstat;
}

/** Change the size of a file */
int ot_truncate(const char *path, off_t newsize)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_truncate(path=\"%s\", newsize=%lld)\n",
       path, newsize);
    ot_fullpath(fpath, path);

    retstat = truncate(fpath, newsize);
    if (retstat < 0)
       ot_error("ot_truncate truncate");

   return retstat;
}

/** Change the access and/or modification times of a file */
/* note -- I'll want to change this as soon as 2.6 is in debian testing */
int ot_utime(const char *path, struct utimbuf *ubuf)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_utime(path=\"%s\", ubuf=0x%08x)\n",
       path, ubuf);
    ot_fullpath(fpath, path);

    retstat = utime(fpath, ubuf);
    if (retstat < 0)
       retstat = ot_error("ot_utime utime");

   return retstat;
}

/** File open operation
 *
 * No creation, or truncation flags (O_CREAT, O_EXCL, O_TRUNC)
 * will be passed to open().  Open should check if the operation
 * is permitted for the given flags.  Optionally open may also
 * return an arbitrary filehandle in the fuse_file_info structure,
 * which will be passed to all file operations.
 *
 * Changed in version 2.2
 */
 int ot_open(const char *path, struct fuse_file_info *fi)
 {
    int retstat = 0;
    int fd;
    char fpath[PATH_MAX];

    log_msg("\not_open(path\"%s\", fi=0x%08x)\n",
       path, fi);
    ot_fullpath(fpath, path);

    // TODO first we need to get the file via SCP

    /* EP: in the end, we just call the system's open() function

        * This struct fuse_file_info thing is defined in <fuse_common.h>.
        * It maintains info about open files.
        * The fh-field is the holder for a "file-handle" (duh)
        * The flags-field is the set of flags used in the user's call to open()
    */
    fd = open(fpath, fi->flags);
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
 * substituted with zeroes.  An exception to this is when the
 * 'direct_io' mount option is specified, in which case the return
 * value of the read system call will reflect the return value of
 * this operation.
 *
 * Changed in version 2.2
 */
// I don't fully understand the documentation above -- it doesn't
// match the documentation for the read() system call which says it
// can return with anything up to the amount of data requested. nor
// with the fusexmp code which returns the amount of data also
// returned by read.
 int ot_read(const char *path, char *buf, size_t size, off_t offset, struct fuse_file_info *fi)
 {
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
 * except on error.  An exception to this is when the 'direct_io'
 * mount option is specified (see read operation).
 *
 * Changed in version 2.2
 */
// As  with read(), the documentation above is inconsistent with the
// documentation for the write() system call.
 int ot_write(const char *path, const char *buf, size_t size, off_t offset,
  struct fuse_file_info *fi)
 {
    int retstat = 0;

    log_msg("\not_write(path=\"%s\", buf=0x%08x, size=%d, offset=%lld, fi=0x%08x)\n",
       path, buf, size, offset, fi
       );
    // no need to get fpath on this one, since I work from fi->fh not the path
    log_fi(fi);

    retstat = pwrite(fi->fh, buf, size, offset);
    if (retstat < 0)
       retstat = ot_error("ot_write pwrite");

   return retstat;
}

/** Get file system statistics
 *
 * The 'f_frsize', 'f_favail', 'f_fsid' and 'f_flag' fields are ignored
 *
 * Replaced 'struct statfs' parameter with 'struct statvfs' in
 * version 2.5
 */
 int ot_statfs(const char *path, struct statvfs *statv)
 {
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_statfs(path=\"%s\", statv=0x%08x)\n",
       path, statv);
    ot_fullpath(fpath, path);

    // get stats for underlying filesystem
    retstat = statvfs(fpath, statv);
    if (retstat < 0)
       retstat = ot_error("ot_statfs statvfs");

   log_statvfs(statv);

   return retstat;
}

/** Possibly flush cached data
 *
 * BIG NOTE: This is not equivalent to fsync().  It's not a
 * request to sync dirty data.
 *
 * Flush is called on each close() of a file descriptor.  So if a
 * filesystem wants to return write errors in close() and the file
 * has cached dirty data, this is a good place to write back data
 * and return any errors.  Since many applications ignore close()
 * errors this is not always useful.
 *
 * NOTE: The flush() method may be called more than once for each
 * open().  This happens if more than one file descriptor refers
 * to an opened file due to dup(), dup2() or fork() calls.  It is
 * not possible to determine if a flush is final, so each flush
 * should be treated equally.  Multiple write-flush sequences are
 * relatively rare, so this shouldn't be a problem.
 *
 * Filesystems shouldn't assume that flush will always be called
 * after some writes, or that if will be called at all.
 *
 * Changed in version 2.2
 */
 int ot_flush(const char *path, struct fuse_file_info *fi)
 {
    int retstat = 0;

    log_msg("\not_flush(path=\"%s\", fi=0x%08x)\n", path, fi);
    // no need to get fpath on this one, since I work from fi->fh not the path
    log_fi(fi);

    return retstat;
}

/** Release an open file
 *
 * Release is called when there are no more references to an open
 * file: all file descriptors are closed and all memory mappings
 * are unmapped.
 *
 * For every open() call there will be exactly one release() call
 * with the same flags and file descriptor.  It is possible to
 * have a file opened more than once, in which case only the last
 * release will mean, that no more reads/writes will happen on the
 * file.  The return value of release is ignored.
 *
 * Changed in version 2.2
 */
 int ot_release(const char *path, struct fuse_file_info *fi)
 {
    int retstat = 0;

    log_msg("\not_release(path=\"%s\", fi=0x%08x)\n",
     path, fi);
    log_fi(fi);

    // We need to close the file.  Had we allocated any resources
    // (buffers etc) we'd need to free them here as well.
    retstat = close(fi->fh);

    return retstat;
}

/** Synchronize file contents
 *
 * If the datasync parameter is non-zero, then only the user data
 * should be flushed, not the meta data.
 *
 * Changed in version 2.2
 */
 int ot_fsync(const char *path, int datasync, struct fuse_file_info *fi)
 {
    int retstat = 0;

    log_msg("\not_fsync(path=\"%s\", datasync=%d, fi=0x%08x)\n",
       path, datasync, fi);
    log_fi(fi);

    // some unix-like systems (notably freebsd) don't have a datasync call
#ifdef HAVE_FDATASYNC
    if (datasync)
       retstat = fdatasync(fi->fh);
   else
#endif
       retstat = fsync(fi->fh);

   if (retstat < 0)
       ot_error("ot_fsync fsync");

   return retstat;
}

#ifdef HAVE_SYS_XATTR_H
/** Set extended attributes */
int ot_setxattr(const char *path, const char *name, const char *value, size_t size, int flags)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_setxattr(path=\"%s\", name=\"%s\", value=\"%s\", size=%d, flags=0x%08x)\n",
       path, name, value, size, flags);
    ot_fullpath(fpath, path);

    retstat = lsetxattr(fpath, name, value, size, flags);
    if (retstat < 0)
       retstat = ot_error("ot_setxattr lsetxattr");

   return retstat;
}

/** Get extended attributes */
int ot_getxattr(const char *path, const char *name, char *value, size_t size)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_getxattr(path = \"%s\", name = \"%s\", value = 0x%08x, size = %d)\n",
       path, name, value, size);
    ot_fullpath(fpath, path);

    retstat = lgetxattr(fpath, name, value, size);
    if (retstat < 0)
       retstat = ot_error("ot_getxattr lgetxattr");
   else
       log_msg("    value = \"%s\"\n", value);

   return retstat;
}

/** List extended attributes */
int ot_listxattr(const char *path, char *list, size_t size)
{
    int retstat = 0;
    char fpath[PATH_MAX];
    char *ptr;

    log_msg("ot_listxattr(path=\"%s\", list=0x%08x, size=%d)\n",
       path, list, size
       );
    ot_fullpath(fpath, path);

    retstat = llistxattr(fpath, list, size);
    if (retstat < 0)
       retstat = ot_error("ot_listxattr llistxattr");

   log_msg("    returned attributes (length %d):\n", retstat);
   for (ptr = list; ptr < list + retstat; ptr += strlen(ptr)+1)
       log_msg("    \"%s\"\n", ptr);

   return retstat;
}

/** Remove extended attributes */
int ot_removexattr(const char *path, const char *name)
{
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_removexattr(path=\"%s\", name=\"%s\")\n",
       path, name);
    ot_fullpath(fpath, path);

    retstat = lremovexattr(fpath, name);
    if (retstat < 0)
       retstat = ot_error("ot_removexattr lrmovexattr");

   return retstat;
}
#endif

/** Open directory
 *
 * This method should check if the open operation is permitted for
 * this  directory
 *
 * Introduced in version 2.3
 */
 int ot_opendir(const char *path, struct fuse_file_info *fi)
 {
    DIR *dp;
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_opendir(path=\"%s\", fi=0x%08x)\n",
     path, fi);
    ot_fullpath(fpath, path);

    dp = opendir(fpath);
    if (dp == NULL)
       retstat = ot_error("ot_opendir opendir");

   fi->fh = (intptr_t) dp;

   log_fi(fi);

   return retstat;
}

/** Read directory
 *
 * This supersedes the old getdir() interface.  New applications
 * should use this.
 *
 * The filesystem may choose between two modes of operation:
 *
 * 1) The readdir implementation ignores the offset parameter, and
 * passes zero to the filler function's offset.  The filler
 * function will not return '1' (unless an error happens), so the
 * whole directory is read in a single readdir operation.  This
 * works just like the old getdir() method.
 *
 * 2) The readdir implementation keeps track of the offsets of the
 * directory entries.  It uses the offset parameter and always
 * passes non-zero offset to the filler function.  When the buffer
 * is full (or an error happens) the filler function will return
 * '1'.
 *
 * Introduced in version 2.3
 */
 int ot_readdir(const char *path, void *buf, fuse_fill_dir_t filler, off_t offset,
    struct fuse_file_info *fi)
 {
    int retstat = 0;
    DIR *dp;
    struct dirent *de;

    log_msg("\not_readdir(path=\"%s\", buf=0x%08x, filler=0x%08x, offset=%lld, fi=0x%08x)\n",
       path, buf, filler, offset, fi);
    // once again, no need for fullpath -- but note that I need to cast fi->fh
    dp = (DIR *) (uintptr_t) fi->fh;

    // Every directory contains at least two entries: . and ..  If my
    // first call to the system readdir() returns NULL I've got an
    // error; near as I can tell, that's the only condition under
    // which I can get an error from readdir()
    de = readdir(dp);
    if (de == 0) {
       retstat = ot_error("ot_readdir readdir");
       return retstat;
   }

    // This will copy the entire directory into the buffer.  The loop exits
    // when either the system readdir() returns NULL, or filler()
    // returns something non-zero.  The first case just means I've
    // read the whole directory; the second means the buffer is full.
   do {
       log_msg("calling filler with name %s\n", de->d_name);
       if (filler(buf, de->d_name, NULL, 0) != 0) {
           log_msg("    ERROR ot_readdir filler:  buffer full");
           return -ENOMEM;
       }
   } while ((de = readdir(dp)) != NULL);

   log_fi(fi);

   return retstat;
}

/** Release directory
 *
 * Introduced in version 2.3
 */
 int ot_releasedir(const char *path, struct fuse_file_info *fi)
 {
    int retstat = 0;

    log_msg("\not_releasedir(path=\"%s\", fi=0x%08x)\n",
       path, fi);
    log_fi(fi);

    closedir((DIR *) (uintptr_t) fi->fh);

    return retstat;
}

/** Synchronize directory contents
 *
 * If the datasync parameter is non-zero, then only the user data
 * should be flushed, not the meta data
 *
 * Introduced in version 2.3
 */
// when exactly is this called?  when a user calls fsync and it
// happens to be a directory? ???
 int ot_fsyncdir(const char *path, int datasync, struct fuse_file_info *fi)
 {
    int retstat = 0;

    log_msg("\not_fsyncdir(path=\"%s\", datasync=%d, fi=0x%08x)\n",
       path, datasync, fi);
    log_fi(fi);

    return retstat;
}

/**
 * Initialize filesystem
 *
 * The return value will passed in the private_data field of
 * fuse_context to all file operations and as a parameter to the
 * destroy() method.
 *
 * Introduced in version 2.3
 * Changed in version 2.6
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

    //log_conn(conn);
    log_fuse_context(fuse_get_context());

    return OT_DATA;
}

/**
 * Clean up filesystem
 *
 * Called on filesystem exit.
 *
 * Introduced in version 2.3
 */
 void ot_destroy(void *userdata)
 {
    log_msg("\not_destroy(userdata=0x%08x)\n", userdata);
}

/**
 * Check file access permissions
 *
 * This will be called for the access() system call.  If the
 * 'default_permissions' mount option is given, this method is not
 * called.
 *
 * This method is not called under Linux kernel versions 2.4.x
 *
 * Introduced in version 2.5
 */
 int ot_access(const char *path, int mask)
 {
    int retstat = 0;
    char fpath[PATH_MAX];

    log_msg("\not_access(path=\"%s\", mask=0%o)\n",
       path, mask);
    ot_fullpath(fpath, path);

    retstat = access(fpath, mask);

    if (retstat < 0)
       retstat = ot_error("ot_access access");

   return retstat;
}

/**
 * Create and open a file
 *
 * If the file does not exist, first create it with the specified
 * mode, and then open it.
 *
 * If this method is not implemented or under Linux kernel
 * versions earlier than 2.6.15, the mknod() and open() methods
 * will be called instead.
 *
 * Introduced in version 2.5
 */
 int ot_create(const char *path, mode_t mode, struct fuse_file_info *fi)
 {
    int retstat = 0;
    char fpath[PATH_MAX];
    int fd;

    log_msg("\not_create(path=\"%s\", mode=0%03o, fi=0x%08x)\n",
       path, mode, fi);
    ot_fullpath(fpath, path);

    fd = creat(fpath, mode);
    if (fd < 0)
       retstat = ot_error("ot_create creat");

   fi->fh = fd;

   log_fi(fi);

   return retstat;
}

/**
 * Change the size of an open file
 *
 * This method is called instead of the truncate() method if the
 * truncation was invoked from an ftruncate() system call.
 *
 * If this method is not implemented or under Linux kernel
 * versions earlier than 2.6.15, the truncate() method will be
 * called instead.
 *
 * Introduced in version 2.5
 */
 int ot_ftruncate(const char *path, off_t offset, struct fuse_file_info *fi)
 {
    int retstat = 0;

    log_msg("\not_ftruncate(path=\"%s\", offset=%lld, fi=0x%08x)\n",
       path, offset, fi);
    log_fi(fi);

    retstat = ftruncate(fi->fh, offset);
    if (retstat < 0)
       retstat = ot_error("ot_ftruncate ftruncate");

   return retstat;
}

/**
 * Get attributes from an open file
 *
 * This method is called instead of the getattr() method if the
 * file information is available.
 *
 * Currently this is only called after the create() method if that
 * is implemented (see above).  Later it may be called for
 * invocations of fstat() too.
 *
 * Introduced in version 2.5
 */
 int ot_fgetattr(const char *path, struct stat *statbuf, struct fuse_file_info *fi)
 {
    int retstat = 0;

    log_msg("\not_fgetattr(path=\"%s\", statbuf=0x%08x, fi=0x%08x)\n",
       path, statbuf, fi);
    log_fi(fi);

    // On FreeBSD, trying to do anything with the mountpoint ends up
    // opening it, and then using the FD for an fgetattr.  So in the
    // special case of a path of "/", I need to do a getattr on the
    // underlying root directory instead of doing the fgetattr().
    if (!strcmp(path, "/"))
       return ot_getattr(path, statbuf);

   retstat = fstat(fi->fh, statbuf);
   if (retstat < 0)
       retstat = ot_error("ot_fgetattr fstat");

   log_stat(statbuf);

   return retstat;
}

/* EP: oh I see, he's not defining a struct type, he's initializing a
       struct of an existing type.

    * this thing is going to be passed to fuse_main(), to tell the fuse
      system what the names of all the functions are  */
struct fuse_operations ot_oper = {

  // these seem to make up the minimal set
  .getattr = ot_getattr,
  .open = ot_open,
  .read = ot_read,

  // these are logged but it still works without them being there
  // .readdir = ot_readdir,
  // .opendir = ot_opendir,
  // .init = ot_init,
  // .access = ot_access,
  // .releasedir = ot_releasedir,
  // .release = ot_release,


  // .write = ot_write,
  // .flush = ot_flush,
  // .fgetattr = ot_fgetattr,
  // .readlink = ot_readlink,
  // // no .getdir -- that's deprecated
  // .getdir = NULL,
  // .mknod = ot_mknod,
  // .mkdir = ot_mkdir,
  // .unlink = ot_unlink,
  // .rmdir = ot_rmdir,
  // .symlink = ot_symlink,
  // .rename = ot_rename,
  // .link = ot_link,
  // .chmod = ot_chmod,
  // .chown = ot_chown,
  // .truncate = ot_truncate,
  // .utime = ot_utime,
  // /** Just a placeholder, don't set */
  // .statfs = ot_statfs,
  // .fsync = ot_fsync,
  // .fsyncdir = ot_fsyncdir,
  // .create = ot_create,
  // .destroy = ot_destroy,
  // .ftruncate = ot_ftruncate,

// #ifdef HAVE_SYS_XATTR_H
//   .setxattr = ot_setxattr,
//   .getxattr = ot_getxattr,
//   .listxattr = ot_listxattr,
//   .removexattr = ot_removexattr,
// #endif
};

void ot_usage()
{
    fprintf(stderr, "usage:  bbfs [FUSE and mount options] rootDir mountPoint\n");
    abort();
}

int main(int argc, char *argv[])
{
    int fuse_stat;
    struct ot_state *ot_data;

    // since access checking is incomplete, it's easier to just refuse users
    // trying to run the file system as root. The somewhat smaller hole of an
    // ordinary user doing it with the allow_other flag is still there because
    // I don't want to parse the options string.
    if ((getuid() == 0) || (geteuid() == 0)) {
        fprintf(stderr, "Running as root opens unnacceptable security holes\n");
        return 1;
    }

    // establish an ssh connection with server@cs.utexas.edu
    if (scp_init(0, NULL) != 0) {
        fprintf(stderr, "scp_init failed\n");
        return 1;
    }

    // Perform some sanity checking on the command line
    if ((argc < 3) || (argv[argc-2][0] == '-') || (argv[argc-1][0] == '-'))
        ot_usage();

    ot_data = malloc(sizeof(struct ot_state));

    if (ot_data == NULL) {
        perror("main calloc");
        abort();
    }

    // TODO: this should save remote server info or something
    ot_data->rootdir = realpath(argv[argc-2], NULL);
    printf("saved rootdir: %s\n", ot_data->rootdir);

    // EP: this is required to adjust the args for the call to fuse_main
    argv[argc-2] = argv[argc-1];
    argv[--argc] = NULL;

    ot_data->logfile = log_open();

    // turn over control to fuse
    fprintf(stderr, "about to call fuse_main\n");

    // EP: returns 0 on success, nonzero on failure
    fuse_stat = fuse_main(argc, argv, &ot_oper, ot_data);
    fprintf(stderr, "fuse_main returned %d\n", fuse_stat);

    return fuse_stat;
}
