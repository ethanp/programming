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
