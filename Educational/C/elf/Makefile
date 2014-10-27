e: clean
	gcc elf_reader.c -g -o e -static
	./e

moved: clean
	gcc -g -Wl,\
	--section-start=.data=0xac1060,\
	--section-start=.bss=0xac2c40,\
	--section-start=.text=0x800360 \
	-static elf_reader.c -o apager
	./apager /home/ethan/Desktop/3lab/e_copy

debug: clean
	gcc -g -Wl,\
	--section-start=.data=0xac1060,\
	--section-start=.bss=0xac2c40,\
	--section-start=.text=0x800360 \
	-static elf_reader.c -o apager
	gdb \
	-ex "b main" \
	-ex "b 256" \
	-ex "r /home/ethan/Desktop/3lab/e_copy" \
	-ex "run" \
	./apager

sec: clean e
	gcc -static someone_elses_code.c -o sec
	./sec e

clean:
	rm -f e sec moved_e apager

output.assembly:
	gcc -S assmb.c
