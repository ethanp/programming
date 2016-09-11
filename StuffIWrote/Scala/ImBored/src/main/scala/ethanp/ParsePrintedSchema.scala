package ethanp

import scala.collection.mutable

/**
  * 8/9/16 10:05 PM
  *
  * The point is to turn the inferred schema printed by Spark of JSON objects
  * into case classes that can be used easily later on
  */
object CaseClassGenerator {
    val exampleInput =
        """
          |root
          ||-- element: struct (containsNull = true)
          ||    |-- appName: string (nullable = true)
          ||    |-- appPackage: string (nullable = true)
          ||    |-- element: struct (containsNull = true)
          ||    |    |-- date: string (nullable = true)
          ||    |    |-- rating: long (nullable = true)
          ||-- id: string (nullable = true)"""
            .stripMargin

    def printObj(start: Line, it: Iterator[Line]): Line = {
        val cc = CaseClass(start.name.capitalize)
        for (l <- it) {
            cc.elements += Element(l.name, l.tipo.name)
            if (l.indent == start.indent + 1) {
                l.tipo match {
                    case Obj(name) => printObj(l, it)
                    case _ =>
                }
            }
        }
        println(cc)
        start
    }

    def main(argv: Array[String]): Unit = {
        val it: Array[Line] = exampleInput
            .split("\n")
            .filter(_.nonEmpty)
            .map(Line(_))
        println(it.mkString("\n"))
        val iterator = it.iterator
        printObj(iterator.next(), iterator)
    }
}

sealed trait LineType {
    def name: String
}
case class Obj(name: String) extends LineType
case object Str extends LineType {
    override def name: String = "String"
}
case object Num extends LineType {
    override def name: String = "Long"
}
case class Line(name: String, indent: Int, tipo: LineType)

object Line {
    def apply(s: String): Line = {
        val indent = s.count(_ == '|')
        if (indent == 0) {
            Line("Root", indent, Obj("Root"))
        } else {
            val name = findMatch("-\\ (.*):", s)
            Line(
                name = name,
                indent = indent,
                tipo = findMatch(":\\ ([^ ]+)", s) match {
                    case "struct" => Obj(name.capitalize)
                    case "string" => Str
                    case "long" => Num
                }
            )
        }
    }

    def findMatch(r: String, s: String): String = r.r.findFirstMatchIn(s).get.group(1)
}

case class CaseClass(name: String, elements: mutable.ListBuffer[Element] = mutable.ListBuffer.empty[Element]) {
    override def toString: String = s"case class $name(${elements.mkString(", ")})"
}

case class Element(name: String, tipo: String) {
    override def toString: String = s"$name: $tipo"
}
