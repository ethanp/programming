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


## Embedded Ruby (ERb) templates

* `<%...%>` **executes** the code inside
* `<%=...%>` **executes** it **and** ***inserts*** the results into the template.
