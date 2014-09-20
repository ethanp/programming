latex input:        mmd-article-header
Title:              Prolog Notes
Author:             Ethan C. Petuchowski
Base Header Level:  1
latex mode:         memoir
Keywords:           logic programming, syntax, language, prolog
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
Copyright:          2014 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

### Sources
1. [Learn Prolog Now](http://www.learnprolognow.org)
2. [Prolog Intro](http://www.cse.unsw.edu.au/~billw/cs9414/notes/prolog/intro.html)

## Introduction

1. **Prolog programs are** simply *knowledge bases*, **collections of facts and rules** which describe some collection of relationships
2. How do we use a Prolog program? By posing queries. That is, by asking questions about the information stored in the knowledge base.
3. Prolog programs specify relationships among objects and properties of objects.
    1. When we say, "John owns the book", we are declaring the ownership relationship between two objects: John and the book.
    2. When we ask, "Does John own the book?" we are trying to find out about a relationship. 
4. Relationships can also be rules, such as:

    > Two people are sisters if they are both female and they have the same parents.
5. A rule allows us to find out about a relationship even if the relationship isn't explicitly stated as a fact.

