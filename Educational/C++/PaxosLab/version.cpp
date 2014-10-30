#include "version.h"
// Defined by Makefile
#ifndef GIT_SHA
#define GIT_SHA "0000000000000000000000000000000000000000"
#endif
#ifndef LINK_TIME
#define LINK_TIME "no time for link"
#endif

const char link_time[] = LINK_TIME;
const char build_git_sha[] = GIT_SHA;
