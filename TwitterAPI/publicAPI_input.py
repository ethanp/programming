# Connect to the Developers Twitter API using OAuth to spew a subset of tweets onto the screen
# 6/13/13

# CODE STOLEN FROM
# http://peter-hoffmann.com/2012/simple-twitter-streaming-api-access-with-python-and-oauth.html

import sys
import tweepy

consumer_key="b2o7HxxxawIhWf8MNX8Q"
consumer_secret="XfRXnLSv2kqGm4Ecei7HxGYOSHsGYGnVVhR2vi9GU"
access_key = "1477700791-5DSssHwV90MbsH3ZmgcxzzfKD1DDGA8GiMH48nQ"
access_secret = "MADSyfR5g028FH6OiRzFvOMjv1vv90YkyOIZaw5kfEY"
access_token = "1477700791-5DSssHwV90MbsH3ZmgcxzzfKD1DDGA8GiMH48nQ"
access_token_secret = "MADSyfR5g028FH6OiRzFvOMjv1vv90YkyOIZaw5kfEY"


auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_key, access_secret)
api = tweepy.API(auth)

class CustomStreamListener(tweepy.StreamListener):
    def on_status(self, status):
        print status.text

    def on_error(self, status_code):
        print >> sys.stderr, 'Encountered error with status code:', status_code
        return True # Don't kill the stream

    def on_timeout(self):
        print >> sys.stderr, 'Timeout...'
        return True # Don't kill the stream


sapi = tweepy.streaming.Stream(auth, CustomStreamListener())
# sapi.filter(track=['the'])
sapi.sample()