/*
  Copyright (C) 2012 Joseph J. Pfeiffer, Jr., Ph.D. <pfeiffer@cs.nmsu.edu>

  This program can be distributed under the terms of the GNU GPLv3.
  See the file COPYING.

  There are a couple of symbols that need to be #defined before
  #including all the headers.
*/

#ifndef _PARAMS_H_
#define _PARAMS_H_

#define FUSE_USE_VERSION 26

// need this to get pwrite().  I have to use setvbuf() instead of
// setlinebuf() later in consequence.
#define _XOPEN_SOURCE 500

// maintain file system state in here
#include <limits.h>
#include <stdio.h>

// EP: of all places, why's he defining this thing in here?
struct ot_state {
    FILE *logfile;
    char *rootdir;
};

#define OT_DATA ((struct ot_state *) fuse_get_context()->private_data)

#endif
