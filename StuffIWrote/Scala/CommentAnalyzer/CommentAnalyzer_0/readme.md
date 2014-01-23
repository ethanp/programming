Comment Analyzer, Version 0
==========================


Short List
----------
1. Add a new table to hold the comments
    * This will require a DB "evolution", docs [here](http://www.playframework.com/documentation/1.2.2/evolutions)
    * I will have to change the video validation/parsing/etc. to have a default date of `new java.util.Date()`
      or however one gets the current time into a `java.util.Date`

Should end up looking roughly like this

    [Video {id, title, lastCommentRetrievalTime}] <---->> [Comment {id, replyCount, text, date}]

1. Make it download and fill in the video's title for the DB automatically

Generally
---------

1. Go the [Parser Combinator](http://en.wikipedia.org/wiki/Parser_combinator)
   route for retrieving results from the DB.
    * I feel like they're nice to know for parsing and making DSLs,
      plus the other methods are boring.


Highest Level
-------------

I'd like a way to visualize interesting things about comments.

How I'm going to try to achieve that
------------------------------------

1. You enter in a URL to a web form
    * Eventually you'd type in a title or a part of a title
    * Or maybe it goes to that URL and parses the title out of the HTML there
    * It depends on what Google needs from me
1. That sends a GET to Play (i.e. me)
1. Play requests data from that video to Google's Youtube Data API v2
1. Google sends me a big JSON blob with who-knows-what
    * I'm counting on it containing comments
    * I'm hoping it also contains
        1. When someone is *responding* to another person's comment
        1. The number of up/downvotes per comment
1. I store the blob in a database for long-term personal use
1. I run Stanford's Sentiment Analyzing Recursive Neural Network over the comments
1. I run some parser that figures out the graph of who's responding to whom
    * Maybe eventually store it in neo4j
1. Classify comments that are spam
    * These may already be removed, hoo nose
1. Save the results in a database
1. Aggregate stats over the pages
1. Render results graphically on user's web-browser

What makes this "Version 0"?
----------------------------

1. I have no idea what I'm doing
1. I have no idea where this is going
1. I don't know how the Internet works
1. I've never used the `Playframework` or made web-app back-end beyond the Tutorial
1. I've never used `(JavaScript || jQuery || HTML || CSS || Database || Web-framework)` before
1. I don't know if my basic plan is possible
1. I'm sure whatever I do will be a "*bad* way to do it"
1. It totally sounds more "hacker" to start at zero
1. I want to put Solr in the next version, so I gotta leave room for another vsn no.
1. It can't go on a server because I put the application.secret up on the web

> The value of the application.secret configuration property will be something
> else: this is a random string that Play uses in various places to generate
> cryptographic signatures, most notably the session cookie. You should
> always leave this generated property in your application configuration. The
> “secret” in application.secret suggests that it should be kept secret. Be
> sure to use a different secret for your production environment and never
> check that into your source code repository.
>
> Peter Hilton, Erik Bakker, Francisco Canedo (2013-10-05).
> Play for Scala: Covers Play 2 (Kindle Locations 766-769). Manning Publications.
> Kindle Edition.

### I got Help

A bunch of my code and ideas come from *Play for Scala*, a book published by *Manning*, © 2014.
Here's what they have to say about this:

> Reuse of the code is permitted,
> in whole or in part, including the creation of derivative works, provided
> that you acknowledge that you are using it and identify the source:
> title, publisher and year.


