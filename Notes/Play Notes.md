Notes on Playframework 2
========================

Asynchronous Results
--------------------

**4/26/14**

[Play Docs: Handling asynchronous results](http://www.playframework.com/documentation/2.2.x/ScalaAsync)

### Why?

**The web client will be blocked while waiting for the response, but nothing will be blocked on the server, and server resources can be used to serve other clients.**

### How?

    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    
    def index = Action.async {
      val futureInt = scala.concurrent.Future { intensiveComputation() }
      futureInt.map(i => Ok("Got result: " + i))
    }


#### Now with timeouts

    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    import scala.concurrent.duration._
    
    def index = Action.async {
      val futureInt = scala.concurrent.Future { intensiveComputation() }
      val timeoutFuture = play.api.libs.concurrent.Promise.timeout("Oops", 1.second)
      Future.firstCompletedOf(Seq(futureInt, timeoutFuture)).map {
        case i: Int => Ok("Got result: " + i)
        case t: String => InternalServerError(t)
      }
    }

Iteratees
---------

**3/22/14**

#### [Play Docs: Iteratees](http://www.playframework.com/documentation/2.0.4/Iteratees)

* Iteratees allow a user to create, consume, and transform streams of data asynchronously
* An Iteratees is a *consumer*, it describes how to consume input to produce some value
* It returns the value it computes after being fed enough input
* `Iteratee[E,A]` has two type parameters, *input* and *output*
* It has 3 possible states
    * `Cont` -- still accepting input ("Continuation")
    * `Error` -- encountered an error
    * `Done` -- contains the result

##### Some types

* Let's call the input type `Input[E]`
* This chunk of input can be
    * `El[E]` -- some actual input
    * `Empty` -- nothing to input at this time, but hold on for more
    * `EOF` -- nothing left at all
* So `Input[String]` might be `El("Hello")`, `Empty`, or `EOF`
* Functions describing what the `Iteratee[E,A]` does in each state all return type `Promise[B]`
    * This means you can register to retrieve their value of type `B` in a callback
    
##### Some Code

Here is an untested example usage based on [PlayFramework's ChatRoom Example]
(github.com/playframework/playframework/blob/samples/scala/websocket-chat/app/models/ChatRoom.scala)

    def chatSocket(username: String) = WebSocket.async[JsValue] { request =>
      Future[(Iteratee[JsValue,_],Enumerator[JsValue])](
        Done[JsValue,Unit]((),Input.EOF),
        Enumerator[JsValue](JsObject(
          Seq("error" -> JsString("a sample error")))
        ).andThen(Enumerator.enumInput(Input.EOF))
      )
    }

Here's what I believe is going on here:

Return a JSON `Future` with some default values.
We're returning a `Future` --- a computation that will return a value
in the Future, the event of which we can register to be notified about.
The `Future` is type-parameterized by a `Tuple` containing

1. an `Iteratee` (an object that can either be `Cont`, `Done`, or `Error`,
   depending on whether it is ready to continue consuming input).
   The `Iteratee` is parameterized by the types `[JsValue,_]`, meaning
   that it *accepts* `JsValues`, and can return `Promises` of
   anything (I think)

2. an `Enumerator` --- "a source that pushes input into a given `Iteratee`".
   Here, the `Enumerator` contains type `[JsValue]`, so we are passing it first
   a `JsObject` (which [I think] is a `JsValue` that contains arbitrary JSON),
   which we are constructing to be `{ error : "a sample error" }`.
   Then we are adding a second item to the `Enumerator`, the `EOF`, which
   (I think) tells the `Iteratee` to put itself into the `Done` state.

So, overall, we are constructing a `JSON` message to pass into the `WebSocket`.

[Better Tutorial on Iteratees]
(http://blog.higher-order.com/blog/2010/10/14/scalaz-tutorial-enumeration-based-io-with-iteratees/)


Templates
---------
### March 21, 2014
#### From `play-websockets-chat-sample`, a GitHub project I've forked

To dynamically show a link to a URL connected to your `routes` file, use

    <a class="brand" href="@routes.Application.index()"> Link-Text </a>
    
I believe this is what they refer to as "reverse routing".


#### JavaScript template

#####To pull in a JavaScript source inside a Scala HTML template file

1) Write

    <script type="text/javascript" charset="utf-8" src="@routes.Application.chatRoomJs(username)"></script>

2) Then in the `Application (Controller)` put

    public static Result chatRoomJs(String username) {
        return ok(views.js.chatRoom.render(username));
    }
 
Which I believe translates to having the following method in an `object Application`

    def chatRoomJs(username: String) = Ok(views.js.chatRoom.render(username))
    

3) Then in the `routes` file, put

    GET  /assets/javascripts/chatroom.js  controllers.Application.chatRoomJs(username)
    
4) Then in `app/views/chatRoom.scala.js`, put

    @(username: String)
    $(function() { /* jQuery for days */ })

I believe at this point, *Play!* will dynamically serve `/assets/javascripts/chatroom.js`
when asked for it, so you don't need to actually create a real file
at that location.

Routes Files
------------
### March 21, 2014
#### From `play-websockets-chat-sample`, a GitHub project I've forked

* Matches are tried from the top down
* To put a query param on a `GET`, add a param to the function call

Example of `GET` **Query Parameter**

    GET  /room  controllers.Application.chatRoom(username: String ?= null)
    
This will make it so that the `GET` request URL will be

    http://localhost:9000/room?username=the+thing+I+typed
    
But the `?= null` means that if you send nothing, via

    http://localhost:9000/room?username=
    
you will pass `null` to `chatRoom`, which in this case will render a
    
    if(username == null) {
        flash("error", "Please choose a valid username.");
        return redirect(routes.Application.index());
    }
    

Architecture
------------
### February 2014
#### From `Play for Scala` (Manning)

A few perspectives from which one might like to consider how
to architect the website:

1. Data Model -- what are the tables, schemas, DBs, and APIs?
1. HTTP Interface -- what will the resulting `Routes` file look like?
    * Note that shorter URLs are better because they're more usable 
1. User Interface -- what will the resulting website *feel* like?


Form Object
---------
### February 2014
#### From `Play for Scala` (Manning) § 2.3.2 (@15%)

##### Listing 2.27

    private val productForm: Form[Product] = Form(      mapping(        "ean" -> longNumber.verifying(          "validation.ean.duplicate", Product.findByEan(_).isEmpty),        "name" -> nonEmptyText,        "description" -> nonEmptyText      )(Product.apply)(Product.unapply)    )

> A form consists of a mapping together with two functions
> that the form can use to map between itself and an instance
> of our Product model class. 

> The first part of the mapping 
> specifies the fields and how to validate them. There are 
> several different validations, and you can easily add your 
> own. 

> The second and third parts of the mapping are the 
> functions the form will use to create a Product model 
> instance from the contents of the form and fill it from
> an existing Product, respectively.

> Our form’s fields map
> directly to the Product class’s fields, so we simply use 
> the `apply` and `unapply` methods that the Scala compiler 
> generates for case classes. If you’re not using case 
> classes or there’s no one-to-one mapping between the case 
> class and the form, you’ll have to supply your own 
> functions here.


Flash Scope
-----
### February 2014
#### From `Play for Scala` (Manning)

* Like session scope, keeps data related to a client, 
  outside the context of a single request.
* Difference is that it is removed after the next request
* Implemented in Play as a cookie cleared on every response
    * So is Session info 
    * This is so different servers can handle the same client's
      request, and still be able to deal properly with both flash
      and session information.
          * This allows you use a cluster of servers in a round-robin.
