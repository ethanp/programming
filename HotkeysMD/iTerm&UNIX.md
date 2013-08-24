UNIX
====

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

## Bash

To do this Action                 | You must take these Steps
----------------------------------|--------------------------
Delete Directory $1               | `dedir $DIR`
Does Directory $DIR Have $PATTERN?| `ddh $DIR $PATTERN`
less json file $1 with formatting | `json $RAWJSON`
compile a file.c                  | `compile $FILE ($OUTPUT)?`
Untar given `.tar.*`              | `tar -zxvf FILE`
Copy `STDIN` to file AND `STDOUT` | `inputSTUFF \| tee file[s]` (`-a` to append to file[s])

### Aliases

Command                         | Name
--------------------------------|------
`cd ~`                          | `h`
`ls \| grep`                     | `lg`
`ls -l \| grep`                  | `llg`
`history \| grep`                | `hisg`
`cs ~/Dropbox/MLease`           | `mlease`
`mvim ~/.vimrc`                 | `vimrc`
`mvim ~/.bash_profile`          | `bprof`
`export PATH="${PATH}:."`       | `this`
Open with Sublime Text 2        | `sb`
`ssh charity.cs.utexas.edu`     | `ut`

