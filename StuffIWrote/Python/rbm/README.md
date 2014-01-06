The REALLY Barebones Minco
===============================
Goal: make something useful and usable that can be iterated if need be

Usage Note
----------
**You *must* leave the event you want to `end` in the calendar**
after you `start` it.

Sample Usages
-------------

In There Now

```bash

    # put "iOS View Controller Docs" on the Work Calendar as a 1-hour long event
    $ minco start iOS View Controller Docs

    # print time AND adjust Calendar event to show now as the end time
    # of the most recent task
    $ minco end

    # print amount of time spent so far on task
    $ minco show
```

Would be quite simple to add if desirable

```bash

    # move start time forward 15 mins
    $ minco move start 15

    # move end time backward 15 mins
    $ minco move end -15
```


File Layout
-----------
```
Task_1 Name
Task_1 Start Time
Task_2 Name
Task_2 Start Time
...
```

