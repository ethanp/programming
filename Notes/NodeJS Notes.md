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