e: clean
	gcc elf_reader.c -g -o e -static
	./e

moved: clean
	gcc -g -Wl,\
	--section-start=.data=0xac1060,\
	--section-start=.bss=0xac2c40,\
	--section-start=.text=0x800360 \
	-static elf_reader.c -o apager
	./apager /home/ethan/Desktop/3lab/st_hello

dyn: clean
	gcc -g -Wl,\
	--section-start=.data=0xac1060,\
	--section-start=.bss=0xac2c40,\
	--section-start=.text=0x800360 \
	-static elf_reader.c -o dpager
	./dpager /home/ethan/Desktop/3lab/st_hello

debug: clean
	gcc -g -Wl,\
	--section-start=.data=0xac1060,\
	--section-start=.bss=0xac2c40,\
	--section-start=.text=0x800360 \
	-static elf_reader.c -o apager
	gdb \
	-ex "b main" \
	-ex "b 174" \
	-ex "r /home/ethan/Desktop/3lab/st_hello" \
	-ex "run" \
	./apager

sec: clean e
	gcc -static someone_elses_code.c -o sec
	./sec e

clean:
	rm -f e
	rm -f sec
	rm -f moved_e
	rm -f apager

output.assembly:
	gcc -S assmb.c

hello:
	rm -f st_hello
	gcc -g basics/hello.c -o st_hello -static
