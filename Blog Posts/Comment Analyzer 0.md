Comment Analyzer 0
==================

May 11, 2014
------------

In January, I made a little web app with [Playframework](http://playframework.com/) for Scala as a way of learning how that Hinternet thing works. It did end up working, but it is slow beyond slow, as in ≥10 seconds to load a page. I believe the best way to speed it up would be to use Websockets and `Iteratees` in `Play`.

The app looks like this:

#### Search for a video

![](/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/Blog\ Posts/CA0\ New.png)

#### View comment analysis of a video

![](/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/Blog\ Posts/CA0\ Coltrane.png)

#### View list of videos retrieved

![](/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/Blog\ Posts/CA0\ Home.png)

The point is, first you paste in the URL of a YouTube video whose comments you want to analyze. Eventually you see all the comments and replies to those comments, and a sentiment analysis score for each, as well as an overall sentiment for the movie.

Here is an abbreviated list of steps to make this happen:

1. Get the comments on the video via the `YouTube API`
2. Parse the XML for each comment to retrieve the ID for
3. A call to the `Google+ API` for each comment to get it's replies
4. All comments and replies are passed through the very complex and fancy [Stanford sentiment analyzer](http://nlp.stanford.edu:8080/sentiment/rntnDemo.html)
5. Everything is loaded into the `PostgreSQL` database
6. All the comments and scores are sent in a full HTML page response

Due to a confusing migration process happening with the API for getting YouTube comments, I decided to make a prototype-playground for them in a [separate directory](https://github.com/ethanp/programming/tree/master/StuffIWrote/Scala/CommentAnalyzer/CommentCollector_0/src/main/scala/na/ethan/CommentCollector) with the same included libraries.

The issue is that Google has been [migrating their YouTube comment system into Google+](https://developers.google.com/youtube/articles/changes_to_comments). So comments directly on videos were (and still are) only accessible via the deprecated [YouTube Data API v2](https://developers.google.com/youtube/2.0/developers_guide_protocol_comments) which returns results as an XML blob. Somewhere in this blob is a Google+ ID. Replies to these comments are only accessible via the [Google+ API](https://developers.google.com/+/api/), to which you pass the ID from the XML blob, and receive a JSON blob as a response.