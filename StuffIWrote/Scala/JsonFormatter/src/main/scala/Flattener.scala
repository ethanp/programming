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

    def flatten(jObj: JObject): String = flatten(jObj, Vector(), Vector()).mkString("{", ",", "}")

    def flatten(value: JValue, soFar: Vector[String], path: Vector[String]): Vector[String] = {
        def pathKey: String = s""""${path mkString "."}":"""
        def valued(quoted: Boolean = true): String = pathKey + (if (quoted) "\"" + value.values + "\"" else value.values)
        value match {
            case JObject(vv) ⇒ vv.foldLeft(soFar) { case (curr, (k, v)) ⇒ curr ++ flatten(v, Vector(), path :+ k) }
            case JArray(arr) ⇒ soFar ++ arr.zipWithIndex.foldLeft(Vector[String]()) {
                case (c, (jv, idx)) ⇒ c ++ flatten(jv, Vector(), path :+ idx.toString)
            }
            case (JString(_) | JBool(_)) ⇒ soFar ++ Vector(valued())
            case _: JsonAST.JNumber ⇒ soFar ++ Vector(valued(quoted = false))
            case _ ⇒ throw new RuntimeException(s"found: $value")
        }
    }

    def main(args: Array[String]): Unit = {
        println(Flattener("""{"asdf":"bvbn","two":23}"""))
        println(Flattener("""{"asdf":"right", "left":{"inn":"object"}}"""))
        println(Flattener("""{"asdf":"right", "left":["inn",{"object":34}]}"""))
    }
}
