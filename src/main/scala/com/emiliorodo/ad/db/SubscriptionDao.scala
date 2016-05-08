package com.emiliorodo.ad.db

import com.emiliorodo.ad.api.integration.dao.SubscriptionOrder
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext

/**
  * This class interacts with the database. Using plain SQL queries is a bit simplistic of an approach,
  * but it works for simple cases like this;
  * Normally Slick provides more elegant ways of querying the db
  * One good thing about the library, as mentioned here:
  *
  * http://slick.lightbend.com/doc/3.1.1/sql.html
  *
  * is that even when we are splicing variables in our SQL strings as in this case,
  * the result SQL string is not a simple concatenation. Therefore, we are protected from
  * SQL injection attacks
  *
  * @author edafinov
  */
class SubscriptionDao(db: Database) {
  def cancelSubscription(accountIdToCancel: String)(implicit ec: ExecutionContext) = {
    db.run(
      sql"""
         DELETE FROM public.subscriptions
         WHERE account_identifier = '#$accountIdToCancel'
         """.asUpdate
    )
  }

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
