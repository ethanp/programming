require 'sinatra'

# note: I had to manually `gem install dm-sqlite-adapter` to get this to work
require 'data_mapper' # there was a typo in the tutorial

# this is "Singing with Sinatra, Part 2", from
# code.tutsplus.com/tutorials/singing-with-sinatra-the-recall-app--net-19128

# here, we are creating a new SQLite3 database in the current dir
DataMapper::setup(:default, "sqlite3://#{Dir.pwd}/recall.db")

# Here we are creating a table called "Notes" [sic] in the DB
# This naming convention is just like Ruby on Rails
class Note
  include DataMapper::Resource
  property :id, Serial  # Serial means it auto-increments
  property :content, Text, :required => true
  property :complete, Boolean, :required => true, :default => false
  property :created_at, DateTime
  property :updated_at, DateTime
end

# updates the DB to contain the tables and fields we have set
DataMapper.finalize.auto_upgrade!


######## ROUTES ########

get '/' do
  # Retrieve all notes in descending order by id
  # (syntax just like RoR's ActiveRecord).¸
  # Assign them to @notes *instance variable*.
  # Using instance variables means they are accessible
  # from the view file.
  # I believe this is just like RoR too.
  @notes = Note.all :order => :id.desc
  @title = 'All Notes'
  erb :home   # render views/home.erb
end

post '/' do
  n = Note.new
  n.content = params[:content]
  n.created_at = Time.now
  n.updated_at = Time.now
  n.save
  redirect '/'
end

get '/:id' do
  @note = Note.get params[:id]
  @title = "Edit note ##{params[:id]}"
  erb :edit
end

# Browsers can't actually *send* PUT requests, but we've "faked" it in
# the form by adding <input type="hidden" name="_method" value="put">
# into the form. Not sure how this *works* to make the POST get routed
# here as a PUT, but that's what it does
put '/:id' do
  n = Note.get params[:id]
  n.content = params[:content]
  n.complete = params[:complete] ? 1 : 0
  n.updated_at = Time.now
  n.save
  redirect '/'
end
