package ethanp.mastermind

import java.util.Scanner

/**
 * Ethan Petuchowski
 * 3/30/15
 */
object Mastermind extends App {
    val sc = new Scanner(System.in)
    val truth = Configuration(Blue, Green, Yellow, Red)
    var s: Array[String] = null
    while (true) {
        print("Enter guess: ")
        processInput()
    }

    def processInput() {
        s = sc.nextLine split " "
        s match {
            case x if x.length == 4 ⇒
                val conf = Configuration from s
                conf match {
                    case Some(e) ⇒ processGuess(e)
                    case None ⇒ println("invalid colors")
                }
            case _ ⇒
                println("require 4 colors")
        }
    }

    def processGuess(guess: Configuration) {
        println(guess)
    }
}

sealed abstract class Color(val repr: String)
case object Blue extends Color("blue")
case object Red extends Color("red")
case object Yellow extends Color("yellow")
case object Green extends Color("green")

case class Configuration(a:Color,b:Color,c:Color,d:Color)

object Configuration {
    def from(strings: Array[String]): Option[Configuration] = {
        val t = strings.flatMap { s ⇒
            s match {
                case x if x equalsIgnoreCase Blue.repr ⇒ Some(Blue)
                case x if x equalsIgnoreCase Red.repr ⇒ Some(Red)
                case x if x equalsIgnoreCase Yellow.repr ⇒ Some(Yellow)
                case x if x equalsIgnoreCase Green.repr ⇒ Some(Green)
                case _ ⇒ None
            }
        }
        if (t.length == 4) Some(Configuration(t(0), t(1), t(2), t(3)))
        else None
    }
}
