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

## Routes

### For creating the `about_path` method etc., see the section **Defining Named Routes** below.

### Defining REST-style URLs for a Model

Add the following line to `routes.rb`

    resources :users

In the following table

* **HTTP request** -- request type sent by the browser to the server
* **URL** -- where to send it
* **Action** -- the method that get's called on the associated controller
* **Named route** -- method we can call to invoke this action from within the server
* **Purpose** -- why we'd want to perform this action


| HTTP request | URL             | The Action  | Named route | Purpose |
| :----------- | :-------------- | :---------- | :---------- | :------ |
| `GET`        | `/users`        | **index**   | `users_path` | page to list all users |
| `GET`        | `/users/1`      | **show**    | `user_path(user)` | page to show user |
| `GET`        | `/users/new`    | **new**     | `new_user_path` | signup page |
| `POST`       | `/users`        | **create**  | `users_path` | create a new user |
| `GET`        | `/users/1/edit` | **edit**    | `edit_user_path(user)` | page to edit user with id `1` |
| `PATCH`      | `/users/1`      | **update**  | `user_path(user)` | update user |
| `DELETE`     | `/users/1`      | **destroy** | `user_path(user)` | delete user |

## Code Generation Commands

### Generate Controller

    rails generate controller StaticPages home help 
    
To *not* create **unit test** files, append `--no-test-framework`

### Generate Model

#### Basic Model

    rails generate model User name:string email:string
 
Note that **unlike controller names, model names are singular**

##### This

1. creates a new **migration file** called `db/migrate/[timestamp]_create_users.rb`, 
   which allows us to alter the structure of the database *incrementally*

        class CreateUsers < ActiveRecord::Migration
          def change
            create_table :users do |t|  # note it's plural, "a DB has many users"
              t.string :name
              t.string :email
        
              t.timestamps  # creates magic columns 'created at' and 'updated at'
            end
          end
        end

2. creates a **model file** called `app/models/user.rb` with the simple code

        class User < ActiveRecord::Base
        end


##### Migrate

Now we *"migrate up"* using
    
    [bundle exec] rake db:migrate

#### Generate model with CRUD code

    rails generate scaffold User name:string email:string
    
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


#### Generate model migration

    rails generate migration add_password_digest_to_users password_digest:string
    
In fact, this gives *Rails* enough information to construct the entire migration
adding the `password_digest` field to the `User` table

    class AddPasswordDigestToUsers < ActiveRecord::Migration
      def change
        add_column :users, :password_digest, :string
      end
    end


### Undo

#### Controllers

    rails generate controller FooBars baz quux
    rails destroy  controller FooBars baz quux
    
#### Models

    rails generate model Foo bar:string baz:integer
    rails destroy model Foo

#### Migrations

* Migrations change the state of the database using

        rake db:migrate

* We can undo a single migration step using

        [bundle exec] rake db:rollback

* To go all the way back to the beginning, we can use

        rake db:migrate VERSION=<NUMBER> 
    
* The version numbering starts at zero

* For things like `create table`, undo is simple, but for adding/removing columns,
  if you want to be able to undo, you must define `up` and `down` in place of the
  `change` method. These define how to do/undo your the changes to your DB.

* After running a `db:migrate`, you probably want to run

        [bundle exec] rake test:prepare
   
     to make sure the test DB (`db/test.sqlite3`) matches the development DB
      (`db/development.sqlite3`)
      

    * Also, sometimes the test database gets corrupted and needs to be reset.
    * If your test suite is mysteriously breaking, be sure to try running
      `rake test:prepare` to see if that fixes the problem.


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
    
### Opening on heroku

    heroku open
    
### View logfile  on heroku

    heroku logs
    
### Remote console

    heroku run console
    
    >> Rails.env
    => "production"

## Specifying environments

### Console

Defaults to development

    rails console

You can specify others

    rails console test
    
    >> Rails.env
    => "test"

### Server

Turn your local machine into a `localhost` server (defaults to development)

    rails server
    
You can specify other environments

    rails server --environment production
    
### Migrating databases

Defaults to development

    bundle exec rake db:migrate

But you can specify others

    bundle exec rake db:migrate RAILS_ENV=production
    
## Adding passwords to the (e.g. User) model

1. Add the line `has_secure_password` to the model for which you'd like to install passwords (e.g. `User`)
2. Run a migration to add *password digests* to that model (this generates all you need to run the migration)

        rails generate migration add_password_digest_to_users password_digest:string

### What does this ensure?

1. `User.password` must match `User.password_confirmation`
2. `User.password` cannot be empty or `" "` *(ensure presence)*
3. We can *authenticate* the retrieved `User` using:

        current_user = user.authenticate(password)

    * this is supposed to **return** the **user** **if** the *password* is *correct*
    * otherwise it **returns** **false**
    
4. Adds `password` and `password_confirmation` attributes to `User`

## Defining named routes

We'd like to stop hard-coding the url in our unit tests and view templates like

    visit '/static_pages/about'

or

    <%= link_to "About", /static_pages/about' %>

and instead use a keyword like

    visit about_path
    
or

    <%= link_to "About", about_path %>
    
So we change our `route` from

    get 'static_pages/about'
    
to

    match '/about', to: 'static_pages#about', via: 'get'
    
This gives us both the keywords

    about_path -> '/about'
    about_url  -> 'http://localhost:3000/about'

### It's different for the homepage

    root  'static_pages#home'
    
This maps the url `/` to `/static_pages/home`



# Rails/ERb Syntax
## Embedded Ruby (ERb) templates

* `<%...%>` **executes** the code inside
* `<%=...%>` **executes** it **and** ***inserts*** the results into the template.

## Ruby (moved to its own Ruby Notes.md)
