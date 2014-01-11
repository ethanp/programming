Comment Analyzer, Version 0
==========================

Notes
-----
v3 of youtube specifically is here
http://maven-repository.com/artifact/com.google.apis/google-api-services-youtube/v3-rev64-1.13.2-beta

v2 of youtube specifically is here
http://maven-repository.com/artifact/com.google.api.client/google-api-data-youtube-v2/1.0.10-alpha

not sure of the pros/cons of that v2 vs the whole client like I have,
except that this one is probably much bigger and has a bunch of stuff I won't use

Highest Level
-------------

I'd like way to visualize interesting things about comments.

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
1. I've never used `(JavaScript || jQuery || HTML || CSS)` before
1. I don't know if my basic plan is possible
1. I'm sure whatever I do will be a "*bad* way to do it"
1. It totally sounds more "hacker" to start at zero
1. I want to put Solr in the next version, so I gotta leave room for another vrsn no.
