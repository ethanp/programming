## Language

### Linux syscalls

##### 9/28/14

#### pread

    #include <unistd.h>
    ssize_t pread(int fd, void *buf, size_t count, off_t offset);
    
Reads up to `count` bytes from file descriptor `fd` at offset `offset` (from the start of the file) into the buffer starting at `buf`. The file offset (held by the OS) is not changed. On success, the number of bytes read or written is returned, or -1 on error, in which case errno is set to indicate the error.

The atomicity of pread enables processes or threads that share file
descriptors to read from a shared file at a particular offset
without using a locking mechanism that would be necessary to
achieve the same result in separate lseek and read system calls.

#### fopen

    #include <stdio.h>
    FILE *fopen(const char *path, const char *mode);

Opens the file whose name is the string pointed to by path and associates a *stream* with it.

Modes

1. **r** --- read
2. **r+** --- read and write, starting at beginning of file
3. **w** --- erase file and enter write mode
4. **w+** --- read and write, clear file, start at beginning
5. **a** --- append, start at end of file
6. **a+** --- read and append, read from start, write from end

##### Differences from open()

1. `open()` is Unix-specific, while `fopen()` is ANSI C
2. `fopen()` is *buffered*, meaning it's *faster*, though you have to
   call `fclose()` and `fflush()` at the appropriate times

### Types

##### 5/19/14

#### size_t

* `unsigned integer` type
* in `<stddef.h>` and `<stdlib.h>`
* Used to represent the size of an object
* It is a type guaranteed to be able to hold any (in-memory) array index
* It is the type returned by `sizeof` and `strlen()`

### Operators

##### 4/6/14

#### Comma Operator

[Wikipedia: Comma Operator](http://en.wikipedia.org/wiki/Comma_operator)

* Evaluates its first operand and discards the result
* Then evaluates the second operand and returns this value (and type).

**Precedence:** *follows* assignment

    i = (a, b);  // stores b into i 
    i = a, b;    // stores a into i. Equivalent to (i = a), b;
                    
### Escape Sequences

##### 4/6/14

I'm having a hard time finding good documentation of these things.
By experiment here is what I know:

* What does `puts("\e[2J\e[H");` do?
   * **puts**: print string followed by newline (see <a href=#puts>link to `puts`</a>)
   * \e[2J: clears the screen by inserting enough newlines that the old content is off the screen
   * \e[H:  in this case, it changes the clearing of the screen by `e[2J` so that it only involves
            writing one newline and scrolling the screen itself so the old content is no longer
            visible

### stdio.h

##### 4/6/14

#### puts

    int puts(const char *str)

* **does:** prints the given `string`, followed by a `newline`
* **returns:** nonnegative value if successful, `EOF` on error

##### 3/19/14

#### memset

    void *memset(void *str, int c, size_t n)

* **does:** fills `n` bytes of `str` with `n` `(unsigned char)c`s
* **returns:** `str` (possibly so that you can chain calls to different functions together)

### MACROS

#### Variable stringification in macro

[Reference (GNU)](https://gcc.gnu.org/onlinedocs/cpp/Stringification.html)

    //  macro to log fields in structs.
    #define log_struct(st, field, format, typecast) \
      log_msg("    " #field " = " #format "\n", typecast st->field)
      
* When a macro parameter is used with a leading ‘#’, the preprocessor
  replaces it with the literal text of the actual argument, converted
  to a string constant.
* So you write a series of adjacent string constants and "stringified"
  arguments, the preprocessor will replace the stringified arguments
  with string constants. Then the C compiler will combine all the
  adjacent string constants into one long string.

#### Newline in macro

##### 4/29/14

Use the `\` character, but you need a space before it unless you want there to
be no space between the letters

	a\
	b  // => ab
	
	a \
	b 	// => a b

#### Macro that does nothing

##### 3/15/14

To define a macro that does nothing:

    #define TAKES_A_PARAMETER_AND_THEN_TURNS_TO_THIN_AIR(X)

#### Using do {} while(0) for great scott

##### 3/15/14

[Reference: SO](http://stackoverflow.com/questions/1067226/c-multi-line-macro-do-while0-vs-scope-block)

If you want to use a macro that calls multiple function within an `if` statement,
you'll be in for a bit of a surprise.

We define the naive

    #define CALL_FUNCS(x) \
    func1(x); \
    func2(x); \
    func3(x);

Then we call it with

    if (<condition>)
        CALL_FUNCS(a);
    else
        bar(a);

But that gets expanded to the following **problematic code**

    if (<condition>)
        func1(x);
    func2(x);
    func3(x);

    // NO! this else now doesn't belong to anything!
    else
        bar(a);

So you need to put the func's in some sort of block, but you want to make sure that
it gets executed exactly once. This is precicely what `do {...} while (0)`
accomplishes.

So we instead define

    #define CALL_FUNCS(x) \
    do { \
    func1(x); \
    func2(x); \
    func3(x); \
    } while (0)

Which will be expanded to

    if (<condition>)
        do {
            func1(x);
            func2(x);
            func3(x);
        } while (0);

    else
        bar(a);

which works as we expect!

#### Pass operator into macro

##### 5/29/14

I found this in Apple's [RosyWriter][] example app. The macro itself
doesn't look useful (at all), but the fact that you pass a `<` into
it is pretty cool.

###### Definition

	#define CMTIME_COMPARE_INLINE(time1, comparator, time2) ((Boolean)(CMTimeCompare(time1, time2) comparator 0))
	
###### Usage

	while( CMTIME_COMPARE_INLINE( [[previousSecondTimestamps objectAtIndex:0] CMTimeValue], <, oneSecondAgo ) )
		[previousSecondTimestamps removeObjectAtIndex:0];	


[RosyWriter]: https://developer.apple.com/library/ios/samplecode/RosyWriter/Introduction/Intro.html

### string.h

##### 3/26/14

#### strchr

    char *strchr(char *str, int character)

* **Returns** a pointer to the first occurrence of `character` in `str`
    * Returns `NULL` if `character` not in `str`

##### 2/27/14

#### strcpy

    char *strcpy(char *dest, char *src)

* **Returns** `dest`
* Also copies null-terminator.

#### strncpy

        char *strncpy(char *dest, char *src, size_t count)

* Only copies `count` characters
    * `if count ≤ strlen(src):` `dest` will not have a null-terminator
    * `else:` Will be padded with nulls
* **Returns** `dest`

### stdio.h

    int sprintf(char *buffer, char *format, ...)

* Basically, it calls `printf` on the `format` argument, but instead of writing the output to `stdout`, it writes it into the `buffer` argument
* **Returns** the number of characters written, or a negative value if there was an error

## Concepts

### Pointer vs Array

##### 8/24/14

This is copied directly from [StOve][SO PvA].

It's a subtle difference. Essentially, the former:

    char amessage[] = "now is the time";

Defines an array whose members live in the current scope's stack
space, whereas:

    char *pmessage = "now is the time";

Defines a pointer that lives in the current scope's stack space,
but that references memory elsewhere (in this one, "now is the
time" is stored elsewhere in memory, commonly a string table).

Also, note that because the data belonging to the second definition
(the explicit pointer) is not stored in the current scope's stack
space, it is unspecified exactly where it will be stored and should
not be modified. *(I.e. beware the segfault, I guess.)*

Edit: As pointed out by Mark, GMan, and Pavel, there is also a
difference when the address-of operator is used on either of these
variables. For instance, &pmessage returns a pointer of type char\*\*,
or a pointer to a pointer to chars, whereas &amessage returns a
pointer of type char(\*)[16], or a pointer to an array of 16 chars
(which, like a char\*\*, needs to be dereferenced twice as litb
points out).

[SO PvA]: http://stackoverflow.com/questions/1335786

## Misc
* In C, function names are pointers to functions
    * So if you need to pass a pointer to a function, you can just pass a function name (idnat special?)
