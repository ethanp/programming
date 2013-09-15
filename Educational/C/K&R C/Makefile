
#############################
# This file was based upon the Learn C the Hard Way Ch:28 Makefile for building a Library
# 	The proplem is, I don't even know what a library is, so that makefile isn't helpful
# 	The url for that chapter is http://c.learncodethehardway.org/book/ex28.html
# 
# What this is supposed to do, is
# 	Take all the *.c files in your ./src dir
# 	Compile them into *.o files to put in the ./obj dir
# 	Compile them into * files to put in the ./bin dir
# 
# But the problem is,
# 	I can't really see when that'd be useful, 
# 		so I'm just going to leave this unfinished for now
# 		and if it turns out it'd be helpful,
# 		I'll finish it
#
# The good news is,
# 	I did learn a great deal about 
# 		how Makefiles work
# 		what they do and
# 		how to use them effectively
#
# 
# I know that there is a TON more stuff one Could learn about 
# 	using them even More effectively to do 
# 	awesome and easily configurable/extensible things,
# 
# but I don't know enough Use Cases yet,
# 	so I don't even know what things would be useful to make!
#
##################
# To use this, put all *.c files you want to compile into a ./src/ dir


# Option info from http://gcc.gnu.org/onlinedocs/gcc/Option-Summary.html
# "The flags you'll pretty much always want to use"
# -g 		=> Produce debugging system that GDB can work with
# -c 		=> Compile but do not link
# -o File 	=> Compile, link, and store in File
# -O2 		=> Level 2 compiler optimization
# -Wall 	=> Enable warnings of 'questionable constructions' (error-prone functions)
# -Wextra 	=> Extra warnings not enabled by -Wall
# -Isrc 	=> This is I<folder>, but it "includes" the folder
# -rdynamic 	=> Allows obtaining bcaktraces from within a program (???, just gonna leave it there)
# -DNEBUG 	=> Got rid of it because I can't find any documentation about it
# $(OPTFLAGS) 	=> E.g. make OPTFLAGS=-m32 |=> adds this flag to your run
CFLAGS=-O2 -Wall -Wextra -Isrc -rdynamic $(OPTFLAGS)

# Dynamically create $SOURCES var by doing a wildcard-based search of the src dir and one-dir-down
SOURCES=$(wildcard src/*.c)

# Use the $SOURCES list of *.c files and make a New list of Object files by changing the extentions
OBJECTS=$(patsubst %.c,%.o,$(SOURCES))


# Strip extentions to name Bin files
BINS=$(patsubst src/%.c,%,$(SOURCES))

all: $(BINS)

ex1-13: src/ex1-13.c
	gcc $(CFLAGS) -o ex1-13 src/ex1-13.c
	
ex1-12: src/ex1-12.c
	gcc $(CFLAGS) -o ex1-12 src/ex1-12.c

