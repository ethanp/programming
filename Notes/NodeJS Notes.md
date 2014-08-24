latex input:        mmd-article-header
Title:              NodeJS Notes
Author:             Ethan C. Petuchowski
Base Header Level:  1
latex mode:         memoir
Keywords:           Node.js, Express.js, Web, Web Framework
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
Copyright:          2014 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

Many of these notes come from *Web Development with Node and Express*, by Ethan Brown.

# NodeJS

## What is NodeJS?

1. A web server like Apache
2. *Minimal*, meaning easy to set up and configure
3. Single threaded
    * If you need multithreading, spin up more instances of Node
4. Uses Google's V8 JavaScript Engine to JIT compile JavaScript to native machine code
    * But there is no separate compile step, your changes are automatically available
      when you save a new version
5. Interfaces are available for all major relational and NoSQL databases
6. Different from a web server like Apache: the app you write *is* the web server

## How does Nginx fit in here?

For larger projects, one will probably want to use Nginx as a
*"proxy server"* that routes requests to either a cache or different
app instances.

## Intro NodeJS

1. `helloWorld.js`

        var http = require('http');        http.createServer(function(req,res){          res.writeHead(200, { 'Content-Type': 'text/plain' });          res.end('Hello world!');        }).listen(3000);        console.log('Server started on localhost:3000; press Ctrl-C to terminate....');

    Now type `node helloWorld.js` and go to `http://localhost:3000`
2. `__dirname` resolves to the directory the executing script resides in.
   So if your script resides in `/home/sites/app.js`, `__dirname` will
   resolve to `/home/sites`.
    * It’s a good idea to use this handy global whenever possible.
      Failing to do so can cause hard-to-diagnose errors if you run
      your app from a different directory.
3. Requiring files
    * To look among `npm` packages (`global` or in `'./node_modules'`)
    
            var express = require('express')
    * To look in current directory
    
            var fortune = require('./lib/fortune.js')
4. [`module.exports`][modexp] is the object that's actually returned as the result of
   a `require` call. ([StOve][stove modexp])
    * x.js:
        
            setTimeout(function() {
              module.exports = { a: "hello" };
            }, 0);
    * y.js:
            
            var x = require('./x');
            console.log(x.a);
   
[modexp]: http://nodejs.org/api/modules.html#modules_module_exports
[stove modexp]: http://stackoverflow.com/questions/5311334

### Request Object

1. **Parameters** can come from
    1. querystring
    2. session (cookies)
    3. request body (POST)
    4. named routing parameters (`'/:name'`)
   
   and the `req.param` method of the request object munges all of these
   parameters together. For this reason, one should avoid it, and instead
   use dedicated properties that hold the various types of parameters
   added by Express.

### App Clusters

1. A simple single-server form of "scaling-out" (having multiple app instances)
2. You can create an independent server for each core on your system
3. This is both faster, and allows you to test your app under parallel conditions


#### How to

Change the `app.js` file to have

    function startServer() {
      http.createServer(app)
        .listen(app.get('port'), function() {
          console.log('Express started in ' + app.get('env') +
            ' mode on http:// localhost:' + app.get('port') +
            '; press Ctrl-C to terminate.');
        });
    }
    if (require.main === module) {
      // application run directly; start app server
      startServer();
    } else {
      // application imported as a module via "require":
      // export function to create server
      module.exports = startServer;
    }

and create a new script `app_cluster.js`
    
    var cluster = require('cluster');
    
    function startWorker() {
      var worker = cluster.fork();
      console.log('CLUSTER: Worker %d started', worker.id);
    }
    if (cluster.isMaster) {
      require('os')
        .cpus()
        .forEach(function() {
          startWorker();
        });
      // log any workers that disconnect; if a worker disconnects, it
      // should then exit, so we'll wait for the exit event to spawn
      // a new worker to replace it 
      cluster.on('disconnect', function(worker) {
        console.log(
          'CLUSTER: Worker %d disconnected from the cluster.',
          worker.id);
      });      
      // when a worker dies (exits), create a worker to replace it 
      cluster.on('exit', function(worker, code, signal) {
        console.log('CLUSTER: Worker %d died with exit code %d (%s)',
          worker.id, code, signal);
        startWorker();
      });
    } else {
      // start our app on worker; see app.js 
      require('./app.js')();
    }

Now start up your new clustered server:

    node meadowlark_cluster.js

If you want to see evidence of different workers handling different
requests, add the following middleware before your routes:

    app.use(function(req, res, next) {
      var cluster = require('cluster');
      if (cluster.isWorker) {
        console.log('Worker %d received request', cluster.worker.id);
      }
    }); 
    
Now you can connect to your application with a browser. Reload a
few times, and see how you can get a different worker out of the
pool on each request.

## Hooking in MongoDB

1. "While there’s a low-level driver available for MongoDB , you’ll
   probably want to use an **'object document mapper' (ODM)**. The officially
   supported ODM for MongoDB is **`Mongoose`**."

# ExpressJS

## What is ExpressJS?

1. A web framework (a bit) like Ruby on Rails
    * Not the *only* web framework for Node, but pretty dominant right now
2. Written by the one and only TJ Holowaychuk
3. Default **templating engine** is (TJ Holowaychuk's) **`Jade`**

## Intro ExpressJS

1. Here is an example route

        app.get('/ab', function(req, res){
          res.type('text/plain');          res.send('Meadowlark Travel');        });

    This says,
    
     1. Route any HTTP `GET` requests to e.g.
  
         * `/ab`
         * `/ab/`
         * `/ab?cd=ef`
         * `/ab/?cd=ef`
     
        to the provided function.
     2. Set the `Content-Type` header to `'text/plain'`
     3. `end` the `response` by putting
        the given text through the wire over TCP
2. `app.use` adds *"middleware"* to Express
3. In Express, the order in which routes and middleware are added is significant.
    * Specifically, if we put the `404` handler above the `routes`,
      the `home` page and `About` page would stop working: instead,
      those URLs would result in a `404`.
4. To set the route to render a template rather than sending plaintext, we have

        router.get('/', function(req, res) {
          res.render('index', { title: 'Express' });
        });
    which also sets the value of the variable `title` for use inside the view
5. The static middleware has the same effect as creating a route for
   each static file you want to deliver that renders a file and returns
   it to the client.
    * E.g., now we can simply reference `"/img/logo.png"` (do not specify
      public), and the static middleware will serve that file, setting
      the content type appropriately.

            app.use(express.static(path.join(__dirname, 'public')));
5. By default, Express looks for *views* in the `views/` directory
    * And *layouts* in `views/layouts/`
6. 


### Request/Response object

1. Requests start as one of Node's `http.IncomingMessage` objects, but a bunch of methods are added
2. Similarly, responses start as one of Node's `http.ServerResponse` objects.
3. `res.send(body)`, `res.send(status, body)`
    1. defaults to a content type of `text/html` so if you want to change it,
       call `res.set('Content-Type', 'text/plain')` or `res.type('txt')`
       before `res.send`
    2. If `body` is an `object` or `array`, the response is sent as `JSON`
        * Though you *should **explicitly*** send `JSON` using `res.json(json)`
4. Use `res.query` to get querystring values, `req.session` to get session
   values, or `req.cookie`/`req.signedCookies` to get cookies.
5. Use `res.render` to render a view within a layout


### Middleware

1. Middleware is a function that takes three arguments:
    1. Request object
    2. Response object
    3. "Next" function
2. Executed in a *pipeline*
    1. Order matters
    2. Things added by one middleware are available to everyone downstream
3. Insert middleware into the pipeline with `app.use`
4. Call the `next()` function to invoke the next function in the pipeline,
   otherwise the request will terminate
   
        app.use(function(req, res, next) { 
          console.log('processing request for "' + req.url + '"....');
          next();
        });
        
        
        // you probably don't want to (accidentally) do the following
        app.use(function(req, res, next) {
          console.log('terminating request');
          res.send('thanks for playing!');
          // note that we do NOT call next() here...
          // this terminates the request
        });
5. You could add middleware on specific requests using `app.VERB`

        app.get('/b', function(req, res, next) {
          console.log('/b: route not terminated');
          next();
        });


# npm

## What is npm

1. Node's amazing ubiquitous package manager
2. Stands for "npm is not an acronym" (??)
3. Allows you to switch *"environments"*, so different projects can
   use different versions of the same packages

## How To

1. Install a package *globally* (make it available to your whole system)

        npm install -g grunt-cli
        
2. Save the package(s) in `node_modules/` *and* update the `package.json` file

        npm install --save express
        
3. Save put the package in `devDependencies` instead of `dependencies` to
   reduce dependencies required to deploy (e.g. for modules related to *testing*)
   
        npm install --save-dev mocha
