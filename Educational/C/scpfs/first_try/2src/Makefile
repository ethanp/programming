clean:
	rm -f hello *.gch

hello:
	gcc -Wall -o hello otherfs.c log.c scp.c -lssh2 `pkg-config fuse --cflags --libs`
