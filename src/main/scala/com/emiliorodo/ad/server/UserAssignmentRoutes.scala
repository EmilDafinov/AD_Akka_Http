package com.emiliorodo.ad.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.emiliorodo.ad.api.ADApiException

/**
  * @author edafinov
  */
trait UserAssignmentRoutes {
  val userAssignmentRoutes: Route =
    (pathPrefix("user") & parameter("eventUrl") & get) { eventUrl =>
        path("assign") {
          complete {
            throw new ADApiException(errorMessage = "Route not implemented")
          }
        } ~
        path("unassign") {
          complete {
            throw new ADApiException(errorMessage = "Route not implemented")
          }
        }
    }
}
