package com.emiliorodo.ad.configuration

import com.typesafe.config.ConfigFactory

/**
  * @author edafinov
  */
trait ApplicationConfigurationModule {
  lazy val config = ConfigFactory.load()
}
