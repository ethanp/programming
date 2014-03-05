C, the Language
---------------

##### 2/27/14

### string.h

    char *strcpy(char *dest, char *src)

* Also copies null-terminator.
* Returns `dest`

        char *strncpy(char *dest, char *src, size_t count)

* Only copies `count` characters
    * `if count ≤ strlen(src):` `dest` will not have a null-terminator
    * `else:` Will be padded with nulls
* Returns `dest`

### stdio.h

    int sprintf(char *buffer, char *format, ...)

* Basically, it calls `printf` on the `format` argument, but instead of writing the output to `stdout`, it writes it into the `buffer` argument
* Returns the number of characters written, or a negative value if there was an error

Reactive Applications
---------------------
##### 2/23/14

According to [Typesafe](http://typesafe.com/platform)

Reactive applications have one or more of the following defining traits:

* **Event driven** -- enables parallel, asynchronous processing of messages or events with ease.
* **Scalable** -- across nodes elastically
* **Resilient** -- recovers and repairs automatically
* **Responsive** -- single-page UIs that provide instant feedback

In particular, the *Typesafe Reactive Platform* consists of

* Play Framework
* Akka
* Scala
* Typesafe Console

Regex --- Lookahead & Lookbehind = "Lookaround"
-----------------------------------------------
##### 2/17/14

From [Regular-Expresions.info](http://www.regular-expressions.info/lookaround.html)

* They do not consume characters in the string, they only assert whether a match is possible or not
* Some regexes would be impossible without them

#### Lookahead
* **Negative lookahead** --- match something *not* followed by something else
    * E.g. `q` *not* followed by `u` --- `q(?!u)`
* **Positive lookahead** --- match something *only if* it's followed by something else
    * E.g. `q` followed by `u` --- `q(?=u)`
        * Recall that the `u` is not consumed in this case
* Any valid regular expression can be used inside the lookahead
  (even capture groups, which do require their own set of parentheses within the lookahead)

#### Lookbehind
* **Negative lookbehind** --- match something *not* preceded by something else
    * E.g. `b` *not* preceded by `a` --- `(?<!a)b`
* **Positive lookbehind** --- match something *only if* it's preceded by something else
    * E.g. `:` preceded by `cite` --- `(?<=cite):`
* Lookbehinds are generally restricted to only allowing some subset of the normal
  regex vocabulary, but the specifics vary (quite a bit) by language

Newlines
--------
##### 2/16/14

* On **Windows**, they use carriage-return & line-feed (`"\r\n"`)
* On **UNIX/Mac**, they use new-line, which is represented by the same
  ascii code as line-feed (`'\n'`), but does both CR-LF in one go

ASCII vs. UTF-8
---------------
##### 2/16/14

Both use the same number of bits, but ASCII is faster to read because it is not
a variable-width encoding, so the interpreter doesn't need to check the width
of each character.  UTF-8 is fully backwards-compatible to ASCII and avoids
endianness complications.  It seems to me, basically one should always use
UTF-8 instead of ASCII, and that is generally what people do.

FTP vs. HTTP
------------
##### 2/16/14

Mainly from [AlBlue's Blog](http://alblue.bandlem.com/2009/02/why-do-people-still-use-ftp.html)

* FTP was created for transferring files
    * That's why it gives you options for binary or ASCII modes
* HTTP was created for transmitting HTML
    * The file type is guessed based on the extension, though it can be specified
      in the header
* Originally, FTP was better for file downloads because HTTP/1.0 didn't
  support resumable downloads (where client disconnects part-way through and
  needs to restart). However, this was made possible in HTTP/1.1 via headers
  `Accept-Ranges` and `Content-Range`.
* HTTP also supports automatic data-compression, querying data-type before downloading,
  proxy support, running over SSL with HTTPS
* **People only use FTP over HTTP out of ignorance**
* In particular, WebDAV -- an extension of HTTP -- allows collaborative editing
  and management of documents stored on Web servers.

Glossary
========

* **Reentrant** -- 2/16/14 -- a function that can be interrupted in the middle of its
  execution and then safely called again ("re-entered") before its previous
  invocations complete execution. Once the reentered invocation completes, the
  previous invocations will resume correct execution.
* **[Partial Function](http://en.wikipedia.org/wiki/Partial_function)** -- 3/1/14 -- *normally* a function maps an *entire* domain to some range. In a **Partial Function**, however, not *every* element of the domain must be mapped. Some values in the domain may be *undefined* after passing through the partial function.