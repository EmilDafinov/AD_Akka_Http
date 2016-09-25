package com.emiliorodo.ad.server

import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.emiliorodo.ad.AkkaDependenciesModule
import com.emiliorodo.ad.api.ADApiException
import com.typesafe.scalalogging.StrictLogging

import scala.util.control.NonFatal
/**
  * @author edafinov
  */
trait ADIntegrationRoutesModule extends StrictLogging  {
  this: SubscriptionRoutes with UserAssignmentRoutes with AkkaDependenciesModule =>
  
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
    case NonFatal(anyOtherException) =>
      logger.error("Client call failed", anyOtherException)
      complete {
        FailureNonInteractiveResponse(new ADApiException)
      }
  }
}
