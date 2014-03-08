Notes on Scala
==============

Futures and Promises
--------------------

3/1/14

From the [Scala Docs](http://docs.scala-lang.org/overviews/core/futures.html)

### Intro

* **A `Future` is a sort of a placeholder object that you can create for a result that does not exist yet**.
* Generally, the result of the Future is computed concurrently and can be later collected.
* They use callbacks instead of blocking operations
* Futures *can* be blocked on when absolutely necessary
* Futures **can only be assigned *once***

### Basics

Create a `Future[T]` with the `future` method

    import scala.concurrent._
    import ExecutionContext.Implicits.global
    
    val f: Future[List[Friend]] = future {
        session.getFriends()
    }
    
We import the `global ExecutionContext` to give us access to the global thread-pool

We may also want to use a `Future` for tasks involving I/O

    val firstOccurrence: Future[Int] = future {
      val source = scala.io.Source.fromFile("myText.txt")
      source.toSeq.indexOfSlice("myKeyword")
    }

* **We can wait** for the `Future` to arrive and then use it
* **Or we can register for a callback** to be performed asynchronously when the `Future` arrives

`onComplete` takes a function of type `Try[T] => U`, where `Try[T]` is similar to `Option[T]` in that it is a monad that can either hold a *value* or an *exception*, where the exception is of type `Throwable`

    f onComplete {
      case Success(posts) => for (post <- posts) println(post)
      case Failure(t) => println("An error has occurred: " + t.getMessage)
    }


Use `onSuccess` instead if you only want to handle successful results, and `onFailure` to handle failed results.

    f onSuccess {
      case posts => for (post <- posts) println(post)
    }

#### Registering multiple callbacks

* Multiple callbacks registered on the same future are *unordered*, they may even execute concurrently
* The callback is not necessarily called by the thread that completed the future *or* the thread which created the callback
* In the event that some of the callbacks throw an exception, the other callbacks are executed regardless
* Once executed, the callbacks are removed from the future object, thus being eligible for garbage collection

### For Comprehensions / Combinators

* Futures can be chained with the standard "combinators": `map, flatMap, filter, foreach, collect`
* The exceptions work nicely in this situation: which ever chained Future threw the exception gets to declare the type of the exception
* These combinators allow use to use *for-comprehensions*

In the following example, the `purchase` Future is only computed both required Futures have completed.

    val usdQuote = future { connection.getCurrentValue(USD) }
    val chfQuote = future { connection.getCurrentValue(CHF) }
    
    val purchase = for {
      usd <- usdQuote
      chf <- chfQuote
      if isProfitable(usd, chf)
    } yield connection.buy(amount, chf)
    
    purchase onSuccess {
      case _ => println("Purchased " + amount + " CHF")
    }
    
If we want our future to contain 0 instead of the exception, we use the `recover` combinator:

    val purchase: Future[Int] = rateQuote map {
      quote => connection.buy(amount, quote)
    } recover {
      case QuoteChangedException() => 0
    }

Combinator `fallbackTo` creates a new future which holds the result of *this* future if it was completed successfully, or otherwise the successful result of the *argument* future. In the event that both this future and the argument future fail, the new future is completed with the exception from *this* future.

    val usdQuote = future {
      connection.getCurrentValue(USD)
    } map {
      usd => "Value: " + usd + "$"
    }
    
    val chfQuote = future {
      connection.getCurrentValue(CHF)
    } map {
      chf => "Value: " + chf + "CHF"
    }
    
    val anyQuote = usdQuote fallbackTo chfQuote
    
    anyQuote onSuccess { println(_) }
    
The `andThen` combinator is used purely for side-effecting purposes. Multiple `andThen` calls are ordered.

    val allposts = mutable.Set[String]()
    future {
      session.getRecentPosts
    } andThen {
      posts => allposts ++= posts
    } andThen {
      posts =>
      clearAll()
      for (post <- allposts) render(post)
    }


### The "Failed" projection

If the original future fails, the `failed` projection returns a future containing a value of type `Throwable`. If the original future succeeds, the `failed` projection fails with a `NoSuchElementException`. The following is an example which prints the exception to the screen:

    val f = future {
      2 / 0
    }
    for (exc <- f.failed) println(exc)
    
The following example does not print anything to the screen:

    val f = future {
      4 / 2
    }
    for (exc <- f.failed) println(exc)


### Intentionally Blocking

Here is an example of how to block on the result of a future:

    import scala.concurrent._
    import scala.concurrent.duration._
    
    def main(args: Array[String]) {
      val rateQuote = future {
        connection.getCurrentValue(USD)
      }
      
      val purchase = rateQuote map { quote =>
        if (isProfitable(quote)) connection.buy(amount, quote)
        else throw new Exception("not profitable")
      }
      
      Await.result(purchase, 0 nanos)
    }

In the case that the future fails, the caller is forwarded the exception that the future is failed with.

Alternatively, calling `Await.ready` waits until the future becomes completed, but does not retrieve its result. In the same way, calling `Await.ready` will not throw an exception if the future is failed.

The `Future` trait implements the `Awaitable` trait with methods method `ready()` and `result()`. These methods **cannot** be called directly by the clients– they can only be called by the execution context.

To allow clients to call 3rd party code which is potentially blocking and avoid implementing the `Awaitable` trait, the same `blocking` primitive can also be used in the following form:

    blocking {
      potentiallyBlockingCall()
    }

The blocking code may also throw an exception. In this case, the exception is forwarded to the caller.


### Promises

* A `promise` can be used to successfully complete a `future` with a value (by “completing” the promise) using the `success` method. 
* Conversely, a `promise` can also be used to complete a `future` with an exception, using the `failure` method.

Read my comments to understand what's going on

    import scala.concurrent.{ future, promise }
    import scala.concurrent.ExecutionContext.Implicits.global

    val p = promise[T]
    val f = p.future                      // get the future from *inside* of promise p

    val producer = future {
      val r = produceSomething()
      p success r                         // assign f the value r
      continueDoingSomethingUnrelated()   // f has already been assigned and can
    }                                     //     be used while this completes

    val consumer = future {
      startDoingSomething()
      f onSuccess {
        case r => doSomethingWithResult() // consumer notes that f was assigned,
      }                                   //   and can proceed to "do something"
    }

To `fail` the `promise` instead, use `p failure (new MyExceptionException)` instead of `p success r`.

> One nice property of programs written using promises with operations described so far and futures which are composed through monadic operations without side-effects is that these programs are deterministic. Deterministic here means that, given that no exception is thrown in the program, the result of the program (values observed in the futures) will always be the same, regardless of the execution schedule of the parallel program.

The method `completeWith` completes the promise with another future. After the future is completed, the promise gets completed with the result of that future as well. The following program prints `1`:

    val f = future { 1 }
    val p = promise[Int]

    p completeWith f

    p.future onSuccess {
      case x => println(x)
    }



Case Class vs. Regular Class
----------------------------

2/23/14

From [Stack Overflow](http://stackoverflow.com/questions/5270752/difference-between-case-object-and-object)

Case classes just add to classes, they don't take away. They give you:

1. **pattern matching support**
2. default implementations of `equals` and `hashCode`
3. default implementations of serialization
4. a prettier default implementation of `toString`, and
5. the small amount of functionality that they get from automatically inheriting from `scala.Product`.

Implicit Variables and Parameters
---------------------------------

#### Defining an implicit val makes it available to future uses of implicit parameters

Must be last argument to function's last set of arguments

[This](http://www.drmaciver.com/2008/03/an-introduction-to-implicit-arguments/)
article spells them out quite simply, and the following example is based on theirs.

##### E.g.

    scala> def foo(s: String)(implicit t: String) = s"$s $t"

    scala> foo("a")("b")
    res0: String = a b

    scala> implicit val w = "it's scary!"
    
    scala> foo("Implicit variables:")
    res1: String = Implicit variables: it's scary!


Iterator.collect[B]\\(pf: PartialFunction[A,B|)
----------------------------------------------

### A convenient way to **simultaneously map and filter**.

The `PartialFunction` parameter means you can use a *Pattern Matching Anonymous Function* as the argument,
and if none of your `cases` match an element, that element gets filtered

#### E.g.
  
    scala> (1 to 10) collect { 
        case x if x % 2 == 0 => -x
        case 1 => 11 
    }
    res0: Vector(11, -2, -4, -6, -8, -10)
    