Click the Square
================

This is going to be the most viralest game ever, I swear. There's a bunch of
squares on the screen, and every once in a while, one of them turns red. Then
the first player to click on the red square gets some points. The first player
to reach 10 points wins.

* One person creates a game, and then waits for someone else to join.
* On the entry page, there's a list of available games to join
* When two people are connected, the game starts
* It's quite extensible to have all sorts of other features, but let's not get
  carried away at the moment

The point is that this project is quite similar in architecture to the
archetypical ChatRoom app for learning to use Play, WebSockets, and Akka; only
this app is *not* a ChatRoom app, so there is some original design work that
remains for me to do.

This is based quite closely on the ChatRoom sample app in the playframework repository:
[github.com/playframework/playframework/tree/master/samples/scala/websocket-chat](https://github.com/playframework/playframework/tree/master/samples/scala/websocket-chat)
