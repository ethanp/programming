Unsolved Riddles
----------------

What is the difference between `$(c)`, `<backtick>c</backtick>`, and `eval c`?

* My guess is that `$(c)` captures the *output*
  and `<backtick>c</backtick>` captures the *return value*,
  and `eval c` runs the command and doesn't capture nada.
  Surely this is wrong though.
  
### Backticks
#### 5/17/14

    $ echo "The name is `hostname`"
    The name is mymacbookinstance
    
File Descriptors
----------------
#### 3/24/14

* **0** -- `stdin`
* **1** -- `stdout`
* **2** -- `stderr`

##### Examples

    make check 2>&1 | tee make-check.log

We can think of **`2>&1`** as **"point `STDERR` to where `STDOUT` points"**

And then of course `| tee file` means "and also print it to the log-file"

Commands to Command
-------------------
## 4/11/14

### eval

(I think..., I'm on a boat) Interprets the text you give it as a bash command.

### ps -- Process Status

##### Example:

    ┌──[~]--[10:56 PM ]
    └──╼ ps
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
    > a/b

    $ dirname a/myfile
    > a

    $ dirname myfile
    > .


### basename

**given a filepath, return the part after the last slash**

This is the **opposite of [`dirname`](#dirname)**

##### Examples

    $ basename "./dir space/other dir/file.txt"
    > file.txt

    # DON'T do this by accident (viz. ALWAYS quote the filename)
    $ basename ./dir space/other dir/file.txt
    > dir
    > other
    > file.txt

### find

**recursive listing of all the files underneath given file**

##### Example

    $ find .
    > ./.DS_STORE
    > ./file.txt
    > ./a
    > ./a/anotherFile.txt
    > ./a space b
    > ./a space b/file.txt

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

