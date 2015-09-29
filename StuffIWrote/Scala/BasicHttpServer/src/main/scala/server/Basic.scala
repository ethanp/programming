package server

import java.io.{InputStreamReader, BufferedReader}
import java.net.{Socket, ServerSocket}

import scala.annotation.tailrec

/**
 * Ethan Petuchowski
 * 9/29/15
 */
object Basic {
    def main(args: Array[String]) = new Basic(0)
}

class Basic(listenPort: Int) {
    val s = new ServerSocket(listenPort)
    println("listening on port: "+s.getLocalPort)
    while (!s.isClosed) {
        val r = s.accept()
        println("found someone")
        new Connection(conn = r)
    }
}

sealed trait Method
sealed trait Idempotent
case object Get extends Method with Idempotent
case object Head extends Method with Idempotent
case object Options extends Method with Idempotent
case object Put extends Method with Idempotent
case object Delete extends Method with Idempotent
case object Trace extends Method with Idempotent
case object Post extends Method

class Connection(conn: Socket) {
    val in = conn.getInputStream
    val out = conn.getOutputStream
    var inRd = new BufferedReader(new InputStreamReader(in))

    def parseMethod(requestLine: String): Method = {
        requestLine.takeWhile(_ != ' ') match {
            case x if x equalsIgnoreCase "Get"      => Get
            case x if x equalsIgnoreCase "Head"     => Head
            case x if x equalsIgnoreCase "Options"  => Options
            case x if x equalsIgnoreCase "Put"      => Put
            case x if x equalsIgnoreCase "Delete"   => Delete
            case x if x equalsIgnoreCase "Trace"    => Trace
            case x if x equalsIgnoreCase "Post"     => Post
        }
    }

    while (!conn.isClosed) {
        val requestLine: String = readReqLine
        val method = parseMethod(requestLine)
        println(s"method: $method")
        val headers: Map[String, String] = readHeaders
        method match {
            case Post =>
                val body = readBody
                println(s"requestLine: $requestLine\nheaders: $headers\nbody: $body")
            case _ =>
                println(s"requestLine: $requestLine\nheaders: $headers")
        }
        out.write(1000)
    }
    def readReqLine: String = inRd.readLine()
    def readHeaders: Map[String, String] = {
        @tailrec def readHeaders(v: Map[String, String]): Map[String, String] = {
            Option(inRd.readLine()) match {
                case None => v
                case Some(str) if str.isEmpty => v
                case Some(str) =>
                    val colonIdx = str.indexOf(":")
                    val header = str.take(colonIdx).trim → str.drop(colonIdx+1).trim
                    readHeaders(v + header)
            }
        }
        readHeaders(Map())
    }
    def readBody: String = {
        val sb = new StringBuilder
        var line = inRd.readLine()
        while (!(line == null || line.isEmpty)) {
            sb.append(line).append('\n')
            line = inRd.readLine()
        }
        sb.toString()
    }
}
