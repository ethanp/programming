import org.json4s.JsonAST.JObject
import org.json4s.jackson.JsonMethods._
import org.json4s._

import scala.util.{Success, Failure, Try}

/**
  * Ethan Petuchowski
  * 3/28/16
  */
object Flattener {
    def apply(jsonStr: String): String = {
        Try(parse(jsonStr).asInstanceOf[JObject]) match {
            case Success(jObj) ⇒ flatten(jObj)
            case Failure(exception) ⇒
                throw new RuntimeException(
                    s"couldn't parse string:\n$exception")
        }
    }

    def flatten(jObj: JObject): String = flatten(jObj, Vector()).mkString("{", ",", "}")

    def flatten(js: JValue, path: Vector[String]): Vector[String] = {
        def pathKey: String = s""""${path mkString "."}":"""
        def valued(quoted: Boolean = true): String = pathKey + (if (quoted) "\"" + js.values + "\"" else js.values)
        def arrToObj(v: Vector[JValue]) = v.zipWithIndex map (x ⇒ x._2.toString → x._1)
        def recurse(v: Vector[(String, JValue)]): Vector[String] = v flatMap { case (x, y) ⇒ flatten(y, path :+ x) }
        js match {
            case JObject(asdf) ⇒ recurse(asdf.toVector)
            case JArray(arr) ⇒ recurse(arrToObj(arr.toVector))
            case (JString(_) | JBool(_)) ⇒ Vector(valued())
            case _: JsonAST.JNumber ⇒ Vector(valued(quoted = false))
            case _ ⇒ throw new RuntimeException(s"found: $js")
        }
    }

    def main(args: Array[String]): Unit = {
        println(Flattener("""{"asdf":"bvbn","two":23}"""))
        println(Flattener("""{"asdf":"right", "left":{"inn":"object"}}"""))
        println(Flattener("""{"asdf":"right", "left":["inn",{"object":34}]}"""))
    }
}
