var logger = require('koa-logger');
var route = require('koa-route');
var koa = require('koa');
var app = module.exports = koa();

app.use(logger());

var routes = require('./routes.js');

app.use(route.get('/', routes.list));
app.use(route.get('/post/new', routes.add));
app.use(route.get('/post/:id', routes.show));
app.use(route.post('/post', routes.create));
app.use(route.post('/post/:id', routes.update));
app.use(route.get('/post/:id/edit', routes.edit));
app.use(route.get('/post/:id/delete', routes.remove));

// the ones I've added
app.use(route.get('/reply/new/:parentID', routes.addReply));
app.use(route.post('/reply/:parentID', routes.createReply));

app.listen(3000); // does _not_ block
console.log('listening on port 3000');
