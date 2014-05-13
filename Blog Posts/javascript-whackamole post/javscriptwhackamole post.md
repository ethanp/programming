After reading the implementation of 2048 [link to post **TODO**](),
it became time to make my own game. There were a few design goals of
this game.

### Design Goals

1. Easy to make
2. Doesn't need jQuery
3. Game is easy to play and obvious to figure out
4. Game is multiplayer
5. All the multiplayer game-elements are there
	* (even if in this version you can't play it multiplayer)

The reason not to include jQuery was that web-designing friend said
it's uncool to use jQuery these days because then you have to send
it out to people everytime they load your page. I'm not sure whether
that's true or not, but I went with it.... Until I got to the last part
of the game which involved adding a subtree of divs to the DOM.

Here's the excerpt that I'm talking about

	Scoreboard.prototype.render = function () {
	   var $scoreboard = $('#scoreboard');
	   $scoreboard.empty();
	   $.each(this.game.scores, function (player, score) {
	       $scoreboard.append(
	           $('<div>')
	               .addClass('panel panel-default')
	               .append(
	               $('<h3>')
	                   .addClass('panel-heading panel-title')
	                   .text(player))
	               .append(
	               $('<h4>')
	                   .addClass('panel-body')
	                   .text(score)));
	   });
	};
	
Suffice it to say, I just wanted my code to look that cool, so I
decided to include jQuery. Once I had included jQuery, I switched
all the normal `getElementBy`... to `$()` *because why not*.


The reason to make a multiplayer-ready game, is that I'd like to someday hook this up to `playframework` and make it truly multiplayer via `websocket`. That's still `TODO` though....

It uses Bootstrap because I don't know any better.

### Here's a screenshot of the game

![](/Users/ethan/code/non_apple/programming/Blog\ Posts/javascript-whackamole\ post/Whole\ Game.png)

### Here's the scoreboard after having added a few more players

![](/Users/ethan/code/non_apple/programming/Blog\ Posts/javascript-whackamole\ post/Addnl\ Players.png)
