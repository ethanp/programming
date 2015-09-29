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

class Connection(conn: Socket) {
    val in = conn.getInputStream
    val out = conn.getOutputStream
    var inRd = new BufferedReader(new InputStreamReader(in))
    while (!conn.isClosed) {
        val requestLine: String = readReqLine
        val headers: Vector[String] = readHeaders
        val body = readBody
        println(s"requestLine: $requestLine\nheaders: $headers\nbody: $body")
        out.write(1000)
    }
    def readReqLine: String = inRd.readLine()
    def readHeaders: Vector[String] = {
        @tailrec def readHeaders(v: Vector[String]): Vector[String] = {
            Option(inRd.readLine()) match {
                case None => v
                case Some(str) if str.isEmpty => v
                case Some(str) => readHeaders(v :+ str)
            }
        }
        readHeaders(Vector())
    }
    def readBody: String = {
        val sb = new StringBuilder
        var line = inRd.readLine()
        while (!(line == null || line.isEmpty)) {
            sb.append(line)
            line = inRd.readLine()
        }
        sb.toString()
    }
}
