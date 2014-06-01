# Useful snippets from this Michael Hartl's Rails Tutorial

[Tutorial Website](http://www.railstutorial.org/)

## Chapter 2: Demo App

### Generate model with CRUD code

    rails generate scaffold User name:string email:string

### Connect two models as One-To-Many

    class User < ActiveRecord::Base
      has_many :microposts
    end
    
    class Micropost < ActiveRecord::Base
      belongs_to :user
      validates :content, length: { maximum: 140 } # just another thing to note
    end

## Chapter 3: Static Pages

### Generate Controller

    rails generate controller StaticPages home help
    
This creates a basic **`static_pages_controller.rb`** with the provided methods

    class StaticPagesController < ApplicationController
      def home
      end
    
      def help
      end
    end

And also adds some `routes` for it

    get 'static_pages/home'
    get 'static_pages/help'


### Heroku

#### First deploy to heroku

    heroku create
    git push heroku master
    heroku run rake db:migrate

#### Pushing to heroku

    git push
    git push heroku
    heroku run rake db:migrate

### Undo

### Controllers

    rails generate controller FooBars baz quux
    rails destroy  controller FooBars baz quux
    
### Models

    rails generate model Foo bar:string baz:integer
    rails destroy model Foo

### Migrations

Migrations change the state of the database using

    rake db:migrate

We can undo a single migration step using

    rake db:rollback

To go all the way back to the beginning, we can use

    rake db:migrate VERSION=0

As you might guess, substituting any other number for
0 migrates to that version number, where the version
numbers come from listing the migrations sequentially.