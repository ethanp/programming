How 2048 is Implemented
=======================

I was curious how a smash-hit online game is put together.  So, in April, on a
cross-country plane ride, I read the game's implementation line-by-line and in
the process became familiar with the basics of JavaScript, CSS, HTML, and
Simple Game Design by osmosis from a very successful game.

The highlights are probably no surprise to anyone who has ever written a "Hello
Game World" in JavaScript, which I had not done at the time (though I have
since [[Javascript Wackamole](https://github.com/ethanp/javascript-whackamole)]).


### Lessons:

1. Every component of the layout is in its own div, custom styled with CSS
2. Animating the movement of the tiles is done with CSS, not Javascript
3. All the game logic is in Javascript
4. The user's personal record score is saved in
   [Web Storage](http://en.wikipedia.org/wiki/Web_storage), specifically
   `window.localStorage`.
5. The major components of the game are each encapsulated within their
   own prototypes

#### Major Components

1. **`InputManager`** -- registers the different ways you can signal your
                   move to the game (wsad keys, arrow keys, and swipe on screen)
2. **`StorageManager`** -- interfaces game with `localStorage`
3. **`Grid`** -- knows the state of the game board, and can give you a random unused
           cell, for example
4. **`GameManager`**
    * Manages whether the game is going, won, or lost
    * Puts new tiles on the screen each turn
    * Saves the state of the `Grid` between moves
    * Tells tiles to move in reaction to InputManager events
    * Figures out tile states, after collisions have been found (or not found)


The game gets kicked off by passing the `size`, `InputManager`, `Actuator`, and
`StorageManger` to the `GameManager`.  This happens when `application.js` is
loaded at the end of the `<body>` of `index.html`, the sole `.html` file.

My practically line-by-line annotated edition of the code (still functions
too), is in my [fork on GitHub](https://github.com/ethanp/2048).
