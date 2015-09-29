package server

import java.io.{OutputStreamWriter, BufferedWriter, InputStreamReader, BufferedReader}
import java.net.{Socket, ServerSocket}

import scala.annotation.tailrec

/**
 * Ethan Petuchowski
 * 9/29/15
 *
 * I've been reading a bunch about HTTP,
 * so it's about time to implement at least a piece of it...
 */
object Basic {
    def main(args: Array[String]): Unit = new Basic(0)
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
sealed trait HasBody
case object Get     extends Method with Idempotent
case object Head    extends Method with Idempotent
case object Options extends Method with Idempotent
case object Put     extends Method with Idempotent
case object Delete  extends Method with Idempotent
case object Trace   extends Method with Idempotent
case object Post    extends Method with HasBody

class Connection(conn: Socket) {
    val in = conn.getInputStream
    val out = conn.getOutputStream
    val inRd = new BufferedReader(new InputStreamReader(in))
    val outPt = new BufferedWriter(new OutputStreamWriter(out))

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
        val body = if (method.isInstanceOf[HasBody]) readBody else ""
        println(s"requestLine: $requestLine\nheaders: $headers\nbody: $body")
        val outString =
           s"""
              |HTTP/1.1 200 OK
              |Content-Type: text/html; charset=UTF-8
              |Accept-Ranges: bytes
              |Connection: close
              |
              |<html>
              |<head>
              |  <title>An Example Page</title>
              |</head>
              |<body>
              |  Hello World, this is a very simple HTML document.
              |  ${headers.mkString(", ")}
              |</body>
              |</html>
            """.stripMargin
        outPt.write(outString)
        outPt.flush()
        conn.close() // this is to save from having to calculate the "Content-Length"
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
