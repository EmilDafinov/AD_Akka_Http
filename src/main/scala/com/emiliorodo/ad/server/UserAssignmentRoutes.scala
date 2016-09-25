package com.emiliorodo.ad.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.emiliorodo.ad.api.ADApiException

/**
  * @author edafinov
  */
trait UserAssignmentRoutes {
  val userAssignmentRoutes: Route =
    // AR: eventUrl must be valid URL, could we validate that? 
    (pathPrefix("user") & parameter("eventUrl") & get) { eventUrl =>
        assign ~
        unassign
    }

  def unassign: Route = {
    path("unassign") {
      complete {
        throw ADApiException(errorMessage = "Route not implemented")
      }
    }
  }

  private def assign: Route = {
    path("assign") {
      // AR: 
      complete {
        throw ADApiException(errorMessage = "Route not implemented")
      }
    }
  }
}
