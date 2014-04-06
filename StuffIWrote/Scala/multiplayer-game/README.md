Click the Square
================

TODO Queue
----------

1. Follow the TODO's in the files, homie
    * They're mainly for the **scoreboard**
2. A "leave game" button
3. Game Over scenario
4. Clicking a square

Outline
-------

This is going to be the most viralest game ever, I swear. There's a bunch of
squares on the screen, and every once in a while, one of them turns red. Then
the first player to click on the red square gets some points. The first player
to reach 10 points wins. Actually I think this is basically Whack-a-Mole.

* One person creates a game, and then waits for someone else to join.
* On the entry page, there's a list of available games to join.
* When 2 people are connected, the game starts, but others can still join.
* It's quite extensible to have all sorts of other features, but let's not get
  carried away at the moment.

The point is that this project is quite similar in architecture to the
archetypical ChatRoom app for learning to use `Play`, `WebSockets`, and
`Akka`; only this app is *not* a ChatRoom app, so there is some original
design work that remains for me to do.

This is based quite closely on the ChatRoom sample app in the playframework repository:
[github.com/playframework/playframework/tree/master/samples/scala/websocket-chat](https://github.com/playframework/playframework/tree/master/samples/scala/websocket-chat)


Grimy Details
-------------

Theres no database. No need, really -- there's no history to keep track of at
this point. The only thing that the server needs to know is who is in which
game instance and how many points each player has.

### So we have some classes

    object GameApp
        val ongoingGames = mutable.List[Game]

    class Game
        val title: String
        val scoreboard: Map[User, Int]

    class User
        val username: String


### The Plot

1. User reaches homepage, sees welcome screen, tells him to log in
2. User logs in
3. User can click on a listed existing game or create new game with the `Ooh shiny` button
4. User enters a game, but if they're the only one there it says "Waiting for another player (`||:.|..|...:||`)"
5. When there's ≥ 2 players, the game begins and it displays the scoreboard
