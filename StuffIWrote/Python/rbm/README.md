The REALLY Barebones Minco
===============================
New goal: make something useful and usable that can be iterated if need be

Current ToDos
-------------

1. Set `minco $@` as a function for `python $PATHTOPYTHONFILE $@`
   or however you capture all args

Sample Usages
-------------

```bash

    ######## IN THERE NOW #########

    # put "iOS View Controller Docs" on the Work Calendar as a 1-hour long event
    $ minco start iOS View Controller Docs

    # stops counting and adjusts Calendar event to reflect true time
    $ minco end

    # print amount of time spent so far on task
    $ minco show

    ######## EVENTUALLY #########

    # cancel current task
    $ minco cancel

    # move start time forward 15 mins
    $ minco move start 15

    # move end time backward 15 mins
    $ minco move end -15

    # list groups
    $ minco ls

    # add group iOS
    $ minco add iOS

    # delete group iOS
    $ minco delete iOS

    # print time for current task (block, day, total)
    $ minco show
```


File Layout
-----------
```
TODO
```


Futuristic Possibilities
------------------------

Use `MongoDB` to store info about commands that have been pressed and total
time spent on activities, etc., more like what the original was going to do, so
this information can then be aggregated.

