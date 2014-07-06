latex input:    mmd-article-header
Title:				  Ruby Notes
Author:			    Ethan C. Petuchowski
Base Header Level:		1
latex mode:     memoir
Keywords:			  Ruby, programming language, syntax, fundamentals
CSS:				    http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
copyright:		  2014 Ethan Petuchowski
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer


## Useful commands

### Chomp
**6/19/14**

* *With no arguments*: `chomp` **removes the DOS or Unix line ending**, if either is present
* *With arguments*: **removes the ending you specify**

E.g.

	> "asdf\n".chomp
	=> "asdf"

	> 'abc123'.chomp('123')
	=> "abc"

### Other

	> Dir.pwd
	=> "/Users/ethan/code/non_apple/programming/Educational/Ruby/Sinatra/Singing with Sinatra/Part 2"

	> `pwd`.chomp == Dir.pwd
	=> true




## Modules

Modules give us a way to package together related methods, which can
then be mixed in to Ruby classes using include. When writing ordinary
Ruby, you often write modules and include them explicitly yourself,
but in the case of a helper module Rails handles the inclusion for us.

    module ApplicationHelper
      def full_title(page_title)
        ...codez...
      end
    end

## Syntax features
### Ranges

    >> a[2..-1]                         # Use the index -1 trick.
    => [2, 3, 4, 5, 6, 7, 8, 9]

Ranges also work with characters:

    >> ('a'..'e').to_a
    => ["a", "b", "c", "d", "e"]

Oops:

    >> 0..9.to_a              # Oops, call to_a on 9.
    NoMethodError: undefined method `to_a' for 9:Fixnum
    >> (0..9).to_a            # Use parentheses to call to_a on the range.
    => [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

Use **3 dots** to **exclude** the last item

    >> (0...10) == (0..9)
     => false 
    >> (0...10)
     => 0...10 
    >> (0...10).to_a == (0..9).to_a
     => true 

### Words

    >> %w[foo bar baz]
    => ["foo", "bar", "baz"]

This would be *great* to have in R. When I think about it, there are a
number of cases where this would be useful.

### Symbols

Symbols are *O*(1) to compare to each other; strings need to be compared
character *O*(*N*). You *cannot* call `String` methods on `Symbols` though.

### Variable prefixes

#### $ --- global variable
* these variables will have the same *value* in *all* places in the program
* they can be used anywhere

#### @ --- instance variable
* ***at**tributes* of something
* belong to an instance of a class

#### @@ --- class variable
* see *double-at*, think *attribute all*
* they are a single attribute over all instances of a class


### That nifty regex stuff

The `a =~ /b/` returns the *index* of the *first match* of `b` in `a`, and `nil` if there are no matches.

    a = 'something'
    
    a =~ /a/
     => nil
     
    a =~ /omet/
     => 1

    line.sub(/Perl/, 'Ruby')    # replace _first_ 'Perl' with 'Ruby'
    line.gsub(/Python/, 'Ruby') # replace _every_ 'Python' with 'Ruby'

### Implementing a method that takes a block

`each` is much like `map` in `Scala`.

    def each
      for each element
        yield(element)
      end
    end
    
Whenever the code reaches `yield(args)`, it executes the `block` that was
passed in, passing `args` *into* the `block`. Reread that if necessary,
it's quite simple. In this case, if we do

    [1,2,3].each do |x|
      puts x * 2
    end
    
what we're doing is calling

    for elem in [1,2,3]
      puts elem * 2  # this is the yield block substitution
    end

## Syntax rules

### Do vs. Braces

The common convention is to use curly braces only for short one-line blocks
and the `do..end` syntax for longer one-liners and for multi-line blocks.

### Optional parentheses and curly braces

#### TFAE

    stylesheet_link_tag("application", media: "all", "data-turbolinks-track" => true)
    stylesheet_link_tag "application", media: "all", "data-turbolinks-track" => true
    stylesheet_link_tag "application", { media: "all", "data-turbolinks-track" => true }

Note that in this case, writing

    data-turbolinks-track: true

would be invalid because of the hyphens.

* **Parentheses** are *optional*
* When *hashes* are the last argument in a function call, the **curly braces** are *optional*

### Defining a class

The constructor is called `initialize(params)`, and it is called with `MyClass.new(params)`
 
    class Song
      def initialize(name, artist, duration)
        @name     = name
        @artist   = artist
        @duration = duration
      end
    end

However at this point, no one else can get/set these instance variables, they are private.

We can implicitly provide setters with

    attr_writer :duration

We can implicitly provide getters with

    attr_reader :artist
    
We can implicitly provide both simple setters & getters with

    attr_accessor :name
    
For better print-outs, modify `to_s`. Note that just adding the code below *will work* because we can modify classes by pretending to re-implement, (see [below](#modifying) for more).
    
    class Song
      def to_s
        "Song: #{@name}--#{@artist} (#{@duration})"
      end
    end

### Modifying built-in classes
<a id="modifying"></a>

Ruby classes can be opened and modified, allowing us to add methods to them:

    class String
      # Returns true if the string is its own reverse.
      def palindrome?
        self == self.reverse
      end
    end

    => nil

    "deified".palindrome?

    => true

It’s considered bad form to add methods to built-in classes without
having a really good reason for doing so.

### Defining your own classes

* Use `<` for inheritance
* `attr_accessor :name, :email` -- creates **getter** and **setter** methods
  for the `@name` and `@email` *instance variables*.

### Importing external functionality

* [From tutorialspoint](http://www.tutorialspoint.com/ruby/ruby_modules.htm)

In `FileA.rb`

    module Foo
      fooVar = "Something"
      def Foo.bar
        puts "something else"
      end

meanwhile in another file

    require `FileA`
    class ABC
      include Foo
      puts Foo::fooVar  # => Something
    end
    Foo.bar             # => something else

### Class vs Instance methods

| Instance methods | Class methods |
| :---: | :---: |
| `instanceName.method` | `ClassName::method` |
