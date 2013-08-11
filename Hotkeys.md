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
Ack                                 | `ga`
Add execute permissions             | `gh`
Add surrounding to this LINE        | `yss`
Add surrounding to this WORD        | `ysiw`
Insert blanks above / below         | `gio` / `go`
Change surrounding                  | `cs`
Delete surrounding                  | `ds`
Go-To beginning of line (above)     | `gk`
Go-To beginning of line (below)     | `gj`
Go-To beginning of word             | `gw`
Go-To beginning of word (strict)    | `gW`
Indentation guides                  | `gig`
Insert tab-character `\t`           | Ctrl-v + \<`TAB`\>
Reload .vimrc                       | `gvr` (doesnt work)
Open .vimrc in h-split              | `gvs`
Open .vimrc in h-split              | `gvv`
Open .vimrc in new buffer           | `gvw`
Open Command-T                      | `zc`
Open Dropbox in Command-T (nice)    | `zC`
Open TreeBranch Undo                | `gz`
Open Undo Tree                      | `gz`
Re-hardwrap paragraphs of text      | `gq`
Strip trailing whitespace           | `gl`
Switch between open tabs            | Optn + Cmd + ( left / right )
Switch buffers in current window    | F13 / F14 or "print scrn"
Toggle comments                     | *line-count* + `gci`
Print list of `marks` in all files  | `:marks`


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
`history | grep`                | `hisg`
`cs ~/Dropbox/MLease`           | `mlease`
`mvim ~/.vimrc`                 | `vimrc`
`mvim ~/.bash_profile`          | `bprof`
`export PATH="${PATH}:."`       | `this`
Open with Sublime Text 2        | `sb`
`ssh charity.cs.utexas.edu`     | `ut`
`ipython qtconsole --pylab`     | `ipy`

## Intellij

To do this Action                   | You must take these Steps
------------------------------------|--------------------------
Open *Templates* (like snippets)    | Ctrl + j
`System.out.println(\<CARET\>)`     | `sout\<TAB\>`


# UNIX Commands

To Do This                          | Do This
------------------------------------|----------
Untar given `.tar.*`                | `tar -xv FILE`
Copy `STDIN` to file AND `STDOUT`   | `inputSTUFF | tee file[s]` (`-a` to append to file[s])


# Other nice Vim stuff

#### NERDTree
To do this Action               | You must take these Steps
--------------------------------|--------------------------
Open in horizontally-split tab  | t
Open in vertically-split tab    | s

#### VimAck:
See ack.txt (Sherlock-it)


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
# TODO: The Sublime Package-Control plugins

