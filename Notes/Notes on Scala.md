Notes on Scala
==============

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
    
