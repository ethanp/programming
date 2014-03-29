package actors

/**
 * Ethan Petuchowski
 * 3/23/14
 */

import akka.actor.Actor

/**
 * The DiscussionActor manages a particular Discussion within the App.
 */
class DiscussionActor extends Actor {
  def receive: Receive = ???
}

/**
 * The DiscussionApp handles the boot-up and boot-down of the App.
 * This means loading the Redis DB from disk and putting the
 * data in the appropriate place, and whatever initializations are req'd.
 */
class DiscussionApp extends Actor {
  def receive = ???
}
