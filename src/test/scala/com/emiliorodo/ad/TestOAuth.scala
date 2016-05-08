package com.emiliorodo.ad

import com.emiliorodo.ad.util.UnitTestSpec

/**
  * @author edafinov
  */
class TestOAuth extends UnitTestSpec {

  it should "fsdfs" in {
    import java.net.{HttpURLConnection, URL}

    import oauth.signpost.basic.DefaultOAuthConsumer

    val consumer = new DefaultOAuthConsumer(
      "consumer_key",
      "consumer_secret"
    )

    val url = new URL("http://example.com/protected");

    val request: HttpURLConnection = url.openConnection().asInstanceOf[HttpURLConnection]
    val signed = consumer.sign(request)

    val signedUrl = consumer.sign("http://example.com/protected")
    val consKey = consumer.getConsumerKey
    val consSecret = consumer.getConsumerSecret
    val constToken = consumer.getToken
    val consTokenSecret = consumer.getTokenSecret
    val a = 5
  }
}
