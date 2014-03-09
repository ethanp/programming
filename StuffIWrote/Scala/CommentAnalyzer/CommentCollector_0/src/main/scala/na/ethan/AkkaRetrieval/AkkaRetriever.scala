/**
 * Ethan Petuchowski
 * 3/8/14
 *
 * Goal:
 * -----
 * Efficiently download and analyze comments from YouTube
 * by passing them between different actors that do different
 * pieces of the whole pipeline.
 *
 * Problem this Addresses:
 * -----------------------
 * My app currently does the following:
 *
 *  1. Download next page of youtube comments (network)
 *  2. Parse comments into components (CPU)
 *  3. Calculate sentiment value of comments (CPU)
 *  4. Store comments in database (I/O)
 *  5. Repeat as necessary
 *
 * NOTE: Each repetition has to wait on the last, but this is for nought!
 *
 * I'd like to parallelize this by introducing actors which are each
 * responsible for a different piece of the action, but I could have
 * a bunch of them doing performing each task at the same time, with
 * no hard limit as far as the program itself is concerned.
 *
 * Actors:
 * -------
 *  YouTube Retriever: downloads a given page of comments from YouTube
 *  Youtube Comment Parser: turns a page of comments into a list of comment components
 *      (still needs to be fleshed out)
 *  GPlus Retriever: downloads a given page of comments from Google Plus
 *  GPlus Comment Parser: turns a page of comments into a list of comment components
 *  Sentiment Analyzer: returns the sentiment value of a given piece of text
 */

package na.ethan.AkkaRetrieval

import akka.actor.{ActorSystem, ActorRef, Actor, Props}

/**
 * Downloads a given page of comments from youtube
 */
class YouTubeRetriever extends Actor {
    // TODO receive
    def receive: Actor.Receive = {
        case _ => None
    }
}

object HelloGDataWithAkka extends App {
    println("First Line")
    val system = ActorSystem(name="helloGData")
    val retriever : ActorRef = system.actorOf(props=Props[YouTubeRetriever], name="retriever")
    println("Final Line")
}
