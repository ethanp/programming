require 'sinatra'
require 'datamapper'

# here, we are creating a new SQLite3 database in the current dir
DataMapper::setup(:default, "sqlite3://#{Dir.pwd}/recall.db")

# here we are creating a table called "Notes" [sic] in the DB
# this naming convention is just like Ruby on Rails
class Note
  include DataMapper::Resource
  property :id, Serial
  property :content, Text, :required => true
  property :complete, Boolean, :required => true, :default => false
  property :created_at, DateTime
  property :updated_at, DateTime
end

DataMapper.finalize.auto_upgrade!
