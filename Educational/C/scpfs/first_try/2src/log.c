/* Copyright (C) 2012 Joseph J. Pfeiffer, Jr., Ph.D. <pfeiffer@cs.nmsu.edu>
   This program can be distributed under the terms of the GNU GPLv3.
   See the file COPYING. */

#include "params.h"

#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include <sys/types.h>
#include <sys/stat.h>

#include "log.h"

FILE *log_open()
{
    FILE *logfile;

    // very first thing, open up the logfile and mark that we got in
    // here.  If we can't open the logfile, we're dead.
    logfile = fopen("otfs.log", "w");
    if (logfile == NULL) {
        perror("logfile");
        exit(EXIT_FAILURE);
    }

    // set logfile to line buffering
    setvbuf(logfile, NULL, _IOLBF, 0);

    return logfile;
}

void log_msg(const char *format, ...)
{
    va_list ap;
    va_start(ap, format);

    vfprintf(OT_DATA->logfile, format, ap);
}

void log_fuse_context(struct fuse_context *context)
{
    log_msg("    context:\n");

    /** Pointer to the fuse object */
    //	struct fuse *fuse;
    log_struct(context, fuse, %08x, );

    /** User ID of the calling process */
    //	uid_t uid;
    log_struct(context, uid, %d, );

    /** Group ID of the calling process */
    //	gid_t gid;
    log_struct(context, gid, %d, );

    /** Thread ID of the calling process */
    //	pid_t pid;
    log_struct(context, pid, %d, );

    /** Private filesystem data */
    //	void *private_data;
    log_struct(context, private_data, %08x, );
    log_struct(((struct ot_state *)context->private_data), logfile, %08x, );
    log_struct(((struct ot_state *)context->private_data), rootdir, %s, );

    /** Umask of the calling process (introduced in version 2.8) */
    //	mode_t umask;
    log_struct(context, umask, %05o, );
}

// This dumps all the information in a struct fuse_file_info.  The struct
// definition, and comments, come from /usr/include/fuse/fuse_common.h
void log_fi (struct fuse_file_info *fi)
{
    log_msg("    fuse_file_info:\n");

    /** Open flags.  Available in open() and release() */
    //	int flags;
    log_struct(fi, flags, 0x%08x, );

    /** Old file handle, don't use */
    //	unsigned long fh_old;
    // log_struct(fi, fh_old, 0x%08lx,  );

    /** In case of a write operation indicates if this was caused by a
        writepage */
    //	int writepage;
    // log_struct(fi, writepage, %d, );

    /** Can be filled in by open, to use direct I/O on this file.
        Introduced in version 2.4 */
    //	unsigned int keep_cache : 1;
    // log_struct(fi, direct_io, %d, );

    /** Can be filled in by open, to indicate, that cached file data
        need not be invalidated.  Introduced in version 2.4 */
    //	unsigned int flush : 1;
    // log_struct(fi, keep_cache, %d, );

    /** Padding.  Do not use*/
    //	unsigned int padding : 29;

    /** File handle.  May be filled in by filesystem in open().
        Available in all other file operations */
    //	uint64_t fh;
    log_struct(fi, fh, 0x%016llx,  );

    /** Lock owner id.  Available in locking operations and flush */
    //  uint64_t lock_owner;
    // log_struct(fi, lock_owner, 0x%016llx, );
};

// This dumps the info from a struct stat.  The struct is defined in
// <bits/stat.h>; this is indirectly included from <fcntl.h>
void log_stat(struct stat *si)
{
    log_msg("    struct stat's info:\n");

    //  dev_t     st_dev;     /* ID of device containing file */
    log_struct(si, st_dev, %lld, );

    //  ino_t     st_ino;     /* inode number */
    log_struct(si, st_ino, %lld, );

    //  mode_t    st_mode;    /* protection */
    log_struct(si, st_mode, 0%o, );

    //  nlink_t   st_nlink;   /* number of hard links */
    log_struct(si, st_nlink, %d, );

    //  uid_t     st_uid;     /* user ID of owner */
    log_struct(si, st_uid, %d, );

    //  gid_t     st_gid;     /* group ID of owner */
    log_struct(si, st_gid, %d, );

    //  dev_t     st_rdev;    /* device ID (if special file) */
    log_struct(si, st_rdev, %lld,  );

    //  off_t     st_size;    /* total size, in bytes */
    log_struct(si, st_size, %lld,  );

    //  blksize_t st_blksize; /* blocksize for filesystem I/O */
    log_struct(si, st_blksize, %ld,  );

    //  blkcnt_t  st_blocks;  /* number of blocks allocated */
    log_struct(si, st_blocks, %lld,  );

    //  time_t    st_atime;   /* time of last access */
    log_struct(si, st_atime, 0x%08lx, );

    //  time_t    st_mtime;   /* time of last modification */
    log_struct(si, st_mtime, 0x%08lx, );

    //  time_t    st_ctime;   /* time of last status change */
    log_struct(si, st_ctime, 0x%08lx, );

}
