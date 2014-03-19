Integrating PlayFramework with Akka
-------------------------------------

3/1/14

From the [Play Docs](http://www.playframework.com/documentation/2.0/ScalaAkka)

Play apps have their own default actor system. Use it as a Factory via

    import play.libs.concurrent.Akka
    val myActor = Akka.system.actorOf(Props[MyActor], name = "myactor")

Add these lines to `conf/application.conf`

    akka.default-dispatcher.core-pool-size-max = 64
    akka.debug.receive = on


A "Hello Akka" set of notes, Written by Me
------------------------------------------

3/1/14

### Based on `HelloAkkaScala.scala`

* Define a `case object Greet` when you want to have a message that an `Actor` can `receive` that doesn't contain any of its own information
* Define a `case class Greeting(message: String)` when you want to have a message that has its own information (in this case, a `message: String`)
* Define an `Actor` via `class Greeter` **`extends Actor`**
    * Add member variables (e.g. `var greeting = ""`)
    * Define the `receive` method using `case Type(?param) => do_something`
        * Access the `sender`'s `ActorRef` using `sender`
        
#### ! == tell()

* **The *bang* (`!`) operator** calls the `tell` method
    * `who_to_send_to ! what_to_send`
    * (At least) when used from within an `Actor`'s class definition, it implicitly passes them the `ActorRef` of the `Actor` being defined
    * Use the `tell` method explicitly when you want to specify the `ActorRef` to pass to the `receive`r

E.g. to get the `Actor` to `receive` the `msg` with *no* sender `ActorRef`, use
    
    actor_Im_telling.tell(msg=ThingTheyWillMatchOn, sender=ActorRef.noSender)

#### ActorSystem

* Creates and manages the actors
* Has a `name` parameter in the constructor, not sure why, maybe just for logging

Constructor usage example:

    val system = ActorSystem(name="Hello_Akka")

Now we can use the ActorSystem's 'factory' to create an instance of a class that `extends Actor` (e.g. `Greeter`)

    val greeter: ActorRef = system.actorOf(props=Props[Greeter], name="greeter")

Again, I'm not sure what the `name` is useful for, maybe just for logging.

#### Inbox

* An actor with no specific implementation details
* It can `send` a message to an `Actor`

        inbox.send(target=greeter, msg=greet)
        
* It can `receive` the reply from that `Actor` with a *timeout*

        val Greeting(message1: String) = inbox.receive(max=5.seconds)
        
* Useful when writing code outside of actors which shall communicate with actors

#### Scheduler

* Use this to repeatedly send a message to an actor every so often (forever, I guess?)

        system.scheduler.schedule(initialDelay=0.seconds, interval=1.second, receiver=greeter, message=Greet)(executor=system.dispatcher, sender=greetPrinter)

[Akka Scala Documentation](http://doc.akka.io/docs/akka/2.2.1/scala.html)
--------------------------

3/1/14

### [Akka Actors Docs](http://doc.akka.io/docs/akka/2.2.1/scala/actors.html)

####Send messages

Messages are sent to an Actor through one of the following methods.

1. `!` means **“fire-and-forget”**, e.g. send a message asynchronously and return immediately. **Also known as `tell`**.
2. `?` sends a message asynchronously and **returns a Future representing a possible reply**. **Also known as `ask`**.

Message ordering is guaranteed on a per-sender basis.

2/25/14

Using Akka with SBT:

    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
    libraryDependencies +=
         "com.typesafe.akka" %% "akka-actor" % "2.2.3"

Scala In Depth, Chp. 9: Actors
------------------------------

2/23/14

* An actor will process received messages sequentially in the order they are received, and only handle one message at a time.
* This fact is critical because it means that actors can maintain state without explicit locks. Actors can also be asynchronous or synchronous. Most actors will not block a thread when waiting for messages, although this can be done if desired. The default behavior for actors is to share threads among each other when handling messages. This means a small set of threads could support a large number of actors, given the right behavior.
* In fact, actors are great state machines. They accept a limited number of input messages and update their internal state. All communication is done through messages and each actor is standalone. * If the application needs to farm many similar tasks out for processing, this requires a large pool of actors to see any concurrency benefits.* Using an actor to perform blocking I/O is asking for trouble. That actor can starve other actors during this processing. 
* Rather than relying on MVC-based parallelism, an actor-based system parallelizes pieces of the architecture and performs all communication asynchronously

#### E.g: Using Actors for Search

This program has a set of documents that live in some kind of search index. Queries are accepted from users and the index is searched. Documents are scored and the highest scored documents are returned to the users. To optimize the query time, a scatter/gather approach is used.The *scatter/ gather approach involves two phases of the query: scatter and gather*.
The first phase, *scatter*, is when **the query is farmed out to a set of subnodes**. Classically, these subnodes are divided topically and store documents about their topic. These nodes are responsible for finding relevant documents for the query and returning the results.
The second phase, *gather*, is when all the **topic nodes respond to the main node with their results**. These are then pruned and returned for the entire query.
From the Akka-Spray Tutorial
----------------------------

2/23/14

This tutorial application will:

* Receive HTTP requests with JSON payloads
* Unmarshal the JSON into `case classes`
* Send these instances to `Actor`s for processing
* Marshal the `Actor`'s responses into JSON
* Use that to construct HTTP responses

It will also use the "Cake" pattern, which enable us to separate out parts of the system so that I can "assemble" the parts of the cake into the components that I ultimately run or test.

### [Spray](http://spray.io/) (an interesting aside)

* An open-source toolkit for building REST/HTTP-based integration layers on top of Scala and Akka.
* Comes with a small, embedded and super-fast HTTP server (called spray-can) that is a great alternative to servlet containers. spray-can is fully asynchronous and can handle thousands of concurrent connections. It also supports request and response streaming, HTTP pipelining and SSL. And: there is an HTTP client to go with it.
* Elegant DSL for API construction promotes RESTful style as well as a clean separation of the various application layers.
* All APIs are fully asynchronous, blocking code is avoided wherever at all possible.
* Akka Actors and Futures are key constructs of its APIs.
* Being *modular*, your application only needs to depend onto the parts that are actually used.

##### Spray's Philosophy

* Regards itself as a suite of libraries rather than a framework.
* A framework is like a skeleton onto which you put the “flesh” of your application in order to have it come alive.

For example, if you are building a browser-facing web application it makes sense to choose a web framework and build your application on top of it because the “core” of the application is the interaction of a browser with your code on the web-server. The framework makers have chosen one “proven” way of designing such applications and let you “fill in the blanks” of a more or less flexible “application-template”. Being able to rely on best-practice architecture like this can be a great asset for getting things done quickly.

However, if your application is not primarily a web application because its core is not browser-interaction but some specialized maybe complex business service and you are merely trying to connect it to the world via a REST/HTTP interface a web-framework might not be what you need. In this case the application architecture should be dictated by what makes sense for the core not the interface layer. Also, you probably won’t benefit from the possibly existing browser-specific framework components like view templating, asset management, JavaScript- and CSS generation/manipulation/minification, localization support, AJAX support, etc.

`spray` is made for building integration layers based on HTTP and as such tries to “stay on the sidelines”. Therefore you normally don’t build your application “on top of” `spray`, but you build your application on top of whatever makes sense and use `spray` merely for the HTTP integration needs.

### Back to the Tutorial

* Our application has a `HttpServer`, which talks to our `API`'s `RootService`, which talks to the `Core` Logic.
* I'm not really at a level to understand what he's doing

Notes from the Hello-Akka Tutorial
----------------------------------

2/23/14

### Notes from the Tutorial itself

Some of these notes only apply to using Akka with Scala, but aside from syntax, it's basically the same between Scala and Java.

##### Akka is a toolkit and runtime for building highly concurrent, distributed, and fault-tolerant event-driven applications on the JVM.
* It **uses the Actor Model of concurrency**
* An Actor's public API is defined through messages that the actor handles.
* Messages can be of arbitrary type
* Define messages with good names and rich semantic and domain specific meaning, even if it's just wrapping your data type. This will make it easier to use, understand and debug.
* It is very important that the messages we create are immutable to avoid sharing state between two Actors
* Scala `case classes` and `case objects` make excellent messages since they are immutable and we can use *pattern matching* to locate messages it has received.
    * Another advantage case classes has is that they are marked as *serializable* by default.
* The Actor is the unit of execution.
* Actors are object-oriented in the sense that they encapsulate state and behavior, but they have much stronger isolation than regular objects
* **The Actor model prevents sharing state between Actors and the only way to observe another actor's state is by sending it a message asking for it.**
* Actors are extremely lightweight, they are only constrained by memory of which they consume only a few hundred bytes each — this means *you can easily create millions of concurrent Actors in a single application*.
* To create an Actor, you `extend Actor` trait, and implement the `receive` method
* The `receive` method is where you define how the Actor responds to different messages it can receive
* An Actor often has internal state
    * Mutating this state is thread safe, being protected by the Actor model
* In the (Scala only) receive method, you don't need to define a *default* case because that is passed to the `unhandled()` method for you by Akka
* You instantiate Actors using a factory
* The factory returns an `ActorRef` *pointing* to the Actor instance
    * The indirection here adds power and flexibility to
        * have the actor in-process or on a remote machine without changing anything.
        * And you can change where it is while it's running.
        * And it enables *"let it crash"*, in which the system heals itself and restarts faulty Actors.
* The Actor factory -- `ActorSystem` -- is also the Actor container, managing their life-cycles.

Here's an example
    
    val system = ActorSystem("helloakka")
    val greeter = system.actorOf(Props[Greeter], "greeter")

* `Props` is a *configuration object*, and you parameterize it with the Type of the Actor you want.
* `"greeter"` is the *name* of the Actor
    * Names are used to look-up and configure actors in the *configuration file*

Some important 10,000 ft. shit right here:

> All communication with Actors is done through asynchronous message passing. This is what makes Actors reactive and event driven. An Actor doesn't do anything unless it's been told to do something, and you tell it to do something by sending the message. Sending a message asynchronously means that the Actor hands the message off by putting it on the recipient's mailbox and is then free to do something more important than waiting for the recipient to react. The actor's mailbox is essentially a message queue and has ordering semantics, this guarantees that the order of multiple messages sent from the same Actor is preserved, while they can be interleaved with messages sent by another actor.

> When it is not processing messages, it is in a suspended state in which it does not consume any resources (apart from memory).

Use `tell()` on the `ActorRef` to pass a message to an Actor, i.e. put it in that Actor's mailbox and return immediately

    greeter.tell(WhoToGreet("akka"), ActorRef.noSender)

or, using the alias 'bang' operator

    greeter ! WhoToGreet("akka")
    
* For a request-reply type of communication instead of one-way, add a reference to yourself to your message to which the receiver will reply.
    * In every message you send, you have the option of doing this, and in Scala it is done all the time, *implicitly*

The receiver accesses the sender via `sender`

    sender ! Greeting(greeting)
           
* To truly leverage the Actor Model you should use lots of Actors. Every hard problem in Actor programming can be solved by adding more Actors; by breaking down the problem into subtasks and delegate by handing them to new Actors.
* `Inbox` contains an Actor which can be used as a puppet for sending messages to other Actors and receiving their replies.
* Create an `Inbox` using `Inbox.create` and send messages from it using `inbox.send`.
    * The internal Actor will just put any message it receives into a queue from which it can be retrieved by calling `inbox.receive`
        * If the queue is empty then that call will block until a message becomes available.
            * blocking is something that can really inhibit performance and scalability, and that you should use very sparingly and with care.
* The sample unit tests use `ScalaTest`, using the Akka `TestKit` module, which makes it so much easier to test and verify concurrent code.
