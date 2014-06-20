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

## Useful Commands

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

## Ranges

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

## Words

    >> %w[foo bar baz]
    => ["foo", "bar", "baz"]

## Do vs. Braces

The common convention is to use curly braces only for short one-line blocks
and the `do..end` syntax for longer one-liners and for multi-line blocks.

## Optional Parentheses and Curly Braces

### TFAE

    stylesheet_link_tag("application", media: "all", "data-turbolinks-track" => true)
    stylesheet_link_tag "application", media: "all", "data-turbolinks-track" => true
    stylesheet_link_tag "application", { media: "all", "data-turbolinks-track" => true }

Note that in this case, writing

    data-turbolinks-track: true

would be invalid because of the hyphens.

* **Parentheses** are *optional*
* When *hashes* are the last argument in a function call, the **curly braces** are *optional*

## Symbols

Symbols are easier to compare to each other; strings need to be compared
character by character, while symbols can be compared all in one go. You
*cannot* call `String` methods on `Symbols` though.

## Modifying built-in classes

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

## Defining your own classes

* Use `<` for inheritance
* `attr_accessor :name, :email` -- creates **getter** and **setter** methods
  for the `@name` and `@email` *instance variables*.

## Importing external functionality

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

