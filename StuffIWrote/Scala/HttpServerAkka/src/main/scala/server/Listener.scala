package server

import java.io.{BufferedReader, InputStreamReader, PrintStream}
import java.net.{ServerSocket, Socket}

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.collection.mutable

/**
 * Ethan Petuchowski
 * 9/29/15
 */
class Listener(listenPort: Int) {
    val listenSocket = try new ServerSocket(8080) catch { case _: Throwable => new ServerSocket(0) }
    println("server start on port: "+listenSocket.getLocalPort)
    val system = ActorSystem("client-services")
    var clientConnections = Vector.empty[ActorRef]
    while (!listenSocket.isClosed) {
        val clientConn = listenSocket.accept()
        println("received client connection")
        clientConnections :+= system.actorOf(ClientConnection.props(socket = clientConn))
    }
}

class ClientConnection(socket: Socket) extends Actor {
    val CRLF = "\r\n"
    val readIn = new BufferedReader(new InputStreamReader(socket.getInputStream))
    val writeOut = new PrintStream(socket.getOutputStream)
    def readRequest(): Option[Request] = {
        println("reading request")
        val (method, path) = {
            val requestLine = readIn.readLine()
            if (requestLine == null) {
                context.stop(self)
                return None
            }
            val method = requestLine.takeWhile(_ != ' ') match {
                case x if x equalsIgnoreCase "Get"      => Get
                case x if x equalsIgnoreCase "Head"     => Head
                case x if x equalsIgnoreCase "Options"  => Options
                case x if x equalsIgnoreCase "Put"      => Put
                case x if x equalsIgnoreCase "Delete"   => Delete
                case x if x equalsIgnoreCase "Trace"    => Trace
                case x if x equalsIgnoreCase "Post"     => Post
            }
            val path = requestLine.dropWhile(_ != ' ').trim.takeWhile(_ != ' ')
            method → path
        }

        val headers = {
            var line = readIn.readLine()
            val headers = new Headers
            while (line != null && !line.isEmpty) {
                headers.addParsed(line)
                line = readIn.readLine()
            }
            headers
        }

        val body = method match {
            case Post =>
                val bodyBuilder = new StringBuilder
                var line = readIn.readLine()
                while (line != null && !line.isEmpty) {
                    bodyBuilder.append(s"$line\n")
                    line = readIn.readLine()
                }
                bodyBuilder.toString()
            case _ => ""
        }

        Some(Request(method, path, headers, body))
    }

    def respond(request: Option[Request]): Unit = {
        if (request.isEmpty) return
        val statusCode = "200 OK"
        val responseStatus = s"HTTP/1.1 $statusCode"
        val responseHeaders = {
            new Headers(mutable.Map[String, String](
                "Content-Type" → "text/html; charset=UTF-8",
                "Accept-Ranges" → "bytes",
                "Connection" → "close" // no persistence at this time.
            ))
        }
        val body = "<h1>aww yeah.</h1>"
        responseHeaders.addPair("Content-Length" → body.length.toString)
        val response = Seq(
            responseStatus,
            responseHeaders.httpString + CRLF,
            body
        ).mkString("", CRLF, CRLF)
//        println(response)
        writeOut.print(response)

        // TODO maybe not so fast? (consider the effect of sending the Content-Length)
        writeOut.close()
    }

    val request = readRequest()
    respond(request)
    socket.close() // not sure how to implement persistence yet.
    context.stop(self)

    override def receive = {case x => println(s"received something?: $x")}
}

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

    def httpString = headers map { case (k, v) => s"$k: $v" } mkString { "\r\n" }

}

case class Request(
    method: Method,
    path: String,
    headers: Headers,
    body: String
)


object ClientConnection {
    def props(socket: Socket) = Props(classOf[ClientConnection], socket)
}

object Listener extends App {
    new Listener(8080)
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
