from __future__ import absolute_import, print_function

from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream
import json

# Go to http://apps.twitter.com and create an app.
# The consumer key and secret will be generated for you after
consumer_key="M6wMSsltNv1aUuKpe7ou2oBvs"
consumer_secret="AmVNUgspDUZLDlidooS207HW9AZxk1Coy5hmV1UIhfjGXyaTHv"

# After the step above, you will be redirected to your app's page.
# Create an access token under the the "Your access token" section

access_token="327473973-OqIeRpAawJvQEfXYqWAW8GvrBjc7IiSo6i2lVlLe"
access_token_secret="WbMpEsn19gV1K7qPlz4wiGv1sEcfzR5OABDHZDLZ59HBY"

class StdOutListener(StreamListener):
    """ A listener handles tweets that are received from the stream.
    This is a basic listener that just prints received tweets to stdout.
    """
    
    def on_data(self, data):
        try:
            with open('data1.json', 'a') as outfile:
                json.dump(data,outfile)
            with open('data2.json','a') as outputj:
                outputj.write(data)
            with open('tweets.txt', 'a') as tweets:
                tweets.write(data)
                tweets.write('\n')
            outfile.close()
            tweets.close()
            outputj.close()
        except BaseException as e:
            print('problem collecting tweet',str(e))
        return True

    def on_error(self, status):
        print(status)

if __name__ == '__main__':
    l = StdOutListener()
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    stream = Stream(auth, l)
    stream.filter(track=['politics'])

