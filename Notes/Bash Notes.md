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

#### 3/20/14

### Iterate through files

    find . -name '*.csv' | while read line; do
        echo "$line"
    done

#### 3/12/14

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

### read

**Read user input into local variable**

[linuxcommand.org](http://linuxcommand.org/wss0110.php)

##### Example

    echo -n "Enter some text > "
    read text
    echo "You entered: $text"

    Enter some text > this is some text
    You entered: this is some text

