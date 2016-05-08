package com.emiliorodo.ad.db

import com.emiliorodo.ad.api.integration.dao.SubscriptionOrder
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext

/**
  * @author edafinov
  */
class SubscriptionDao(db: Database) {

  def createSubscription(order: SubscriptionOrder)(implicit ec: ExecutionContext) = {
    db.run(
      sql"""
         INSERT INTO public.subscriptions (company_name, creator)
         VALUES ('#${order.company.name}', '#${order.creator.lastName}')
         RETURNING account_identifier;
         """.as[String]
    ) map(_.head)
  }
}
