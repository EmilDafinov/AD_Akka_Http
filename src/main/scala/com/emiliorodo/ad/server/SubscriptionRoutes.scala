package com.emiliorodo.ad.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.emiliorodo.ad.AkkaDependenciesModule
import com.emiliorodo.ad.api.integration.dao.SubscriptionDaoModule
import com.emiliorodo.ad.db.DatabaseModule
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import com.emiliorodo.ad.api.ADApiException
/**
  * @author edafinov
  */
trait SubscriptionRoutes {
  this: SubscriptionDaoModule with AkkaDependenciesModule with DatabaseModule =>

  lazy val subscriptionEventsRoutes: Route =
    (pathPrefix("subscription") & parameter("eventUrl") & get) { eventUrl =>
      createSubscription(eventUrl) ~
      cancelSubscription(eventUrl) ~
        path("change") {
          complete {
            throw new ADApiException(errorMessage = "Route not implemented")
          }
        } ~
        path("assign") {
          complete {
            throw new ADApiException(errorMessage = "Route not implemented")
          }
        } ~
        path("update") {
          complete {
            throw new ADApiException(errorMessage = "Route not implemented")
          }
        }
    }

  def cancelSubscription(eventUrl: String): Route = {
    path("cancel") {
      complete {
        val resp = for {
        //TODO: Remove the second argument, it is there to ensure a mock response is generated for the call
          accountIdToCancel <- subscriptionEventDao.getCancelSubscriptionEvent(eventUrl/*, subscriptionEventDao.mockCancelSubscriptionResponseResolver*/)
          cancelledSubscriptionId <- subscriptionDao.cancelSubscription(accountIdToCancel)
        } yield None
        resp map SuccessfulNonInteractiveResponse("Subscription Cancelled")
      }
    }
  }

  def createSubscription(eventUrl: String): Route = {
    path("order") {
      complete {

        //TODO: Remove the second argument, it is there to ensure a mock response is generated for the call
        val subscriptionOrderAccountId = for {
          subscriptionOrder <- subscriptionEventDao.getSubscriptionOrderEvent(eventUrl/*, subscriptionEventDao.mockSubscriptionOrderResponseResolver*/)
          accountIdentifier <- subscriptionDao.createSubscription(subscriptionOrder)
        } yield Option(accountIdentifier)
        subscriptionOrderAccountId map SuccessfulNonInteractiveResponse("Account creation successful")
      }
    }
  }
}
