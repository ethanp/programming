Notes on Python
===============

Regex
-----

**Create** a regex object

    m = re.compile(r'MyRegex@\{\"\w:;\s+')

Name a **group** like so

    (?P<group_name>[^regex]+)

**Match** *starts* the match at the *beginning of the string*

    m.match(string)

**Findall** allows for *more than one* (non-overlapping) *match* in your string

    m.findall(string)

**Search** returns the *first match* it finds within the string

    m.search(string)

Decorators
----------

You wrap a burger (your function) inside a bun (the decorator)

    def outer(some_func):
        def inner():
            print "before some_func"
            ret = some_func()
            return ret + 1
        return inner

    def foo():
        return 1

    >>> decorated = outer(foo)
    >>> decorated()
    out: before some_func
    out: 2

    # same as decorated = outer(foo)
    @outer
    def foo():
        return 1


To provide more generic decorators (I don't know the details), you have

    def outer(func):
        def inner(*args, **kwargs):
            return func(*args, **kwargs)
        return inner

    @outer
    def foo():
        code_goes_here

