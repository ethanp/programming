should = require('chai').should()
request = require 'request'
qs = require 'querystring'

APP_ROOT = 'http://localhost:5000'

# Note: Tests require for the app to be running on port 3000.
describe 'the public controller', ->
  
  it 'should respond with a 200 at index', (done) ->
    request.get "#{APP_ROOT}", (error, response, body) ->
      response.statusCode.should.equal 200
      done()
