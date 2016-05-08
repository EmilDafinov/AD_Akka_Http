package com.emiliorodo.ad.db

import com.emiliorodo.ad.api.integration.dao.SubscriptionOrder
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext

/**
  * @author edafinov
  */
class SubscriptionDao(db: Database) {

  class FeaturesNew(tag: Tag) extends Table[(String, String)](tag, "subscriptions") {
    def companyName = column[String]("company_name")
    def creatorName = column[String]("creator")
    def * = (companyName, creatorName)
  }

  val subscriptionsTable = TableQuery[FeaturesNew]

  def createSubscription(order: SubscriptionOrder)(implicit ec: ExecutionContext) = {
//    val runInsertion = db.run(
//      DBIO.seq(
//        subscriptionsTable ++= Seq(
//          (order.company.name, order.creator.lastName),
//          sql"SELECT account_identifier FROM public.subscriptions WHERE company_name = #$order.comany"
//        )
//      )
//    )
    db.run(
      sql"""
         INSERT INTO public.subscriptions (company_name, creator)
         VALUES (#${order.company.name}, #${order.creator.lastName})
         RETURNING account_identifier;
         """.as[String]
    ) map(_.head)
  }
}
