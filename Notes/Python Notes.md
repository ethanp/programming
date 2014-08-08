latex input:        mmd-article-header
Title:              Python Notes
Author:             Ethan C. Petuchowski
Base Header Level:  1
latex mode:         memoir
Keywords:           Programming language, Python
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
Copyright:          2014 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

## Data libraries

### File I/O

    df = pd.read_csv('data/train.csv', header=0)
    
### Quick column-by-name access

    df['colName']
    df.colName

### Wrangling strategies

#### Assign mode value to all rows with null value

We use `values` because `mode()` returns a `pd.Series` and we want a `np.array`

    df.Embarked[ df.Embarked.isnull() ] = df.Embarked.dropna().mode().values
    
##### A more complex example using a different syntax:

In all locations where the column `Fare` is null `and` the `Pclass` equals `f+1`,
set the value in the `Fare` column to the `median_fare[f]` calculated before.

    df.loc[ (df.Fare.isnull()) & (df.Pclass == f+1 ), 'Fare'] = median_fare[f]
    
#### Convert Categorical variable to a set of dummies/indicators

First we create a dataframe of dummy variable columns for each of the values found in the Categorical variable

    dummies = pd.get_dummies(df.Categ_Column)

Then we append those to the original dataframe

    df = pd.concat([df, dummies], axis=1)
    
Then we drop the original categorical variable and one of the dummies (because having *all* of them is redundant)

    df = df.drop(['one_of_the_vars', 'Embarked'], axis=1)
    
##### All in one go it would be

    df = pd.concat([df,pd.get_dummies(df.Embarked)], axis=1).drop(['one_of_the_vars', 'Embarked'], axis=1)
    
### Convert dataframe to numpy array
sklearn expects a `numpy array`, not a `dataframe`

    np_arr = df.values

    logit = LogisticRegression(penalty='l2')
    X = np_arr[:,1:]
    y = np_arr[:,0]
    logit = logit.fit(X,y)
    predictions = logit.predict(test_arr).astype(int)
    
### Exploratory Data Analysis

    df.groupby(['col']).mean().plot()
    
    df.describe()      # print summary stats of *only those cols for which this is available*
    df.head(n=5)       # print first 'n' rows
    df.count()         # count of non-nulls
    df.mean()          # probably DON'T want to do this, not sure how it treats NaNs
    df.dropna().mean() #  \-> use this instead
    df.columns         # np.array of unicode column names
    df.Column.value_counts() # Series showing counts for each value in Column
    df.Column.value_counts().hist() # histogram of the count info for Column
    

## `vars(thing)`: Turn `thing` into an equivalent dictionary
#### 6/29/14
    
    >>> class A(object):
    ...    def __init__(self):
    ...       self.a = 123
    ...       self.b = 436
    ...

    >>> vars(a)
    {'a': 123, 'b': 436}

    >>> a.__dict__          # maybe this is the same thing
    {'a': 123, 'b': 436}
    
### This gives you a cheap implementation of object equivalence

This is my own invention, so maybe it's a Bad Idea for some reason.

    >>> class A(object):
    ...    def __init__(self):
    ...       self.a = 123
    ...       self.b = 436
    ...    def __eq__(self, other):
    ...       return vars(self) == vars(other)

## Append to file Real Quick
#### 6/28/14

To append to a file real quick, use

    print >> file_handle, 'msg'     # 2.x only
    

To do breakneck speed logging while running a `nose unittest`, use

    logfile = open('/tmp/test.log', 'a')
    print >> logfile, 'the thing to write out'
    logfile.close()

Though what you're *supposed* to do is use the `logging` framework

## unittest
#### 6/3/14

Python's **built-in xUnit framework**

    import unittest
    
    class TestMyClass(unittest.TestCase):
    
        def setUp(self):
            setup_code()
    
        def test_my_test_case(self):
            something = use_code_under_test()
            self.assertFalse(not something, 'returned None instead of something')
            self.assertEqual(something, 'that thing my code returns', 'my code did not return what it's supposed to')
    
    if __name__ == '__main__'
        unittest.main()

## Python 3


### The "//" operator

Always does *"integer division"*, even on floats (returns floored float value)

    >>> 4 / 3
    1.3333333333333333
    >>> 4 // 3
    1
    >>> 4.0 / 3
    1.3333333333333333
    >>> 4.0 // 3
    1.0

## Global variables

#### 5/5/14

* [SO source](http://stackoverflow.com/questions/423379)

If you are just ***referencing*** a global variable, *you don't need to do
anything special* to access it.

	globvar = 0
	def print_globvar():
	    print globvar  # this is OK

However, to ***assign to*** a global variable, you **must declare it
as `global` first**

	globvar = 0	
	def set_globvar_to_one():
	    global globvar # Needed to modify global copy of globvar
	    globvar = 1

## next(`iterator`)

#### 4/16/14

* **return** the next element of the iterator
* If there are no more elements, raises a `StopIteration`

**Note:** a **`list`** is **not** an **`iterator`**

    # Can't use a list!
    >>> r = range(2)
    >>> next(r)
    TypeError: list object is not an iterator

    # Can cast list to iterator
    >>> r = iter(range(2))
    >>> next(r)
    0
    >>> next(r)
    1
    >>> next(r)
    StopIteration

    # I told you once before, you can't use a list!
    >>> next([i for i in range(2)])
    TypeError: list object is not an iterator

    # Can use a generator
    >>> next(i for i in range(2))
    0

    # Doesn't reuse the old generator (it'd be real weird if it didn't!)
    >>> next(i for i in range(2))
    0

    # Create a generator
    >>> a = (i for i in range(2))
    >>> next(a)
    0
    # Stored generator does get iterated through (as expected)
    >>> next(a)
    1
    >>> next(a)
    StopIteration

## Filter and Map

**Stuff the iterable in the second parameter
through the function in the first parameter.**

#### 4/15/14

    >>> a = [1,2,3,4,5,6]

    # don't try to pretend this is Scala
    >>> a.map(lambda x: x > 3)
    Error: 'list' object has no attribute 'map'

    # it does still work if you do it right
    >>> filter(lambda x: x > 3, a)
    [4, 5, 6]
    >>> map(lambda x: x > 3, a)
    [False, False, False, True, True, True]


## Sorting in-place & out-of-body

Sort the `list` in-place

    myList.sort()

Create a new list out of sorted values of given list

    sorted(myList)

Justifying Text
---------------

    >>> print '  TITLE  '.center(20, '-')
    -----  TITLE  ------

    >>> print '  TITLE  '.ljust(20, '-')
      TITLE  -----------

    >>> print '  TITLE  '.rjust(20, '-')
    -----------  TITLE

Raw Strings
-----------

Usually patterns will be expressed in Python code using this raw string notation.

* `r"\n"` is a **two-character string** containing `'\'` and `'n'`
* `"\n"` is a **one-character string** containing a *newline*.

Regex
-----

`string`s have several methods for performing operations with fixed strings and they’re usually much faster.

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

`(?P<name>...)` defines a named group, and `(?P=name)` is a back-reference to a named group.

**`{m,n}`** causes the resulting RE to match from m to n repetitions of the preceding RE, attempting to match as few repetitions as possible. This is the non-greedy version of the previous qualifier. For example, on the 6-character string 'aaaaaa', a{3,5} will match 5 'a' characters, while a{3,5}? will only match 3 characters.

**`(?#...)`** -- A comment; the contents of the parentheses are simply ignored.

**`\b`** only matches the empty string (i.e. doesn't consume any
characters) only at the beginning or end of the word

**`\B`** matches empties strings, only *not* at the beginning or
end of the word

**`\s`** matches any *whitespace* character

**`\w`** matches any *alphanumeric* character

**`\W`** matches any *non alphanumeric* character

#### Using compile()

the sequence

    prog = re.compile(pattern)
    result = prog.match(string)

is equivalent to

    result = re.match(pattern, string)

but using `re.compile()` saves the resulting regular expression object
for reuse

#### Maketrans

The same as `Unix`'s `tr` command

    >>> from string import maketrans   # Required to call maketrans function.
    >>> print "this is example, wow!".translate(maketrans("aeiou", "12345"))
    th3s 3s 2x1mpl2, w4w!

#### match() versus search()

The `match()` function only checks if the RE matches at the beginning of
the string while `search()` will scan forward through the string for a
match. Otw they are the same.


### Examples

Find all repeated words

    >>> p = re.compile(r'(\b\w+)\s+\1')
    >>> p.search('Paris in the the spring').group()
    'the the'

Groups can be nested

    >>> p = re.compile('(a(b)c)d')
    >>> m = p.match('abcd')
    >>> m.group(0)
    'abcd'
    >>> m.group(1)
    'abc'
    >>> m.group(2)
    'b'
    >>> m.groups()
    ('abc', 'b')
    >>> m.group(2,1,2)
    ('b', 'abc', 'b')

You can use Named Groups

    >>> p = re.compile(r'(?P<word>\b\w+\b)')
    >>> m = p.search( '(((( Lots of punctuation )))' )
    >>> m.group('word')
    'Lots'

Match a filename of the form `.*[.].*$` excluding `*.bat` files

* Naive (aka horrendous)

        .*[.]([^b].?.?|.[^a]?.?|..?[^t]?)$

* Improved (using *negative lookahead*)

        .*[.](?!bat$).*$

    * Excluding another filename extension is now easy; simply add it as an
      alternative inside the assertion. The following pattern excludes
      filenames that end in *either* `bat` *or* `exe`:

            .*[.](?!bat$|exe$).*$

Checkout the `re.split()` function

    >>> re.split('[\W]+', 'Words, words, words.', 1)
    ['Words', 'words, words.']

If capturing parentheses are used in the RE, their values are also returned
as part of the list

    >>> p = re.split(r'\W+', 'This... is a test.')
    ['This', 'is', 'a', 'test', '']

    >>> p2 = re.split(r'(\W+)', 'This... is a test.')
    ['This', '... ', 'is', ' ', 'a', ' ', 'test', '.', '']

Substitution

    >>> p = re.compile( '(blue|white|red)')
    >>> p.sub( 'colour', 'blue socks and red shoes', count=1)
    'colour socks and red shoes'

    # subn return a tuple with the count
    >>> p.subn( 'colour', 'blue socks and red s    hoes')
    ('colour socks and colour shoes', 2)

Referring to *named groups*

    >>> p = re.compile('section{ (?P<name> [^}]* ) }', re.VERBOSE)
    >>> p.sub(r'subsection{\1}','section{First}')
    'subsection{First}'
    >>> p.sub(r'subsection{\g<1>}','section{First}')
    'subsection{First}'
    >>> p.sub(r'subsection{\g<name>}','section{First}')
    'subsection{First}'

A `?` after a quantifier (incl. `{m,n}`) makes it *non-greedy*

    >>> print re.match('<.*>', '<html><head><title>Title</title>').group()
    <html><head><title>Title</title>

    >>> print re.match('<.*?>', '<html><head><title>Title</title>').group()
    <html>


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

