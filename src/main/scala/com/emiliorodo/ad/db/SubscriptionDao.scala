package com.emiliorodo.ad.db

import com.emiliorodo.ad.api.integration.dao.SubscriptionOrder
import com.emiliorodo.ad.api.{ADApiException, AccountNotFound, UserAlreadyExists}
import com.typesafe.scalalogging.StrictLogging
import org.postgresql.util.PSQLException
import slick.driver.PostgresDriver.api._

import scala.concurrent.{ExecutionContext, Future}

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
class SubscriptionDao(db: Database) extends StrictLogging {
  def cancelSubscription(accountIdToCancel: String)
                        (implicit ec: ExecutionContext): Future[Int] = {
    db.run(
      sql"""
         DELETE FROM public.subscriptions
         WHERE account_identifier = '#$accountIdToCancel'
         """.asUpdate
    ) map { deletedRecords =>
      if (deletedRecords == 0)
        throw new ADApiException(
          errorCode = AccountNotFound,
          errorMessage = "The subscription's account Identifier was not found"
        )
      deletedRecords
    }
  }

  def createSubscription(order: SubscriptionOrder)
                        (implicit ec: ExecutionContext): Future[String] = {
    db.run(
      sql"""
         INSERT INTO public.subscriptions (company_name, creator)
         VALUES ('#${order.company.name}', '#${order.creator.lastName}')
         RETURNING account_identifier;
         """.as[String]
    ) map(_.head) recover {
      case dbException :PSQLException if dbException.getSQLState == "23505"=>
        throw new ADApiException(errorCode = UserAlreadyExists, errorMessage = "Account already exists")
    }
  }
}
