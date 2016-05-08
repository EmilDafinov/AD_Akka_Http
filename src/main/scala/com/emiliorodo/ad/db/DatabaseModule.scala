package com.emiliorodo.ad.db

import com.emiliorodo.ad.configuration.ApplicationConfigurationModule
import slick.driver.PostgresDriver.api._
/**
  * @author edafinov
  */
trait DatabaseModule {
  this: ApplicationConfigurationModule =>

  val database: Database =
    Database.forURL(
      url = config.getString("db.url"),
      user = config.getString("db.user"),
      password = config.getString("db.password"),
      driver = "org.postgresql.Driver"
    )

  lazy val subscriptionDao = new SubscriptionDao(database)
}
