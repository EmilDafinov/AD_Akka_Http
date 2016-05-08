package com.emiliorodo.ad.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.emiliorodo.ad.configuration.ApplicationConfigurationModule

/**
  * @author edafinov
  */
trait MainRoutesModule {
  this: ApplicationConfigurationModule with ADIntegrationRoutesModule=>

  lazy val webAppRootDirectory = config.getString("webapp.root.directory")

  private val staticContentRoute: Route = getFromResourceDirectory(webAppRootDirectory) ~
    path("") {
      getFromResource("webapp/index.html")
    }
  private val testRoute: Route = path("ping") {
    get {
      complete("pong")
    }
  }
  val baseRoute: Route =
    staticContentRoute ~
    testRoute ~
    adIntegrationRoutesBase

}
