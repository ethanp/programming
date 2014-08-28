latex input:        mmd-article-header
Title:              C++ Notes
Author:             Ethan C. Petuchowski
Base Header Level:  1
latex mode:         memoir
Keywords:           Java C++, OOP, old school garbage, school
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
Copyright:          2014 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

Most of these notes come from *Introduction to Object Oriented Programming*,
3rd Edition, by ?? Budd.

# Chapter by Chapter

## Chapter 1: Thinking Object Oriented

* Fundamental features of OOP were invented in the 1960's
* OO-Languages came to public attention in the 1980's
    * Description of Smalltalk in Byte magazine 1981
    * First international conference on OOP 1986
* Resonates with the way people think about problems in everyday life
* *Software crisis* --- the tasks we'd like to solve with computers outstrip
  our abilities
  
### Does it actually matter what language you use?

* "`FORTRAN` programs *can* be written in any language"
* The same programmer will think of a different solution/algorithm when using
  a different language
    * The given example is with developing algorithms for comparing DNA
      sequences using `FORTRAN` (loops) and `APL` (high-level operators like
      `sort`)
* **Sapir-Whorf hypothesis** --- from linguistics
    > *Individuals working in one language can imagine thoughts and utter
      ideas that cannot be transloated or even understood by individuals
      "operating in a different linguistic framework".*
    * Rejected by many linguists (viz. they think that with enough work,
      any idea can be expressed in any language)
        * This sounds pretty vacuous to me
* **Church's conjecture** --- from mathematics/nascent-CS in the 1930s 
    > *Any computation for which there exists an effective procedure can
      be realized by a Turing machine.*
    * Unprovable because there's no rigorous definition of *"effective 
      procedure"*
        * Believed to be *"true"* nonetheless
* Any lanugage with a conditional statement and a looping construct can
  emulate a Turing machine [1966]
* In any case, working in a certain programming language makes certain
  things easier to think of and to accomplish

### The rest of the chapter

* This is in a block quote so maybe it's important, but probably not
    > **An object oriented program is** structured as a *community* of
      interacting agents, called *objects*. Each object has a role to
      play. Each object provides a service, or performs an action, that
      is used by other members of the community.
* Similar with this one
    > Action is initiated in OOP by the transmission of a *message* to
      an agent (an object) responsible for the action. The message
      encodes the request for an action and is accompanied by any
      additional information (arguments) needed to carry out the
      request. The *receiver* is the object to whom the message is sent.
      If the receiver accepts the message, it accepts the responsibility
      to carry out the indicated action. In response to a message, the
      receiver will perform some *method* to satisfy the request.
* **Information hiding** --- the client sending the request need not know the actual means by which the request will be honored
* **Messages versus Procedure Calls**
    1. First 
        * In a message there is a designated *receiver* for that message,
      and that receiver is some object to which the message is sent
        * In a procedure call, there is no designated receiver
    2. Second
        * Something

# Big Ideas

## Wouldn't it be cool to have something in this section?