package com.emiliorodo.ad.security

import akka.http.scaladsl.model.Uri
import com.emiliorodo.ad.util.UnitTestSpec

/**
  * @author edafinov
  */
class OAuthToolTest extends UnitTestSpec {
  behavior of "OAuthTool"

  val consumerKey = "consumer_key"
  val consumerSecret = "consumer_secret"
  val testedOauthTool = new OAuthTool(consumerKey, consumerSecret)

  it should "dummy test, serves to debug the oauth behavior" in {
    //Given
    val dummyUrl = "http://dummy.com"

    //When
    val signedUrl = testedOauthTool.sign(dummyUrl)
    val uri = Uri(signedUrl)
    val rawQuery: Array[(String, String)] = uri.rawQueryString.get.split("&") map { header =>
      val tokens = header.split("=")
      tokens(0) -> tokens(1)
    }
    val params = rawQuery.groupBy {
      case (headerName: String, headerValue: String) => headerName
    } mapValues (_.head._2)
    testedOauthTool.hasValidSignature(signedUrl, params)
    //Then
    val a = 5
  }
}
