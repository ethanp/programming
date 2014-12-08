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

## Awk

### Basics

1. The default delimiters are spaces & tabs
2. `$1`, `$2`, ... refer to the individual *fields* on the input line
3. `$0` refers to the *whole* line

### Options

1. **-F,** --- change the **delimiter** to a `,` [comma]
2. **-f afile** --- read `awk` script from `afile`

### Examples

Print first field of each line

    awk '{ print $1 }' afile

Print lines matching `pattern`

    awk '/pattern/' afile

Print first field of lines matching `pattern`

    awk '/pattern { print $1 }' afile


