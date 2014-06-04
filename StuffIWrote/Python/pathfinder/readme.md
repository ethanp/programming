Pathfinder
==========

This is a utility method for those times when you have a bunch of log files
that you need to collect data out of, but they don't always come in the same
directory structure. Or there is a bunch of logged data files and you only want
the ones with a particular filename prefix/suffix/type. **This utility will
allow you to easily and readably express the given directory structure and return to you a
list of the absolute paths of *only* the files you want.**

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

    from pathfinder import pathfinder

    my_files = pathfinder('.', ['upper_dir', '*', '^want', '.csv$='])

    print my_files

            => ['/Absolute...Path/pwd/upper_dir/files 1/want it 1/dat data1.csv',
                '/Absolute...Path/pwd/upper_dir/files b/want it 2/dat data2.csv']

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
    * `$` must precede `=` if both are being used as directives
    * There is no escape if you want the directives as literals

### TODO
What'd be cool is like an XPath `\\search` thing where it searches
arbitrarily nested dirs for the first thing that matches after.
I'd say `**` would be cool for that.