package com.emiliorodo.ad.server

import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.emiliorodo.ad.AkkaDependenciesModule
import com.emiliorodo.ad.api.ADApiException
import com.emiliorodo.ad.api.integration.dao.SubscriptionDaoModule
import com.emiliorodo.ad.db.DatabaseModule
import com.typesafe.scalalogging.StrictLogging
/**
  * @author edafinov
  */
trait ADIntegrationRoutesModule extends StrictLogging with SubscriptionRoutes with UserAssignmentRoutes {
  this: SubscriptionDaoModule with AkkaDependenciesModule with DatabaseModule =>

  lazy val adIntegrationRoutes: Route =
    handleExceptions(adIntegrationRoutesErrorHandler) {

      pathPrefix("ad" / "events") {
        subscriptionEventsRoutes ~
        userAssignmentRoutes
      }
    }

  lazy val adIntegrationRoutesErrorHandler = ExceptionHandler {
    case adApiException: ADApiException =>
      complete {
        FailureNonInteractiveResponse(adApiException)
      }
    case anyOtherException =>
      logger.error("Client call failed", anyOtherException)
      complete {FailureNonInteractiveResponse(new ADApiException)}
  }
}
