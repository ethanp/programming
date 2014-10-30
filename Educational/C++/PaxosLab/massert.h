#pragma once
#include <cstdio>
#include <cstdlib>

// Generic assert functionality MASSERT to avoid name clash
#ifndef NDEBUG
#   define MASSERT(condition, ...) \
    do { \
        if (! (condition)) { \
        std::fflush(stdout); \
        std::fflush(stderr); \
        std::fprintf(stderr, "Assertion `" #condition "' : %s::%d\n",   \
                     __FILE__, __LINE__);                               \
        std::fprintf(stderr, __VA_ARGS__);                              \
        std::exit(EXIT_FAILURE);                                        \
        }                                                               \
    } while (false)
#else
#   define MASSERT(condition, ...) do { } while (false)
#endif
