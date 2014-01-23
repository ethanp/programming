Postgres DB Viewer
------------------

There's a *really nice-looking one* **built-in** to IntelliJ, and **it sees my data!**.

Just click on the `Database` button on the right side of the screen.

I found it when I clicked randomly on the screen by accident.

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

