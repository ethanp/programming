Pathfinder
==========

This is a utility method for those times when you have a bunch of log files
that you need to collect data out of, but they don't always come in the same
directory structure. This utility will allow you to easily express the given
directory structure and return to you a list of the absolute paths of the
files in question.

#### Given this directory structure

    pwd -> upper_dir -> files a -> want it 1 --> dat data1.csv
                    \          \            \
                     \          \            > data I don't want.tsv
                      \          \
                       \          > don't want it
                        \
                         > files b -> want it 2 --> dat data2.csv
                                  \            \
                                   \            > data I don't want.tsv
                                    \
                                     > don't want it
                              
#### Behold the following command

    from pathfinder import pathfinder.pathfinder

    my_files = pathfinder('.', ['upper_dir', '*', '^want', '.tsv$='])
    
    print my_files
    
            => ['pwd/upper_dir/files 1/want it 1/dat data1.csv',
                'pwd/upper_dir/files b/want it 2/dat data2.csv']

#### Reference

Signature

    def pathfinder(cd_able, path):


* **cd_able** must be *either* a directory one may `cd` into from
  the current directory, *or* not a directory at all.
  
* **path** is the part that has the following **DSL**:
    * `*`    means any file
    * `^...` means **"starts with"**
    * `...$` means **"ends with"**
    * `...=` means **"is not a directory"**