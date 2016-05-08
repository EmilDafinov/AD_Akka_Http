package com.emiliorodo.ad.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.emiliorodo.ad.AkkaDependenciesModule
import com.emiliorodo.ad.api.integration.dao.SubscriptionDaoModule
import com.emiliorodo.ad.db.DatabaseModule
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
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

  def cancelSubscription(eventUrl: String): Route = {
    path("cancel") {
      complete {
        val resp = for {
        //TODO: Remove the second argument, it is there to ensure a mock response is generated for the call
          accountIdToCancel <- subscriptionEventDao.getCancelSubscriptionEvent(eventUrl, subscriptionEventDao.mockCancellSubscriptionResponseResolver)
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
        val processedOrderAccountId = for {
          subscriptionOrder <- subscriptionEventDao.getSubscriptionOrderEvent(eventUrl, subscriptionEventDao.mockSubscriptionOrderResponseResolver)

          accountIdentifier <- subscriptionDao.createSubscription(subscriptionOrder)
        } yield Option(accountIdentifier)
        processedOrderAccountId map SuccessfulNonInteractiveResponse("Account creation successful")
      }
    }
  }
}
