Hotkeys
======

## Global

To do this Action                       | You must take these Steps
----------------------------------------|-----------------------------------
Center window                           | Ctrl-Alt-Home
Move window                             | Ctrl-Alt-Arrow keys
Open iTerm2 Visor                       | ctrl + opt + cmd + space
Render markdown                         | drag *.md to `Marked` app in dock
Resize window                           | Ctrl + Alt + shift + Arrow keys
Show hidden files in Open/Save dialogs  | Cmd + Shift + .


## MacVim

To do this Action                   | You must take these Steps
------------------------------------|--------------------------
Ack                                 | g a
Add execute permissions             | g h
Add surrounding to this LINE        | y s s
Add surrounding to this WORD        | y s i w
Insert blanks above / below         | g i o / g o
Change surrounding                  | c s
Delete surrounding                  | d s
Go-To beginning of line (above)     | g k
Go-To beginning of line (below)     | g j
Go-To beginning of word             | g w
Go-To beginning of word (strict)    | g W
Indentation guides                  | g i g
Insert tab-character `\t`           | Ctrl-v + \<TAB\>
Reload .vimrc                       | g v r (doesnt work)
Open .vimrc in h-split              | g v s
Open .vimrc in h-split              | g v v
Open .vimrc in new buffer           | g v w
Open Command-T                      | z c
Open Dropbox in Command-T (nice)    | z C
Open TreeBranch Undo                | g z
Open Undo Tree                      | g z
Re-hardwrap paragraphs of text      | g q
Strip trailing whitespace           | g l
Switch between open tabs            | Optn + Cmd + ( left / right )
Switch buffers in current window    | F13 / F14 or "print scrn"
Toggle comments                     | *line-count* + g c i


## iTerm

To do this Action               | You must take these Steps
--------------------------------|--------------------------
Autocomplete from scrollback    | Cmd + -
Copy text from shell            | Select it with the mouse
Drag and drop selected text     | Cmd + Drag + Drop
Instant replay                  | Cmd + Alt + B
iTerm2 Exposé                   | Cmd + Alt + E
Kill text from here to EOL      | Ctrl + k
Maximize current pane / Revert  | Shift + Cmd + Enter
Move between panes              | Cmd + ] / Cmd + Alt + Arrow
Open file                       | Cmd + Click on filename
Paste history                   | Cmd + Shift + H
Paste text from clipboard       | middle-click
Select rectangle                | Cmd + Alt + Drag
Split shell vertically          | Cmd + d



## Sublime
To do this Action                   | You must take these Steps
------------------------------------|--------------------------
Align selected lines                | Ctrl + Cmd + a



## Bash

To do this Action                 | You must take these Steps
----------------------------------|--------------------------
Delete Directory $1               | `dedir $DIR`
Does Directory $1 Have grep $2?   | `ddh $DIR $PATTERN`
less json file $1 with formatting | `json $RAWJSON`
compile a file.c                  | `compile $FILE ($OUTPUT)?`

### Aliases

Command                         | Name
--------------------------------|------
`cd ~`                          | `h`
`ls | grep`                     | `lg`
`ls -l | grep`                  | `llg`
`history | grep`                | `hg`
`cs ~/Dropbox/MLease`           | `mlease`
`mvim ~/.vimrc`                 | `vimrc`
`mvim ~/.bash_profile`          | `bprof`
`export PATH="${PATH}:."`       | `this`
Open with Sublime Text 2        | `sb`
`ssh charity.cs.utexas.edu`     | `ut`

## Intellij

To do this Action                   | You must take these Steps
------------------------------------|--------------------------
Open *Templates* (like snippets)    | Ctrl + j
`System.out.println(\<CARET\>)`     | `sout\<TAB\>`

## Other nice Vim stuff

#### NERDTree
To do this Action               | You must take these Steps
--------------------------------|--------------------------
Open in horizontally-split tab  | t
Open in vertically-split tab    | s



##### Good SNIPPETS:

**IN a C file:**

prefix      | result
------------|--
`def`       |  define
`do`        |  do/while
`enum`      |  enum declaration
`for`       |  really nice!
`fund`      |  function declaration
`if`        |  if
`inc`       |  include <>
`Inc`       |  include ""
`main`      |  main
`pr`        |  printf
`td`        |  type definition
`tds`       |  struct declaration
`wh`        |  while

**IN a PYTHON file:**

prefix          |  result
----------------|--
`cl`            |  class declaration
`def`           |  function
`deff`          |  simpler version
`for`           |  for
`imp`           |  import
`property`      |  property
`try`           |  buncha options given
`wh`            |  while


# TODO: The other vim plugins I have
