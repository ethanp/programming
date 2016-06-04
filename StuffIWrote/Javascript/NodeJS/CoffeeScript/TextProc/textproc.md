# TextProc

Ethan Petuchowski

August 21, 2014

## What are the technologies?

This is my first experiment with

* Node.js
* Express.js


It seems like the whole world is raving about these things, so
what's the harm in giving them a look-see?

## What is this project?

It downloads *Alice in Wonderland* from Gutenberg, and builds up a
reverse-index of word by line number.

## How did I make it

I think I just used the `express MyApp` generator

    express -c stylus TextProc

Now, to start the app requires

    npm start
    
    # or
    node ./bin/www