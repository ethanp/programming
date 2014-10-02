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

# Z-Shell

## Refs

1. [https://wiki.archlinux.org/index.php/zsh]()
2. [https://github.com/robbyrussell/oh-my-zsh](), [http://ohmyz.sh/]()
3. [http://zanshin.net/2013/09/03/how-to-use-homebrew-zsh-instead-of-max-os-x-default/]()
4. [http://www.iterm2.com/#/section/features/configurability]()
5. [http://mikebuss.com/2014/02/02/a-beautiful-productive-terminal-experience/]()
6. [http://mikegrouchy.com/blog/2012/01/zsh-is-your-friend.html]()
7. [http://stevelosh.com/blog/2010/02/my-extravagant-zsh-prompt/]()
8. [http://www.paradox.io/posts/9-my-new-zsh-prompt]()

## Intro

### Things to figure out

1. The prompt
2. The coloring
3. Vim mode
4. Git stuff
5. Autocompletion
6. Easy `ls`ing to common locations    

# Sed & Awk

Sed
---

### Delete matching lines from file

[Source](http://en.kioskea.net/faq/1451-sed-delete-one-or-more-lines-from-a-file)

    sed '{[/]<n>|<string>|<regex>[/]}d' <fileName>
    sed '{[/]<addr1>[,<addr2>][/]d' <fileName>

* `/.../` = delimiters
* `n` = line number
* `string` = string found in in line
* `regex` = regular expression corresponding to the searched pattern
* `addr` = address of a line (number or pattern)
* `d` = delete
* `!d` = delete anything that *doesn't match*

#### This is the command I *wanted*

Replace `file1` with only the lines that *don't* contain `dlm`

    sed '/dlm/d' file1 > tmp; cat tmp > file1; rm -f tmp

#### These are the given examples

* These examples are just printed (stdout1= screen).
* Using, the `-i"tmpfile"` option, you can save the original version to `tmpfile` as a backup

        sed -i".bak" '3d' filename.txt

Remove the 3rd line:

    sed '3d' fileName.txt


Remove the line containing the string "awk":

    sed '/awk/d' filename.txt


Remove the last line:

    sed '$d' filename.txt



Remove all empty lines:

    sed '/^$/d' filename.txt
    sed '/./!d' filename.txt


Remove the line matching by a regular expression (by eliminating one containing digital characters, at least 1 digit, located at the end of the line):

    sed '/[0-9/][0-9]*$/d' filename.txt


Remove the interval between lines 7 and 9:

    sed '7,9d' filename.txt


The same operation as above but replacing the address with parameters:

    sed '/-Start/,/-End/d' filename.txt


# Other

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

says to use pkg-config to determine what C compiler flags are necessary to compile a source file that makes use of FUSE.

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

##### Examples

    $ dirname a/b/myfile
    a/b

    $ dirname a/myfile
    a

    $ dirname myfile
    .


### basename

**given a filepath, return the part after the last slash**

This is the **opposite of [`dirname`](#dirname)**

##### Examples

    $ basename "./dir space/other dir/file.txt"
    file.txt

    # DON'T do this by accident (viz. ALWAYS quote the filename)
    $ basename ./dir space/other dir/file.txt
    dir
    other
    file.txt

### find

**recursive listing of all the files underneath given file**

##### Example

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

