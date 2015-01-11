latex input:    mmd-article-header
Title:          Compilers Notes
Author:         Ethan C. Petuchowski
Base Header Level:		1
latex mode:     memoir
Keywords:       algorithms, computer science, theory, grammars, compilers, school, CS 375, Novak
CSS:            http://fletcherpenney.net/css/document.css
xhtml header:   <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
copyright:      2014 Ethan Petuchowski
latex input:    mmd-natbib-plain
latex input:    mmd-article-begin-doc
latex footer:   mmd-memoir-footer

Many of these notes are from *Compilers: Principles, Techniques, and Tools*,
by Aho, Lam, Sethi, and Ullman, Pearson 2nd ed. 2007; as well as Wikipedia and Stack Overflow etc.

## Introduction

### The Very Beginning

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

### The Structure of a Compiler

1. The compiler is *not* a *single box*, macro-wise it does
    1. **Analysis / Front End**
        1. break source into pieces, impose a grammatic structure, produce an
           intermediate representation, check its syntax and semantic content
           to be well-formed, and provide informative diagnostic messages to
           the user
        2. pass the generated *symbol table* and *intermediate representation*
           to the *synthesis* stage
    2. **Synthesis / Back End** --- optimize intermediate representation and generate machine-dependent machine-optimized code
2. There are what are called **phases**, though the compiler program itself
   may not be so thoroughly separated
    1. **Lexical Analyzer** --- reads *stream of **characters*** from source, and
       groups them by *lexeme* (matched via BNF) into **token** objects  of
       the form (**token name**, *attribute value*)
    2. **Syntax Analyzer / Parser** --- takes the *stream of **tokens*** and
       creates a **syntax tree**, where each interior node is an *operation*,
       and each child is an *argument*. The ordering of operations in the tree
       will be maintained when generating the target program. The parser can
       be specified using a *context-free grammar* (Ch 4).
    3. **Semantic Analyzer** (Ch 6)
        1. checks the syntax tree for *semantic consistency* with the language
           definition
        2. stores type information in the parse tree or symbol table
        3. performs *type checking*
        4. performs permitted type-*coersions* (e.g. `inttofloat`)
    4. **Intermediate Code Generator** (Ch 5-6) --- generates "machine-like code" for
       an "abstract machine" where one simple operation is done per line, so
       we can eventually assign each individual result to a register, fixing
       the order of operations; e.g. (pg. 9) `a = b + c * 60` where `c = 3.4f`
       might be turned into

            t1 = inttofloat(60)
            t2 = id3 * t1
            t3 = id2 + t2
            id1 = t3
    5. **Machine-Independent Code Optimizer** --- goals might be faster, less
       code, or lower power-consumption, e.g. (pg. 10)

            t1 = id3 * 60.0
            id1 = id2 + t1
    6. **Code Generator** (Ch 8 [post-midterm])
        1. maps intermediate code to target machine code
        2. selects registers or memory locations for each of the variables
        3. translates intermediate instructions into machine instructions
    7. **Machine-Dependent Code Optimizer** ---
    8. **Symbol Table** (Ch 2 [not on syllabus]) --- record variable & function names, collect
       attributes such as [return] type, scope, number and types of arguments,
       pass by reference or by value
3. Phases are grouped into *passes* that each read the input file into an
   output file, the front-end could be one-pass, optimization another, and the
   back-end final code generation another
4. Sometimes (e.g. LLVM) the same front-end can be used with different back-
   ends for targeting different architectures, and the same back-end can be
   used with different front-ends for different source languages
5. Tools with specialized built-in algorithms are used to help create the code
   for each of these stages of compilation
    1. **Scanner generators** --- produce *lexical analyzers* from *regular
       expressions*
    2. **Parser generators** --- produce syntax analyzers from grammatical
       descriptions
    3. **Syntax-directed translation engines** --- produce routines for
       walking the parse tree to generate intermediate code
    4. **Code-generator generators** --- use a collection of translation rules
       from intermediate to target machine language
    5. **Data-flow analysis engines** --- not sure what these are
    6. **Compiler-construction toolkits** --- integrate many of the above
       tools

