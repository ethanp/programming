var koa = require('koa');
var jade = require('jade');
var mongel = require('mongel');
var parse = require('co-body');
var colors = require('colors');

var app = koa();

// our connection to mongodb
var collection = 'vertices';
var db = 'readit'
var Vertex = mongel(collection, 'mongodb://localhost/'+db);

var app = koa();

/**
 * basic structure here is with help from
 *
 * packtpub.com/books/content/how-to-build-a-koa-web-application-part-1
 *
 * the app still doesn't work though so next, I'm going to look at
 *
 * github.com/Elzair/koa-mongodb-example/blob/master/package.json
 */

// `next` represents the "next middleware on the stack (if any)"
// the `*` means we're creating a (newfangled) Javascript "generator"
app.use(function *(next){

    // parameters of the HTTP request are available here in Koa's
    // function context
    if (this.path != '/create') {
        // not the endpoint we have in mind, so pass
        // the request to the next middleware
        yield next;
        // if we don't `return`, we'll come back here and end up
        // executing all the rest of the code
        return
    }

    if (this.method == 'POST') {
        // we call `yield` because parse.form() is async
        // and we must wait for it
        var body = yield parse.form(this);

        // again we yield because it is async
        var vertex = yield Vertex.createOne({
           title: body.title,
           contents: body.contents
        });
        this.redirect('/' + vertex._id);
        return
    }
    else if (this.method != 'GET') {
        this.status = 405;
        this.body = 'Method Not Allowed';
        return
    }
});

app.use(function* () {
    if (this.method != 'GET') {
        this.status = 405;
        this.body = 'Method Not Allowed';
        return
    }
    var params = this.path.split('/').slice(1);
    var id = params[0];
    if (id.length == 0) {
        var vertices = yield Vertex.find();
        var html = jade.renderFile('list.jade', { vertices: vertices });
        this.body = html;
        return
    }
    else {
        var page = yield Vertex.findById(id);
        var html = jade.renderFile('view.jade', page);
        this.body = html;
    }
});

// the app does NOT block here
app.listen(3000);

// this still gets printed
console.log('Hello, world!'.green);
