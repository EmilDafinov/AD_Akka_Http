package com.emiliorodo.ad

import akka.http.scaladsl.Http
import com.emiliorodo.ad.api.integration.dao.SubscriptionDaoModule
import com.emiliorodo.ad.configuration.ApplicationConfigurationModule
import com.emiliorodo.ad.db.DatabaseModule
import com.emiliorodo.ad.server._
import com.typesafe.scalalogging.StrictLogging

import scala.util.{Failure, Success}

/**
  * @author edafinov
  */
// AR: What is purpose of using traits composition here?
// ED: Dependency injection: the root application context should only load the required modules
trait ApplicationContext extends StrictLogging
  with ApplicationConfigurationModule
  with AkkaDependenciesModule
  with SubscriptionDaoModule
  with DatabaseModule
  with SubscriptionRoutes
  with UserAssignmentRoutes
  with ADIntegrationRoutesModule
  with MainRoutesModule

object Main extends App
  with ApplicationContext {

  lazy val serverInterface = config.getString("http.server.interface")
  lazy val serverPort = config.getInt("http.server.port")

  Http()
    .bindAndHandle(
      handler = baseRoute,
      interface = serverInterface,
      port = serverPort
    )
    .onComplete {
      case Success(serverBinding) =>
        logger.info(s"Server started on $serverInterface:$serverPort")
        sys.addShutdownHook {
          serverBinding.unbind()
          logger.info("Server stopped")
        }
      case Failure(exception) =>
        logger.error(s"Cannot start server on $serverInterface:$serverPort", exception)
        sys.addShutdownHook {
          logger.info("Server stopped")
        }
    }
}
