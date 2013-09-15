# the challenge here is to list the given files by the alphabetical ordering of
# their contents.
# e.g. "a" > g; "g" > z; "z" > a; ./sortFilesByContents.sh *
# =>    g
#       z
#       sortFilesByContents.sh
#       a

# TODO: All of it.

# grep -l == --files-with-matches
sort $* | grep -l $*
