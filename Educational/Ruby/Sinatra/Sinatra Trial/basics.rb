# tutorial from
# http://code.tutsplus.com/tutorials/singing-with-sinatra--net-18965
# 6/19/14

require 'sinatra'

get '/' do
  "Hello, World!"
end

get '/about' do
  'A little about me.'
end

# this will ONLY match paths with ONE parameter
# after the hello (that means it won't accidentally
# catch the next version too)
get '/hello/:name' do
  "Hello there, #{params[:name]}."
end

# only matches if it has two params after hello
get '/hello/:name/:city' do
  "Hey there #{params[:name]} from #{params[:city]}."
end

# this will except any number of paths
# e.g. localhost:4567/more/asddf/asdf/asdf/asdf
get '/more/*' do
  params[:splat]
end

# loads the form.erb ERB (Embedded Ruby) file from a views/ directory.
get '/form' do
  erb :form
end

# this gets hit when browser calls POST on localhost:4567/form
# e.g. from hitting an <input type="submit">
#      in a <form action="/form" method="post">
post '/form' do
  "You said '#{params[:message]}'"
end

get '/secret' do
  erb :secret # looks for views/secret.erb
end

# print the reverse of the POSTed string
post '/secret' do
  params[:secret].reverse
end

get '/decrypt/:secret' do
  params[:secret].reverse
end

# override the default Sinatra 404 page
#       ("Sinatra doesn't know this ditty, try '...'")
# with our own hilarious custom content
not_found do
  halt 404, 'page not found'
end
