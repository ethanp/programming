Comment Analyzer, Version 0
==========================

Downloading Comments from the Youtube API
-----------------------------------------

* Until further notice, one must use API v2 to download a video's comments
   * [Docs on that](https://developers.google.com/youtube/2.0/developers_guide_protocol_comments)
* However *comment threads* have been moved out of the Youtube API to G+, so now
  you have to use that to retrieve responses to comments
  
### [Youtube Developer Guide: Java](https://developers.google.com/youtube/2.0/developers_guide_java)

> Your client application can use the YouTube Data API to fetch video
feeds, comments, responses, and playlists, as well as query for
videos that match particular criteria.



### According to [Changes to Comments](https://developers.google.com/youtube/articles/changes_to_comments#threading):

> Comment replies made before the transition now appear as regular comments.

* Not sure yet if that means it's now hopeless to find those

> Replies to new-style YouTube comments via the YouTube and Google+
  sites and apps are not returned with the legacy API's comment feed.
  However, you can get the actual replies via the Google+ APIs.

*"The excerpt below demonstrates how a Google+ Activity ID, in the indicated
text, appears in a comment feed entry:"*

    <entry>
      <id>tag:youtube,2008:video:xpI6VNvRTII:comment:THIS=>**z13rtnibotmidjggs04cdvzgvurngv3pwnw0k**</id>
      ...
      <yt:replyCount>4</yt:replyCount>
      ...
    </entry>

<a name="blobofxml"></a>
### [Blob of XML](http://gdata.youtube.com/feeds/api/videos/9bZkp7q19f0/comments?prettyprint=true) for the comments of Gangnam Style

* The junk-text in the data's URL is the same as the video's URL

If `<feed><entry><yt:replyCount> > 0`, then you plug in the 
`<feed><entry><id>(.*)comments/(.*)$.r.group(2)`
(in this case `z13pylpylxucy5rjh04cfpyqdoemdpkrmhs`) to the
[Google+ Comments: list #try-it](https://developers.google.com/+/api/latest/comments/list#try-it),
it sends a

    GET https://www.googleapis.com/plus/v1/activities/z13pylpylxucy5rjh04cfpyqdoemdpkrmhs/comments?key={YOUR_API_KEY}

to which you get a `Response 200 OK: json` which has the 9 responses
inside a list attached to `"items"`, and each of those has an
`"object" // "content" // <TEXT OF THE COMMENT>`, **so I guess we're done.**

The only problem is it looks like it's undergoing quite a bit of change,
and has been for a few months, so maybe this method isn't the most stable,
but I don't see any other way to do it, and [the corresponding SOQ](http://stackoverflow.com/questions/19965856/how-to-get-all-comments-from-a-video-in-the-youtube-api-following-the-change-to)
has no responses.

### [Data API](https://developers.google.com/youtube/2.0/reference?hl=en#Comments_Feeds)
The above link shows how using the data api one can find the comments feed
for a given video. I guess this would bring me to the [blob of xml](#blobofxml)
shown above.

### This [also works for YouTube channels](http://googlesystem.blogspot.com/2013/11/youtube-tests-google-comments.html)
Could also be interesting, jus' sayin'.

Short List
----------
1. Download comments from the Youtube API and see what I actually have 
   to work with
1. Plug the DB in in such a way that it verifiably *is* saving my form
   data there and rendering it to the view
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

Ideas for the Future
--------------------
[User's playlists feed](https://developers.google.com/youtube/2.0/reference?hl=en#User_favorites_feed),
retrieves a list of videos that a particular user has explicitly flagged as a favorite video.

[Playlist feed](https://developers.google.com/youtube/2.0/reference?hl=en#Playlist_feed)
is also retrievable.


Unimportant Notes
-----------------
v3 of youtube specifically is [here](http://maven-repository.com/artifact/com.google.apis/google-api-services-youtube/v3-rev64-1.13.2-beta)

v2 of youtube specifically is [here](http://maven-repository.com/artifact/com.google.api.client/google-api-data-youtube-v2/1.0.10-alpha)

Not sure of the pros/cons of that v2 vs the *whole client* like I have,
except that this one is probably much bigger and has a bunch of stuff I won't use.

