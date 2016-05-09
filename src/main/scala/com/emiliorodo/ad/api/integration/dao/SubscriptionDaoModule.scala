package com.emiliorodo.ad.api.integration.dao

import com.emiliorodo.ad.AkkaDependenciesModule
import com.emiliorodo.ad.configuration.ApplicationConfigurationModule
import com.emiliorodo.ad.security.OAuthTool

/**
  * @author edafinov
  */
trait SubscriptionDaoModule {

  this: ApplicationConfigurationModule with AkkaDependenciesModule =>

  lazy val OAuthSigner = new OAuthTool(
    consumerKey = config.getString("oauth.consumer.key"),
    consumerSecret = config.getString("oauth.consumer.secret")
  )

  lazy val subscriptionEventDao = new SubscriptionEventDao(signer = OAuthSigner)

}
