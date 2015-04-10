package ethanp.cluster

/**
 * Ethan Petuchowski
 * 4/9/15
 */
object FirstCluster extends App {
    // starting 2 clients and 3 servers
    Client.main(Seq("2551").toArray)
    Server.main(Seq("2552").toArray)
    Server.main(Array.empty)
    Server.main(Array.empty)
    Client.main(Array.empty)
}






