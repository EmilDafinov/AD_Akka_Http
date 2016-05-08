import java.net.{HttpURLConnection, URL}

import oauth.signpost.basic.DefaultOAuthConsumer

val consumer = new DefaultOAuthConsumer(
  "consumer_key",
  "consumer_secret"
)

val url = new URL("http://example.com/protected");

val request: HttpURLConnection = url.openConnection().asInstanceOf[HttpURLConnection]
val signed = consumer.sign(request)

consumer.sign("http://example.com/protected")
consumer.getConsumerKey
consumer.getConsumerSecret
consumer.getToken
consumer.getTokenSecret
