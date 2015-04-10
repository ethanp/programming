package ethanp.cluster

/**
 * Ethan Petuchowski
 * 4/9/15
 *
 * The master receives commands from the command line and deals with them appropriately
 */
// TODO make the master the first seed node
object Master extends App {
    // starting 2 clients and 3 servers (in this Process, but as separate Actors)
    Client.main(Seq("2551").toArray)
    Server.main(Seq("2552").toArray)
    Server.main(Array.empty)
    Server.main(Array.empty)
    Client.main(Array.empty)
}


