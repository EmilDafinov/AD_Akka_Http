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
trait ADIntegrationRoutesModule extends StrictLogging {
  this: SubscriptionDaoModule with AkkaDependenciesModule with DatabaseModule =>

  lazy val adIntegrationRoutesBase: Route =
    handleExceptions(adIntegrationRoutesErrorHandler) {
      pathPrefix("ad" / "events") {
        subscriptionEventsRoutes
      }
    }

  lazy val adIntegrationRoutesErrorHandler = ExceptionHandler {
    case adApiException: ADApiException =>
      complete {
        failureNonInteractiveResponse(adApiException)
      }
    case anyOtherException =>
      logger.error("Client call failed", anyOtherException)
      complete {failureNonInteractiveResponse(new ADApiException)}
  }

  private lazy val subscriptionEventsRoutes: Route =
  (pathPrefix("subscription") & parameter("eventUrl") & get) { eventUrl =>
    path("order") {
      complete {

        //TODO: Remove the second argument, it is there to ensure a mock response is generated for the call
        val processedOrderAccountId = for {
          subscriptionOrder <- subscriptionEventDao.getSubscriptionOrderEvent(eventUrl, subscriptionEventDao.mockSubscriptionOrderResponseResolver)
          accountIdentifier <- subscriptionDao.createSubscription(subscriptionOrder)
        } yield Option(accountIdentifier)
        processedOrderAccountId map toSuccessfulNonInteractiveResponse("Account creation successful")
      }
    } ~
    path("cancel") {
      complete {
        val resp = for {
          accountIdToCancel <- subscriptionEventDao.getCancelSubscriptionEvent(eventUrl, subscriptionEventDao.mockCancellSubscriptionResponseResolver)
          cancelledSubscriptionId <- subscriptionDao.cancelSubscription(accountIdToCancel)
        } yield None
        resp map toSuccessfulNonInteractiveResponse("Subscription Cancelled")
      }
    } ~
    path("change") {
      complete {
        "Subscription Changed"
      }
    } ~
    path("assign") {
      complete {
        "Subscription Assigned"
      }
    } ~
    path("update") {
      complete {
        "Subscription Updated"
      }
    }
  }

  private def toSuccessfulNonInteractiveResponse(message: String)(accountIdentifier: Option[String] = None) = {
    <result>
      <success>true</success>
      <message>{message}</message>
      {if (accountIdentifier.isDefined) <accountIdentifier>{accountIdentifier.get}</accountIdentifier>}
    </result>
  }

  private def failureNonInteractiveResponse(exception: ADApiException) = {
    <result>
      <success>false</success>
      <errorCode>{exception.getErrorCode}</errorCode>
      <message>{exception.errorMessage}</message>
    </result>
  }
}
