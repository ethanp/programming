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

# Notes from Beggining Rails 4
**This could be a blog topic about how it is hard to *intuit* while learning Rails without knowing much Ruby, what is "magic" and what isn't.**

* For example it is a surprise to know (see below) that scaffolds are not magical at all, they just print code into files you have access to modify. That's pretty simple.
* I guess as you learn more Rails it just becomes less magical overtime as you become acquainted with the sausage factory.
* **I wonder what the piece of code looks like that turns `as: "something"` into the methods `something_path` and `something_url`. Is that what they call *"metaprogramming"?***
    * Finding the answer to that would be a *better* blog topic than the first option

Quotes in this section are from *Beginning Rails 4*, written by Adam Gamble et al., published by Apress.

* All controllers inherit from `application_controller.rb`, so put methods here to make them accessible in every controller.
* The generators don't do any behind-your-back voodoo-magic, they just create the files you see and fill them with barebones-boilerplate that you can edit
* `Rake` is a build language & task automator
    * Its default actions are things like running database migrations, tests, and updates
    * Use `rake -T` to see what it can do
* Remember that **controllers are *plural***, and **models are *singular***, but the **database tables are *plural***
* When you `generate` new versions of `scaffold` files, it will ask you to confirm if you want to overwrite files you already have
* Ruby hash objects *preserve their order* (how strange)
* "An `ActiveRecord` subclass isn’t much different from a regular Ruby class; about the only difference is that you need to make sure you don’t unintentionally overwrite any of Active Record’s methods (find, save, or destroy, for example)"
    * "There is no difference between the methods Active Record creates and those you define."
* `ActiveRecord` handles the **ORM**. Your `model` classes inherit from `ActiveRecord::Base`
* `ActionPack` contains `ActionController` as well as `ActionView`
    * These facilitate the coordination between these two major components of your app
* Controllers inherit from `ApplicationController`, which is a class you can modify to imbue all your controllers with methods
    * `ApplicationController` in turn, inherits from `ActionController::Base`
* "Action Pack’s helpers provide a convenient location to encapsulate code that would otherwise clutter the view and violate MVC."
* The router is a great thing, because it decouples your *files* from your app's *interface*
* If you `generated` a `model` with `scaffolding`, then you can request `url/mymodels.json` and it will return a `JSON` array of all those objects in the database! Bind mlown.
* `redirect_to(@article)` simply performs `redirect_to(article_ path(:id => @article))`
* "In order to simulate state atop HTTP, Rails uses cookies. When the first request comes in, Rails sets a cookie on the client’s browser. The browser remembers the cookie locally and sends it along with each subsequent request. The result is that Rails is able to match the cookie that comes along in the request with session data stored on the server."
    * So where do you put the data when you want to give your app state? *In the* `session` *hash!*

## Directory structure
* **bin** --- executables
* **config** --- configuration files
* **config.ru** --- used by rack servers to start the app
* **lib** --- libraries your app might use
* **vendor** --- gems and plug-ins bundled automatically by the app

### Assets directories
* **app/assets** --- for assets owned by this application (images, style sheets, javascript)
* **lib/assets** --- for assets shared across applications, but created/owned by you (for perhaps a collection of apps)
* **vendor/assets** --- for assets created/owned by someone who isn't you (e.g. a framework)

## Controllers
### respond_to
    def abcd
      @task = Task.find(params[:task_id])
      respond_to do |format|
        format.html { redirect_to @task, notice: 'Task was successfully created.' }
        format.json { render :show, status: :created, location: @task }
      end
    end
        
1. If you want to respond to specific formats in different ways, you declare here (in a block) how to respond to each format
2. If you leave the default empty, it will `render` the `template` associated with this *controller-action* for this format
3. If that's not what you want, you can use `redirect_to` or `render` to declare what you *do* want
4. I think if you leave a format out, it will not be able to respond to that format
    5. So that's why you see `format.js` (with no block) to enable support for responses to AJAX requests.
1. This is an example of what a render partial with local variables looks like:

        <% = render 'header', title: 'My Blog' %>
        
   Any number of local variables can be assigned this way, and any object can be set as the value. In the preceding example, the partial has access to the local variable title.

### Testing
1. There's a *testing database* just for testing
2. The database gets dropped and re-created every time the tests are run

# Useful snippets

## Configuration

1. To make life easier, put this in `config/application.rb`

        I18n.enforce_available_locales = false

1. To allow your customized `devise`-based `User` forms to work with the authentication system, you're going to need to append something like this in `app/controllers/application_controller.rb`

        # http://stackoverflow.com/questions/16297797/add-custom-field-column-to-devise-with-rails-4
        before_filter :configure_permitted_parameters, if: :devise_controller?
        
        protected
        
        def configure_permitted_parameters
          registration_params = [:first_name, :last_name, :email, :password, :password_confirmation]
        
          if params[:action] == 'update'
            devise_parameter_sanitizer.for(:account_update) {
              |u| u.permit(registration_params << :current_password)
            }
          elsif params[:action] == 'create'
            devise_parameter_sanitizer.for(:sign_up) {
              |u| u.permit(registration_params)
            }
          end
        end

    Now you can add the new items to the registration form in `app/views/devise/registrations/new.html.erb`
    
          <div><%= f.label :first_name %><br/>
            <%= f.text_field :first_name, autocomplete: "off" %>
          </div>
        
          <div><%= f.label :last_name %><br/>
            <%= f.text_field :last_name, autocomplete: "off" %>
          </div>

    Lastly, not sure why, but you have to do put this in `config/routes.rb`
    
        # http://stackoverflow.com/questions/6567863/no-route-matches-get-users-sign-out
        devise_for :users do get '/users/sign_out' => 'devise/sessions#destroy' end
        # devise_for :users
## Controllers

### Don't allow access to the controller without authenticating with *devise*

    before_filter :admin_required, only: :destroy
    before_action :authenticate_user!

## Templates
### Notes
1. If a controller action doesn’t want to render its default template, it can render a different one by calling the render method explicitly. Any template file in the app/views directory tree is available.

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
            ...
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

### Validations

This is a *comprehensive* summary of the stuff [in the guide][validation guide]

[validation guide]: http://guides.rubyonrails.org/active_record_validations.html

1. Presence
2. Format (using regex
3. Length (min, max, range; errors for each type of mistake)
4. Must be a number (or int specifically) (min, max, range)
5. `on:` -- only check during (create, update)
6. You can specifically add `allow_blank` even if that breaks some other criterion
7. Must be empty
8. Must be unique (amongst specified attributes) (case insensitive)
9. You can also define your own validator function so things don't get all cluttered in-line
10. You can use `:if` and `:unless` options that call your boolean function on the data

### ON DELETE CASCADE

    has_many :subtasks, dependent: :destroy
    
## Routes

### For creating the `about_path` method etc., see the section **Defining Named Routes** below.

### _path vs _url

    rails c
    
    > app.categories_path
     => "/categories" 
    
    > app.categories_url
     => "http://www.example.com/categories" 

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

### Nesting
1. From [The Rails 3 Way Chp 3.7][]
2. To created nested resource routes, put this in routes.rb:

        resources :auctions do
          resources :bids
        end
    
3. now you can access the `auction_id` in the `bids_controller` via
    
        params[:auction_id]
        
4. This changes the names of the routes for `bids` *and* the way you call them

        auction_bids_path(auction)
        auction_bid_path(auction, bid), method: :delete
        
5. Now: If you want to add a bid to an auction, your nested resource URL would be

        http://localhost:3000/auctions/5/bids/new

    **The auction is identified in the URL rather than having to clutter your new bid form data with hidden fields or resorting to non-RESTful practices.** *(bingo!)*
    
6. Resource routes accept a `:shallow` option that helps to shorten URLs where possible. The goal is to leave off parent collection URL segments where they are not needed. The end result is that the only nested routes generated are for the `:index`, `:create`, and `:new` actions. The rest are kept in their own shallow URL context.

7. It's easier to illustrate than to explain, so let's define a nested set of resources and set `:shallow` to `true`:

        resources :auctions, :shallow => true do
          resources :bids do
            resources :comments
          end
        end

    alternatively coded as follows (if you're block-happy)

        resources :auctions do
          shallow do
            resources :bids do
              resources :comments
            end
          end
        end
    
    The resulting `comments` routes are:

                        GET    /bids/:bid_id/comments(.:format)
           bid_comments POST   /bids/:bid_id/comments(.:format)
        new_bid_comment GET    /bids/:bid_id/comments/new(.:format
                        GET    /comments/:id(.:format)
                        PUT    /comments/:id(.:format)
                comment DELETE /comments/:id(.:format)
           edit_comment GET    /comments/:id/edit(.:format)

    If you analyze the routes generated carefully, you'll notice that the nested parts of the URL are only included when they are needed to determine what data to display.

8. Now, let's say you want to be able to view a resource in a *different* way then the ones you've provided for the standard CRUD operations, how do you cleanly add in the URL for the new way of viewing/updating/etc. that resource? Well to get a URL that looks like

        /auctions/3/bids/5/retract 
   
    You'd add an extra "member route"
   
        resources :auctions do
          resources :bids do
            member do
            
              # v1
              get :retract
              post :retract
              
              # v2
              match :retract, via: [:get, :post]
              
            end
          end
        end
    
    That you can call like
    
        link_to "Retract", retract_bid_path(auction, bid)
        
9. To create a route that only applies to "news resources (that haven't been saved to the DB yet), use a `new` block

        resources :reports do
          new do
            post :preview
          end
        end
        
    which gives you
    
        preview_new_report POST   /reports/new/preview(.:format)
  
[The Rails 3 Way Chp 3.7]: http://www.informit.com/articles/article.aspx?p=1671632&seqNum=7
## Forms
### Notes

1. By default, a `<%= form_tag do %>` will `POST` to the **current page**
2. The CSRF `authenticity_token` is provided automatically in a `hidden input`
3. You can choose the destination and method quite simply

        <%= form_tag "/search", method: "get" do %>

4. One can rely on the router as well

        form_tag({controller: "people", action: "search"}, method: "get", class: "nifty_form")
        
5. Basic helpers, with names ending in "`_tag`" (such as `text_field_tag` and `check_box_tag`), generate just a single `<input>` element.
    1. **When the form is submitted, the first parameter name will be passed along with the form data, and will make its way to the params hash in the controller with the value entered by the user for that field**
        1. For example, if the form contains `<%= text_field_tag(:query) %>`, then you would be able to get the value of this field in the controller with `params[:query]`.

#### Forms for editing or creating a model object
6. Rails provides helpers tailored to use for this *instead* of the `*_tag` tags, for example `text_field` and `text_area`.
8. the **first argument** is the name of an `@instance_variable` and 
2. the **second argument** is the name of a method (usually an `@attribute`) to call on that object
    7. If your *controller* has defined `@person` and that person's name is `Henry` then a form containing:

            <%= text_field(:person, :name) %>

        will produce output similar to

            <input id="person_name" name="person[name]" type="text" value="Henry"/>

8. Upon form submission the value entered by the user will be stored in `params[:person][:name]`.
9. The `params[:person]` hash is suitable for passing to `Person.new` or, if `@person` is an instance of `Person`, `@person.update`. 
10. While the name of an attribute is the most common second parameter to these helpers this is not compulsory.
11. In the example above, as long as person objects have a name and a name= method Rails will be happy.
12. You must pass the *name of an instance variable*, i.e. `:person` or `"person"`, *not an actual instance* of your model object.

#### Binding a form to an object
1. **This is what comes with the *scaffold***
2. Imagine an `@article` was passed in from the *controller*
3. Now we have

        <%= form_for @article, url: {action: "create"}, html: {class: "nifty_form"} do |f| %>
          <%= f.text_field :title %>
          <%= f.text_area :body, size: "60x12" %>
          <%= f.submit "Create" %>
        <% end %>

4. In this:
    5. `@article` is the actual object being edited.
    6. There is a *single* hash of options.
    3. *Routing* options are passed in the `:url` hash,
    4. *HTML* options are passed in the `:html` hash.
    5. One can provide a `:namespace` option for your form to ensure uniqueness of id attributes on form elements.
    6. The `form_for` method yields a `form builder` object `f`.
    7. Methods to create form controls are called on the form builder object `f`
5. The name passed to `form_for` controls the key used in `params` to access the form's values.
    6. Here the name is `article` and so in the `create` *action* `params[:article]` will have keys `:title` and `:body`.
6. If you want to do this stuff, you should declare `resources :article`
    7. This means you can make `create` and `edit` forms, without specifying the URL or the Method, you just do `form_for @article`
#### Ref
* [Rails Guide to Forms](http://guides.rubyonrails.org/form_helpers.html)

#### Params hash info

For example if a form contains

    <input id="person_name" name="person[name]" type="text" value="Henry"/>
    
the params hash will contain

    {'person' => {'name' => 'Henry'}}

and 
    
    params[:person][:name]
    
will retrieve the submitted value in the controller.

## Code Generation Commands

### Generate Controller

    rails generate controller StaticPages home help 
    
To *not* create **unit test** files, append `--no-test-framework`

### Generate Model

1. See [RailsGuides tutorial: advanced model generators](http://railsguides.net/advanced-rails-model-generators/)

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

#### Inherit from other table

    $ rails g model admin --parent user
    
generates the model

    class Admin < User
    end

#### Make columns indexes

    $ rails g model user email:index location_id:integer:index
    
#### Make column unique

    $ rails g model user pseudo:string:uniq
    
#### Set string length limit

    $ rails generate model user pseudo:string{30}
    
#### Make column *not NULL*

According to [StOve][ModelNulling], you have to manually edit the migration file for that.
The generator just offers a starting point, it does not do everything.
Doing this in the migration file is very easy anyway.

    class CreateIntervals < ActiveRecord::Migration
      def change
        create_table :intervals do |t|
          t.datetime :start,  null: :false
          t.datetime :end,    null: :false
          t.references :task, index: true
    
          t.timestamps
        end
      end
    end
    

Well, I tried that, and it *didn't* put it in the `schema.rb`, so I had to it *again* as a migration,
and now it's there in the `schema.rb`.

    class ChangeTimesNotNullInterval < ActiveRecord::Migration
      def change
        change_column_null( :intervals, :start, false )
        change_column_null( :intervals, :end,   false )
      end
    end


[ModelNulling]: http://stackoverflow.com/questions/4562677/passing-additional-parameters-to-rails-generate-model

#### Reference another table

Creates a `group_id` *and* add's `belongs_to: :group`

    # yes, it's *singular*
    $ rails g model member group:references

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
    
### Go to the heroku webpage

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

### Resources

    resources :products
    
    # customizeable
    resources :products, :except => [:new, :create, :destroy]
    resources :products, :only => [:index, :show]
    
This maps the url `/` to `/static_pages/home`

## Commands

Start development server

    rails s

Make `console` reverse whatever changes you make to the database during the session

    rails console --sandbox
    
Run all tests

    bundle exec rspec spec/

Run a specific test suite

    bundle exec rspec spec/requests/user_pages_spec.rb
    
Run a single test within a test suite

    bundle exec rspec spec/requests/user_pages_spec.rb -e "signup page"

## App Configuration

Force all access to the app over SSL, use Strict-Transport-Security,
and use secure cookies.

`config/environments/production.rb`

    config.force_ssl = true

## Debugging

### Model items are "invalid"

E.g.

    Failure/Error: expect(@user.reload.email).to eq mixed_case_email.downcase
         ActiveRecord::RecordNotFound:
           Couldn't find User without an ID

Try this

    rake db:migrate
    rake db:test:prepare

### Resetting the development database

    rake db:reset

### Quirks

1. If you save some DB items into a variable, then modify those items, the items *within* the variable will appear to not have changed, but *make no mistake*, if you re-query the DB, the items will be changed.

### Controller not instantiated

1. Make sure the controller file and it's class name have the resource *in **plural***

## Embedded Ruby (ERb) templates

* `<%...%>` **executes** the code inside
* `<%=...%>` **executes** it **and** ***inserts*** the results into the template.

## Other

1. Those `model_helper.rb` files are for *view* specific logic, not *model* specific logic. Who'da thunk it?

## Ruby (moved to its own Ruby Notes.md)
