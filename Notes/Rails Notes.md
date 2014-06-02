latex input:        mmd-article-header
Title:              Rails Notes
Author:             Ethan C. Petuchowski
Base Header Level:  1
latex mode:         memoir
Keywords:           Math, DSP, Digital Signal Processing, Fourier Transform
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
Copyright:          2014 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

# Useful snippets from Rails Tutorial dot org

## Templates

### For the base template

#### Located at `app/views/layouts/application.html.erb`

#### IE lt 9 shim

    <%= render 'layouts/shim' %>

* Put it in the `<head>`.
* This is a **partial**, which means it loads the contents of a file named
  `app/views/layouts/_shim.html.erb`. You actually have to define that file
  yourself to have the standard content.
  
        <!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

#### Insert body content

      <body>
        <%= render 'layouts/header' %>
        <div class="container">
          <%= yield %>
        </div>
      </body>

* This assumes we have a **partial** called `app/views/layouts/_header.html.erb`
  with our bootstrap code
  
        <header class="navbar navbar-fixed-top navbar-inverse">
          <div class="navbar-inner">
            <div class="container">
              <%= link_to "sample app", '#', id: "logo" %>
              <nav>
                <ul class="nav pull-right">
                  <li><%= link_to "Home",    '#' %></li>
                  <li><%= link_to "Help",    '#' %></li>
                  <li><%= link_to "Sign in", '#' %></li>
                </ul>
              </nav>
            </div>
          </div>
        </header>


### The <%= link_to %> thing

#### For example

    <%= link_to "sample app", '#', id: "logo" %>
    
Turns into

    <a href="#" id="logo">sample app</a>
    
Basically, we have

* Text to display
* URL to link to
* And the `id` attribute (not required, obviously)

### Yield

    <%= yield %>
    
This inserts the contents of each page into the site layout.

## Models

### Connect two models as One-To-Many

    class User < ActiveRecord::Base
      has_many :microposts
    end
    
    class Micropost < ActiveRecord::Base
      belongs_to :user
      validates :content, length: { maximum: 140 } # just another thing to note
    end

## Code Generation Commands

### Generate model with CRUD code

    rails generate scaffold User name:string email:string

### Generate Controller

    rails generate controller StaticPages home help
    
##### This

1. creates a basic **`static_pages_controller.rb`** with the provided methods

        class StaticPagesController < ApplicationController
          def home
          end
        
          def help
          end
        end

2. adds some `routes` for it

        get 'static_pages/home'
        get 'static_pages/help'
        
3. creates some stub `<method>.html.erb` files.

**Note:** of course you can always perform these 3 steps *after* executing
the `generate` command to add pages that you didn't generate.

### Undo

#### Controllers

    rails generate controller FooBars baz quux
    rails destroy  controller FooBars baz quux
    
#### Models

    rails generate model Foo bar:string baz:integer
    rails destroy model Foo

#### Migrations

Migrations change the state of the database using

    rake db:migrate

We can undo a single migration step using

    rake db:rollback

To go all the way back to the beginning, we can use

    rake db:migrate VERSION=<NUMBER> 
    
The version numbering starts at zero


### Integration tests

E.g. for the `StaticPagesController`

    rails generate integration_test static_pages
  

which produces `static_pages_spec.rb`, where your associated tests live.


## Heroku

### First deploy to heroku

    heroku create
    git push heroku master
    heroku run rake db:migrate

### Pushing to heroku

    git push
    git push heroku
    heroku run rake db:migrate


# Rails/Ruby/ERb Syntax

## Embedded Ruby (ERb) templates

* `<%...%>` **executes** the code inside
* `<%=...%>` **executes** it **and** ***inserts*** the results into the template.

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
    
## Do vs. Braces

The common convention is to use curly braces only for short one-line blocks
and the `do..end` syntax for longer one-liners and for multi-line blocks.

## Optional Parentheses and Curly Braces

#### TFAE

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
