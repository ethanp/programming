Notes on Scala
==============

Implicit Variables and Parameters
---------------------------------

Must be last variable list

[This](http://www.drmaciver.com/2008/03/an-introduction-to-implicit-arguments/)
article spells them out quite simply, and the following example is based on theirs.

    scala> def foo(s: String)(implicit t: String) = s"$s $t"

    scala> foo("a")("b")
    res0: String = a b

    scala> implicit val w = "it's scary!"
    scala> foo("Implicit variables:")
    res1: String = Implicit variables: it's scary!

