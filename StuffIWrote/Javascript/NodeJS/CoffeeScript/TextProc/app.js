var express = require('express');

// Nice API for dealing with file paths (e.g. `path.join(a,b)`)
var path = require('path');
var favicon = require('static-favicon');

// http request logger middleware for node.js
// doesn't seem to be in use anywhere at this point
// it belongs to expressjs
var logger = require('morgan');

// these are pieces of the expressjs framework
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes/index');
var users = require('./routes/users');

// instantiate an ExpressJS application
var app = express();

// set location of views
app.set('views', path.join(__dirname, 'views'));

// inform app of templating engine type
app.set('view engine', 'jade');

app.use(favicon());
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded());
app.use(cookieParser());
app.use(require('stylus').middleware(path.join(__dirname, 'public')));

/* The static middleware has the same effect as creating a route for
   each static file you want to deliver that renders a file and returns
   it to the client.

   E.g., now we can simply reference "/img/logo.png" (do not specify
   public), and the static middleware will serve that file, setting
   the content type appropriately. */
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);
app.use('/users', users);

/// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

/// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
        message: err.message,
        error: {}
    });
});

console.log('Server started on localhost:3000; press Ctrl-C to terminate....');

module.exports = app;
