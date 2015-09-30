package server

import java.net.ServerSocket

import akka.actor.{ActorRef, ActorSystem}

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

object Listener extends App {
    new Listener(8080)
}


