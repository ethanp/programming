package na.ethan.AkkaConcurrency

/**
 * Ethan Petuchowski
 * 4/27/14
 * From Akka Concurrency, by Derek Wyatt
 */

import java.util.concurrent.Executors
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration._
object MainAkka {
  val pool = Executors.newCachedThreadPool()
  implicit val ec = ExecutionContext.fromExecutorService(pool)
  def main(args: Array[String]) {
    val future = Future { "Fibonacci Numbers" }
    val result = Await.result(future, 1.second)
    println(result)
    pool.shutdown()
  }
}

import akka.actor.{Actor, Props, ActorSystem}
// Our Actor
class BadShakespeareanActor extends Actor { // The 'Business Logic'
  def receive = {
    case "Good Morning" =>
      println("Him: Forsooth 'tis the 'morn, but mourneth for thou doest I do!")
    case "You're terrible" => println("Him: Yup")
  }
}

object BadShakespeareanMain {
  val system = ActorSystem("BadShakespearean")
  val actor = system.actorOf(Props[BadShakespeareanActor])
  // We'll use this utility method to talk with our Actor
  def send(msg: String) {
    println("Me: " + msg)
    actor ! msg
    Thread.sleep(100)
  }
  // And our driver
  def main(args: Array[String]) {
    send("Good Morning")
    send("You're terrible")
    system.shutdown()
  }
}
