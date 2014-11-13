latex input:    mmd-article-header
Title:          Git Notes
Author:         Ethan C. Petuchowski
Base Header Level:  1
latex mode:     memoir
Keywords:       Git, Version Control, Command Line, Terminal, Syntax
CSS:            http://fletcherpenney.net/css/document.css
xhtml header:   <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
copyright:      2014 Ethan C. Petuchowski
latex input:    mmd-natbib-plain
latex input:    mmd-article-begin-doc
latex footer:   mmd-memoir-footer

Many of these notes are from O'Reilly's *Git Pocket Guide,* by Richard E. Silverman.

## Basic Commands
#### 11/12/14

### Show current branch

    $ git branch
    * master        # for example

### Checkout a tagged commit

    $ git checkout mytag
    You are in 'detached HEAD' state...

### Show `diff`erence between your working tree and the *index* (staging area)

    $ git diff

### Show the `diff`erence between your *index* (staging area) and the most recent ("current") commit

    $ git diff --staged

### Make the *index* (staging area) *become* the newest commit
Physically, this just adds a pointer from it to the previous commit

    $ git commit

### Merge branch `refactor` into `master`.
This applies the diffs, asks you to resolve conflicts, and commits the result.

    $ git checkout master   # switch to master branch
    $ git merge refactor

##### Note: A `merge` is what happens when you `git pull` after it `fetch`es the changes.

### Add only *some* of the changes you've made
Starts an interactive loop that lets you select which "hunks" of (all) changes
you want to index.

Use "`?`" to see the commands

    $ git add -p

### Remove a file

    $ git rm [filename]

What this actually does is

* Remove it from your index ("staging area" from now on, that's more clear to
  me)
    * which will remove it from your repo from now on
        * not really sure the best way to say this
        * basically, if you were to clone the repo after git rm [fn] and git
          commit
        * then you wouldn't see the file in your clone
        * of course it will still be there in the history
* NB: **This also deletes the working file**
    * as if you did a little `rm [filename]` too

### Reset (empty) the staging area
Your changes will still be there on your local filesystem

    $ git reset
