latex input:		mmd-article-header
Title:		Bash Notes
Author:		Ethan C. Petuchowski
Base Header Level:		1
latex mode:		memoir
Keywords:		Bash, Unix, Linux, Shell, Command Line, Terminal, Syntax
CSS:		http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
copyright:			2014 Ethan C. Petuchowski
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer

## Jobs
#### 11/9/14

    $ vim

    # you type
    ^z  # stop (pause) process

    [1]+ Stopped    vim

    $ jobs
    [1]+ Stopped    vim

    # go back to most recently stopped job
    $ fg
    ^z

    $ less somefile.txt
    # you type
    ^z
    [2]+ Stopped    less somefile.txt

    $ jobs
    [1]- Stopped    vim
    [2]+ Stopped    less somefile.txt

    $ fg  # back to  less
    ^z

    $ fg %1 (or) fg 1  # back to vim
    ^z

    $ kill 2
    bash: kill: (2) - No such process

    $ kill %2
    [2]- Terminated: 15   less somefile.txt

    $ fg  # now vim is queued for foreground'ing
    ^z

If you have a job that's taking too long and you want to **move it to the
background**, you can do `CTRL-Z` to `STOP` it, then do

    $ bg %{it's job_no}

and the shell will run it as a background jobs, as though you had run it with

    $ command for background execution &

## File Descriptors

#### 3/24/14

* **0** -- `stdin`
* **1** -- `stdout`
* **2** -- `stderr`

##### Examples

    make check 2>&1 | tee make-check.log

We can think of **`2>&1`** as **"point `STDERR` to where `STDOUT` points"**

And then of course `| tee file` means "and also print it to the log-file"

## State Variables

### $!
#### 9/22/14
PID of the most recent background command

    ~$ echo hello &
    [1] 3846
    hello
    ~$ echo $!
    3846
    [1]+  Done                    echo hello
    ~$

### $$
#### 9/22/14
The process ID that the (current) script file is running under

    ~$ ps
      PID TTY          TIME CMD
     3699 pts/13   00:00:00 bash
     3847 pts/13   00:00:00 ps
    ~$ echo $$
    3699
    ~$

### $?
#### 9/22/14
Most recent foreground pipeline exit status

    ~$ echo $0   # name of current shell
    -bash
    ~$ $?
    0: command not found
    ~$ echo $?
    127
    ~$

## Repeat a command *N* times

From [Server Fault][bash repeat]

There are two good options

    # cleaner syntax
    for i in {1..10}; do command; done

    # potentially more flexible
    for i in `seq 10`; do command; done

[bash repeat]: http://serverfault.com/questions/273238/how-to-run-a-command-multiple-times

# Commands to Command

## 11/10/14
### apropos --- pertaining too ...

    $ apropos database

Find all man-pages containing the word "database".

There is a **synonym**

    $ man -k database

### printf --- basically like you'd expect

    $ printf "hello %s, that's $%.2f please for %d hamburglers\n" Jann 2.32 3
    hello Jann, that's $2.32 please for 3 hamburglers

### lynx --- run the text-based browser

This is *just* too cool.

### diff --- print differences between text files

* -y --- view differences *side-by-side* (mind-blowing)
* -u --- use the +/- format (it's honestly not as nice)

### cut --- extract column of text

    cut -(b|c|f)range [optns] [files]

* -c5 --- extract the 5th character of each line
* -b3-5 --- extract the 3rd, 4th, and 5th byte of each line
* -f2,4 -d, --- extract the 2nd and 4th **fields** of each line, where a field delimiter is a comma
* by default, the *delimiter* (`-d`) is a `TAB`
* --output-delimiter=C --- when you're printing multiple fields,
*                          use this delimiter (default is `TAB`)
* -s --- suppress (don't print) lines not containing the delimiter character

### paste --- make multiple text files into a csv-type-thing

    $ cat D1
    A
    B
    C

    $ cat D2
    1
    2
    3

    $ paste D1 D2
    A   1
    B   2
    C   3

    $ paste -d, D1 D2
    A,1
    B,2
    C,3

    # this file is shorter than the others
    $ cat D3
    A
    B

    # it turns into a blank column
    $ paste -d, D3 D2
    A,1
    B,2
    ,3

    # we can transpose too!!
    $ paste -s D1 D2
    A   B   C
    1   2   3

### sort

* You can sort by columns
* You can use offsets within columns
* You can use multiple columns, each with its own offset

    $ cat D3
    B,2
    A,1
    E,4
    D,5
    C,3

    # sort by column two, with separator=,
    $ sort --key=2 --field-separator=, D3
    $ sort -k2 -t, D3
    A,1
    B,2
    C,3
    E,4
    D,5

## 11/9/14

### tail --- print the end of the file

    tail [optns] [files]

* -N --- show last N lines
* +N --- show all but first N lines
* -f --- (**watch file**) keep file open, and when new content gets appended,
         print it too (super useful)


### ln --- create a file that is a link to this file

    ln [-sif] source target

* **i** --- ask before doing anything
* **f** --- don't ask for permission
* **s** --- make a symbolic/soft link instead of a hard link

#### Hard vs. Soft Links

* **Hard** --- create a new name for a pointer to the `source`-file's
               `inode` on disk
* **Soft** --- create a new file on disk whose contents hold the
               `source`-file's *name*
    *  if the source file disappears this symbolic link will be broken

## 10/2/14

### seq -- create a sequence of numbers

    seq [first [incr]] last

* Numbers are floating point
* `first` and `incr` both default to 1
* **-s** --- set the separator
    * `$ seq -s \\t 3  =>    1\t2\t3`
* **-f** --- use printf style formatters
    * `$ seq -f %.2f -s ' ' 1 .5 3`
    * `=>  1.00 1.50 2.00 2.50 3.00`
* **-t** --- add a terminator to the sequence
* **-w** --- set width by padding with zeros

### jot --- print sequential or random data

* *Very* similar to `seq`
* **-r** --- use random data
* **-b** --- just print a given word repeatedly
* Print some random ascii (this could be improved...)
    * `jot -s '' -r -c 100 A`

## 9/28/14

### pkg-config -- determine C compiler flags

#### Examples

    $ pkg-config fuse --cflags

    -D_FILE_OFFSET_BITS=64 -D_DARWIN_USE_64_BIT_INODE -I/usr/local/Cellar/osxfuse/2.7.1/include/osxfuse/fuse

says to use pkg-config to determine what C compiler flags are necessary to
compile a source file that makes use of FUSE.

    $ pkg-config fuse --libs

    -L/usr/local/Cellar/osxfuse/2.7.1/lib -losxfuse -pthread -liconv

does the same for the libs to link with.

So you might use it in a Makefile like this

    bbfs : bbfs.o log.o
            gcc -g -o bbfs bbfs.o log.o `pkg-config fuse --libs`

    bbfs.o : bbfs.c log.h params.h
            gcc -g -Wall `pkg-config fuse --cflags` -c bbfs.c

    log.o : log.c log.h params.h
            gcc -g -Wall `pkg-config fuse --cflags` -c log.c


### tail -- last part of file

#### Options

* **-F** --- if a log file is currently getting written to, `tail` will
  keep the connection to `stdout` open so that all live input into the
  log file gets written out to the terminal

## 9/22/14

### locate -- find location of <header> file on file system

It's just like `find` only *way* faster and less thorough and has less features.
For simple things it ought to suffice.

## 7/24/14
### ack -- better than grep

Basic:

    ack pattern
    ack --[no]language pattern

Specify output format

    ack '^([^:]+)if:(.*)$' --output='$1 and $2 too!'

Options:

* **-w** --- match only whole words
* **-i** --- ignore case
* **-h** --- don't print filename
* **-l** --- only print filenames
* **-c** --- show count of matching lines in *all* files
* **-lc** --- show count of matching lines in *files with matches*
* **-C** --- specify lines of context (default: `2`)
* **--pager=LESS** --- pipe it into less (same as `ack ... | less`), except that it can be added (not sure yet if I want this) to the `~/.ackrc`
* **--[no]smart-case** --- Ignore case distinctions in `PATTERN`, only if `PATTERN` contains no upper case. Ignored if **-i** is specified.

## 6/25/14

### tr -- translate characters

**I think you *have* to pipe or carrat stuff into this thing, there's no place for an input file**

To map all uppercase characters to their corresponding lowercase variants:

	tr A-Z a-z


* **-d** --- **delete** all specified characters

		tr -d ' '   # removes all spaces

* **-c** --- set **complement**

        tr -cd ' '  # removes *everything but* the spaces

* **-s** --- **squeeze** multiple repetitions into a single instance

        tr -s ' ' '\t'  # convert spaces to tabs

* **-cs** --- convert repetitions of compliments first string into singles,
              then turn them into the second string (used by Doug McIlroy)

        tr -cs A-Za-z '\n' < /tmp/a.txt  # convert any number of non-letters into a single newline

## 6/20/14

### `$(c)` vs `backtick(c)` vs `eval c`

* `$(c)` and `backtick(c)` are (at least practically) the same, they **capture the output**.
* `eval c` **interprets the text** you give it as a bash command.

### remove duplicate lines: `uniq` and/or `sort`

	$ cat a.txt
	a
	a
	b
	b
	a
	a

	$ uniq a.txt
	a
	b
	a

	$ sort a.txt | uniq
	a
	b

	$ sort -u a.txt
	a
	b

* **`uniq -c --count`**	 --- prefix lines by the number of occurrences

        $ sort < a.txt | uniq -c
        4 a
        2 b

## 4/11/14

### ps -- Process Status

##### Example:

    [~]--[10:56 PM] ps
    PID TTY           TIME CMD
    370 ttys000    0:00.11 -bash
    2802 ttys001    0:00.01 -bash
    3199 ttys002    0:00.02 -bash
    3244 ttys002    0:00.01 man ps
    3245 ttys002    0:00.00 sh -c (cd '/usr/share/man' && (echo ".ll 9.0i"; echo ".

##### Description:

Prints information about running processes (and threads with option).

You can do things like

* List processes by memory usage
* List them by CPU usage
* List processes by other users
* List them by user

### Sort

con`cat`enate the contents of the given files, and sort that list of lines.

##### Useful-looking Options

* `-o FILE, --output=FILE` -- write to file
* `-r, --reverse` -- reverse the output
* `-f, --ignore-case` -- ignore case


## 3/20/14

### Iterate through files

    find . -name '*.csv' | while read line; do
        echo "$line"
    done

## 3/12/14

### dirname

**returns path to the input file, not including the file itself in that path**

this is the **opposite of [`basename`](#basename)**

    $ dirname a/b/myfile
    a/b

    $ dirname a/myfile
    a

    $ dirname myfile
    .


### basename

**given a filepath, return the part after the last slash**

This is the **opposite of [`dirname`](#dirname)**

    $ basename "./dir space/other dir/file.txt"
    file.txt

    # DON'T do this by accident (viz. ALWAYS quote the filename)
    $ basename ./dir space/other dir/file.txt
    dir
    other
    file.txt

You can get it to strip off the file suffix, though this doesn't seem to work
well with globbing.

    $ basename args.h .h
    args

### find

**recursive listing of all the files underneath given file**

    $ find .
    ./.DS_STORE
    ./file.txt
    ./a
    ./a/anotherFile.txt
    ./a space b
    ./a space b/file.txt

#### Useful-looking options

* `-(max|min)depth n`
* `-newer FILE` -- only files newer than `FILE`

### read

**Read user input into local variable**

[linuxcommand.org](http://linuxcommand.org/wss0110.php)

##### Example

    echo -n "Enter some text > "
    read text
    echo "You entered: $text"

    Enter some text > this is some text
    You entered: this is some text

