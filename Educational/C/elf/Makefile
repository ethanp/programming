e: clean
	gcc elf_reader.c -g -o e -static
	./e

moved: clean
	gcc -Wl,\
	--section-start=.data=0x00000000006f0060 \
	-static hello.c -o moved_hello

sec: clean e
	gcc -static someone_elses_code.c -o sec
	./sec e

clean:
	rm -f e sec

output.assembly:
	gcc -S assmb.c
