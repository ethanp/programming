Notes on Scala
==============

Futures and Promises
--------------------

3/1/14

From the [Scala Docs](http://docs.scala-lang.org/overviews/core/futures.html)

* **A `Future` is a sort of a placeholder object that you can create for a result that does not exist yet**.
* Generally, the result of the Future is computed concurrently and can be later collected.
* They use callbacks instead of blocking operations
* Futures *can* be blocked on when absolutely necessary
* Futures **can only be assigned *once***

Create a `Future[T]` with the `future` method

    import scala.concurrent._
    val f: Future[List[Friend]] = future {
        session.getFriends()
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

Must be last variable list

[This](http://www.drmaciver.com/2008/03/an-introduction-to-implicit-arguments/)
article spells them out quite simply, and the following example is based on theirs.

##### E.g.

    scala> def foo(s: String)(implicit t: String) = s"$s $t"

    scala> foo("a")("b")
    res0: String = a b

    scala> implicit val w = "it's scary!"
    scala> foo("Implicit variables:")
    res1: String = Implicit variables: it's scary!


Iterator.collect[B](pf: PartialFunction[A,B|)
----------------------------------------------

#### A convenient way to **simultaneously map and filter**.

The PartialFunction parameter means you can use a *Pattern Matching Anonymous Function* as the argument

##### E.g.
  
    scala> (1 to 10) collect { 
        case x if x % 2 == 0 => -x
        case 1 => 11 
    }
    res0: Vector(11, -2, -4, -6, -8, -10)
    
