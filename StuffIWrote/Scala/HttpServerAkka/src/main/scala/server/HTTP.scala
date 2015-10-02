package server

import scala.collection.mutable

/**
 * Ethan Petuchowski
 * 9/30/15
 */
object HTTP {

    val CRLF = "\r\n"

    sealed trait Method
    object Method {
        def parse(str: String) = Methods.find(_.getClass.getSimpleName.init.equalsIgnoreCase(str)).get
    }
    sealed trait Idempotent
    sealed trait HasBody
    case object Get extends Method with Idempotent
    case object Head extends Method with Idempotent
    case object Options extends Method with Idempotent
    case object Put extends Method with Idempotent
    case object Delete extends Method with Idempotent
    case object Trace extends Method with Idempotent
    case object Post extends Method with HasBody
    val Methods = Seq(Get, Head, Options, Put, Delete, Trace, Post)

    class Headers(val headers: mutable.Map[String, String] = mutable.Map.empty[String, String]) {
        def addPair(pair: (String, String)): Unit = {
            headers(pair._1) = pair._2
        }

        def addParsed(line: String): Unit = {
            val firstColonIdx = line.indexOf(':')
            val headerKey = (line take firstColonIdx).trim
            val headerVal = (line drop firstColonIdx+1).trim
            headers += headerKey → headerVal
        }

        def httpString = headers.map{ case (k, v) => s"$k: $v" }.mkString(CRLF) + CRLF
    }
}
