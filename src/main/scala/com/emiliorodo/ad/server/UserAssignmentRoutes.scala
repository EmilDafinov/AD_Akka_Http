package com.emiliorodo.ad.server

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

/**
  * @author edafinov
  */
trait UserAssignmentRoutes {
  val userAssignmentRoutes: Route =
    (pathPrefix("user") & parameter("eventUrl") & get) { eventUrl =>
        path("assign") {
          complete {
            "User Assigned"
          }
        } ~
        path("unassign") {
          complete {
            "User Unassigned"
          }
        }
    }
}
