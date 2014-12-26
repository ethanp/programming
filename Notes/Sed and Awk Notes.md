latex input:		mmd-article-header
Title:		Sed & Awk Notes
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

## Sed

* [Source](http://www.thegeekstuff.com/2009/09/unix-sed-tutorial-printing-file-lines-using-address-and-patterns/)

### Overview

1. Non-interactive stream editor
2. 2 buffers: **pattern** and **hold**
3. Pseudocode
    1. Read a line from `stdin`, remove trailing newline
    3. Put line in *pattern buffer*
    4. Modify *pattern buffer* according to command
    5. Print *pattern buffer* to `stdout`

#### Initial Examples

Print 3rd line of afile

    sed -n 3p afile

Print every `N`th line starting at `M`

    sed -n 'M~N'p afile

Print lines `M` to `N` inclusive

    sed -n 'M,N'p afile

Print last line

    sed -n '$p' afile

Print `N`th through last line

    sed -n 'N,$p' afile

Print lines matching pattern `xxx`

    sed -n /xxx/p afile

Print all lines starting at one matching pattern `xxx`, up through the 7th line

    sed -n '/xxx/,7p' afile

Print lines from 3 to one matching `xxx`

    sed -n '3,/xxx/p' afile

Print lines matching `xxx` and the next 2 lines

    sed -n '/xxx/,+2p' afile

Print from where `xxx` matches to where `yyy` matches

    sed -n '/xxx/,/yyy/p' afile

#### Special Characters

1. **&** --- represents the matched text

    e.g. to replace `this` with `this and that`

        sed 's/this/& and that/g' afile

2. **Groups** --- capture by surrounding with `\(` `\)`

    e.g. change order

        sed 's/\([^0-9]+\):\([^a-z]+\)/\2:\1/' afile

#### Options (before command)

1. **-n** --- don't print input lines
2. **-e** --- allows you to supply more than one instruction on the command line
3. **-f file** --- read command from `file`

#### Flags (last part of command)

| **Flag**      | **Effect**                                            |
| ------------: | :---------------------------------------------------- |
| **g**         | replace *every* `regex` with `replacement`            |
| **`[0-9]+`**  | replace *nth* instance of `regex` with `replacement`  |
| **i**         | match case insensitively                              |
| **w file**    | write changed lines to `file`                         |
| **p**         | print data in *pattern buffer*                        |
| **q**         | quit sed after performing                             |

### Substitute Command (`s//`)

#### By Example

* [Source](http://www.thegeekstuff.com/2009/09/unix-sed-tutorial-replace-text-inside-a-file-using-substitute-command/)

Replace *all* `xxx` with `yyy` in `afile`

    sed 's/xxx/yyy/g' afile

Replace *first* `xxx` on each line with `yyy`

    sed 's/xxx/yyy/' afile

Replace *second* `xxx` on each line with `yyy`

    sed 's/xxx/yyy/2' afile

Write (only) modified lines to `afile`

    sed 's/xxx/yyy/2w bfile' afile

On lines containing `zzz`, replace all `xxx` with `yyy`

    sed '/zzz/s/xxx/yyy/g' afile

Delete last 3 chars from each line

    sed 's/...$//' afile

#### General Format

    sed '[<address>]s/<regex>/<replacement>/<flags>' filename
    sed '[<pattern>/]s/<regex>/<replacement>/<flags>' filename

Note that the *delimiter* here is `/`, but it could be one of `@%;:` instead.

### Delete command

#### By Example

Print all lines except line 3

    sed 3d afile

Print all lines except \[7,9\] (inclusive)

    sed '7,9d' afile

Print all lines that don't have `<pattern>`

    sed /<pattern>/d afile

Print all but (completely) empty lines:

    sed '/^$/d' filename.txt
    # or
    sed '/./!d' filename.txt

#### Flags

* **d** --- delete matching lines
* **!d** --- delete *non*-matching lines

### More Examples

Delete comments and empty lines

    sed -e 's/#.*//;/^$/d' afile

Print the first n (default to 25) lines

    sed ${1:-25}q

## Awk

### Basics

6. In general, we have \\(pattern \to action\\) pairs
    1. *Actions* with no associated *pattern* are executed on *every* record
    2. *Patterns* with no associated *action* are *printed* for every match
7. There's no string concatenation operator, so `"A" "B"=="AB"`
8. All numbers are represented as double-precision floats
    1. This means you can do `1/32` and you won't get `0`
    2. Use `int(number)` to truncate it to an integer
9. The arithmetic operators are as you'd expect (including `++` and `?:`),
   except that there are no bitwise operators, and both `a^b` and `a**b` mean
   `a` *to the power* `b`, though `^` is more portable

### Options

1. **-F,** --- change the **delimiter** to (e.g. *comma*)
2. **-f afile** --- read `awk` script from `afile`

### BEGIN & END

* `BEGIN` blocks (there may be multiple) are run (in given sequence) before
  `awk` starts reading input
    * You may want to set variables in here

            awk 'BEGIN { FS=":" ; OFS="**" }'
* `END` blocks are run after reading input

In general, we have

    BEGIN    { startup code }
    pattern1 { action1 }
    pattern2 { action2 }
    END      { cleanup code }

### Examples

Print first field of each line

    awk '{ print $1 }' afile

Print lines matching `pattern`

    awk '/pattern/' afile

Print first field of lines matching `pattern`

    awk '/pattern { print $1 }' afile

### Built-in Functions

1. `length(string)` --- #chars in `string`
2. String comparison operators `==`, `!=`, `<=`, etc.
3. Regex match operators
    1. `~` --- matches

            "ABC" ~ "^[A-Z]+$" #=> true
    2. `!~` --- does not match
4. `int(number)` truncates the fraction off the `number`, leaving an integer
5. `sqrt()`, `sin()`, `log()`, `exp()`

### Built-in Variables

| Variable      | Description                               |
| ------------: | :---------------------------------------  |
| `FILENAME`    | input file's name                         |
| `FNR`         | current record's number (e.g. line-no)    |
| `FS`          | field seperator (regex, default: `" "`)   |
| `NF`          | #fields in current record                 |
| `NR`          | record-number in job                      |
| `OFS`         | output field-separator (default: `" "`)   |
| `ORS`         | output record-separator (default: `"\n"`) |
| `RS`          | input record-separator (default: `"\n"`)  |
| `$1`, `$2`, ... | individual *fields* on the input line   |
| `$0`          | the *whole* line                          |
| `$NF`         | last field                                |

### Misc

1. You *could* change the environment's *field separator* `$FS` before running
   `awk`, and then you wouldn't need to set `-F`
2. You can also change the *output field seperator* `$OFS` too

        awk -F: -v 'OFS=**' '{ print $1, $5 }' /etc/passwd
