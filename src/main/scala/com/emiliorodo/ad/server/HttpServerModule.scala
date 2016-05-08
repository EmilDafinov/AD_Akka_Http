package com.emiliorodo.ad.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.emiliorodo.ad.configuration.ApplicationConfigurationModule

/**
  * @author edafinov
  */
trait HttpServerModule {
  this: ApplicationConfigurationModule with MainRoutesModule =>

  lazy val serverInterface = config.getString("http.server.interface")
  lazy val serverPort = config.getInt("http.server.port")

}
