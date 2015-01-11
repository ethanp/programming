latex input:    mmd-article-header
Title:          Compilers Notes
Author:         Ethan C. Petuchowski
Base Header Level:		1
latex mode:     memoir
Keywords:       algorithms, computer science, theory, grammars
CSS:            http://fletcherpenney.net/css/document.css
xhtml header:   <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
copyright:      2014 Ethan Petuchowski
latex input:    mmd-natbib-plain
latex input:    mmd-article-begin-doc
latex footer:   mmd-memoir-footer

Many of these notes are from *Compilers: Principles, Techniques, and Tools*,
Aho Sethy, Ullman, 2nd ed. 2007, as well as Wikipedia and Stack Overflow etc.

## Introduction

### §1.1.1 The Very Beginning

1. A **compiler** turns *source code* into an executable *program* which can
   *afterwards* be used to input data is turned into output data
2. An **interpreter** uses the source code *itself* to turn input data into
   output data
3. Since the *compiler* generally produces *machine-language* that can be
   directly executed by a processor, it will generally produce an equivalent
   program that can run faster than an interpreted version.  However, since an
   interpreter runs on the source code itself, it can provide clearer feedback
   to the programmer about the state of the program in relation to the
   original source code at the point where a bug occurs.  It is also simple to
   iteratively write an interpreted program because one needn't make the step
   of compiling between writing code and running it on test input data.
4. The compiler may be part of a larger pipeline for generating an executable
   program that may include a
    1. **proprocessor** --- collect source files into one; expand macros
    2. **assembler** --- turn *assembly-language* *symbols* into **relocatable
       machine code** ([explanation and example][rmcso]), i.e. an **object
       file** containing **object code** (e.g. *ELF*---**E**xecutable and
       **L**inkable **F**ormat)
        1. Turn assembler mnemonics and syntax into numerical equivalend
           *opcodes* and control bits
        2. Resolve symbolic nams for memory locations
        3. Inline *called* subroutines into instruction sequences
        4. Perform ISA-specific optimizations, e.g. instruction rearrangements
           to exploit CPU pipeline efficiently
    3. **linker** --- resolve "external memory addresses" (references to code-
       locations in another *object file*)
    4. **loader** --- loads all relevant executable *object files* into memory

    We can also consider the whole pipeline to be the "Compiler". We might do
    this so that we don't have to compile the whole thing at once, and so that
    the executable code can be spread across multiple files, loaded
    dynamically, and reused between multiple programs.
5. Some compilers produce *assembly*-language code rather than machine code.
   This is more human readable and therefore makes it easier to debug the code
   and the compiler itself. Also the assembly may still have [unresolved]
   *symbols* in it refering to locations in other parts of the source code
   that were not compiled at the same time (e.g. *libraries*). These symbols
   will referring to "external memory addresses" will be "resolved" by the
   *linker*.
6. It could be a good idea to use C as a target language for a compiler
   because there are highly robust, optimized, and efficient compilers going
   from C to all sorts of architectures that will likely be maintained and
   improved until we move on to quantum computers and possibly beyond.

[rmcso]: http://stackoverflow.com/a/22890413/1959155

### §1.2 The Structure of a Compiler

1. The compiler is *not* a *single box*
