package com.emiliorodo.ad.api.integration.dao

import com.emiliorodo.ad.AkkaDependenciesModule
import com.emiliorodo.ad.configuration.ApplicationConfigurationModule

/**
  * @author edafinov
  */
trait SubscriptionDaoModule {

  this: ApplicationConfigurationModule with AkkaDependenciesModule =>

  lazy val subscriptionEventDao = new SubscriptionEventDao(
    consumerKey = config.getString("oauth.consumer.key"),
    consumerSecret = config.getString("oauth.consumer.secret")
  )
}
