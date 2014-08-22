VENDOR = "vendor"
requirejs.config
  paths:
    'jquery': "#{VENDOR}/jquery.min"
    'backbone': "#{VENDOR}/backbone.min"
    'underscore': "#{VENDOR}/underscore.min"
    'jade': "#{VENDOR}/jade.min"
  shim:
    'jquery': exports: '$'
    'backbone':
      deps: ['underscore', 'jquery']
      exports: 'Backbone'
    'underscore': exports: '_'
    'jade': exports: 'jade'

requirejs ['jquery'], ($) ->
  console.log 'Edit me in client/coffee/main.coffee.'
