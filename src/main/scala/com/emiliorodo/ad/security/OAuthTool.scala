package com.emiliorodo.ad.security

import akka.http.scaladsl.model.Uri
import oauth.signpost.basic.DefaultOAuthConsumer

import scala.util.Try
import scala.util.control.NonFatal

/**
  * @author edafinov
  */
class OAuthTool(consumerKey: String, consumerSecret: String) {

  def sign(url: String) = {
    val consumer = new DefaultOAuthConsumer(
      consumerKey,
      consumerSecret
    )

    consumer.sign(url)
  }

  def hasValidSignature(uRI: Uri, urlParams: Map[String, String]) = {
    Try {
      val oauthSignature = urlParams("oauth_signature")
      val oauthVersion = urlParams("oauth_version")
      val consumerKey = urlParams("oauth_consumer_key")
      val signatureMethod = urlParams("oauth_signature_method")

      ???
    } recover {
      case NonFatal(exception) => false
    }
  }
}
