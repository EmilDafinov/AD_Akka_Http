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
    (pathPrefix("subscription") & parameter("eventUrl") & get) { implicit eventUrl =>
      order ~
      cancel ~
      change ~
      assign ~
      update
    }

  private def order(implicit eventUrl: String): Route = {
    path("order") {
      complete {

        //TODO: Remove the second argument, it is there to ensure a mock response is generated for the call
        // AR: Why you need Option(accountIdentifier), rather just accountIdentifier?
        val subscriptionOrderAccountIdFuture = for {
          subscriptionOrder <- subscriptionEventDao.getSubscriptionOrderEvent(eventUrl, subscriptionEventDao.mockSubscriptionOrderResponseResolver)
          accountIdentifier <- subscriptionDao.createSubscription(subscriptionOrder)
        } yield accountIdentifier
        
        subscriptionOrderAccountIdFuture map SuccessfulNonInteractiveResponse("Account creation successful")

      }
    }
  }

  private def cancel(implicit eventUrl: String): Route = {
    path("cancel") {
      complete {
        val accountIdToCancel = subscriptionEventDao.getCancelSubscriptionEvent(eventUrl, subscriptionEventDao.mockCancelSubscriptionResponseResolver)
        accountIdToCancel foreach subscriptionDao.cancelSubscription
         
          UnidentifiedSuccessfulNonInteractiveResponse("Subscription Cancelled")
      }
    }
  }
  
  private def update: Route = {
    path("update") {
      complete {
        throw ADApiException(errorMessage = "Route not implemented")
      }
    }
  }

  private def assign: Route = {
    path("assign") {
      complete {
        throw ADApiException(errorMessage = "Route not implemented")
      }
    }
  }

  private def change: Route = {
    path("change") {
      complete {
        throw ADApiException(errorMessage = "Route not implemented")
      }
    }
  }

  

}
