package com.emiliorodo.ad.api.integration.dao

import com.emiliorodo.ad.util.UnitTestSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.language.postfixOps
/**
  * @author edafinov
  */
class SubscriptionEventDaoTest extends UnitTestSpec {
  behavior of "SubscriptionDao"

  def readTestResource(testResource: String): String = {
    Source.fromURL(getClass.getResource(testResource)) mkString
  }

  val testedSubscriptionDao = new SubscriptionEventDao(consumerKey = "dummyKey", consumerSecret = "dummySecret")

  it should "parse the order correctly" in  {

    //Given
    val mockResponse = readTestResource("/SubscriptionOrder.xml")
    val mockResponseResolver: String => Future[String] = {_ => Future{mockResponse}}

    //When
    val order = Await.result(
      testedSubscriptionDao.getSubscriptionOrder("mockUrl", mockResponseResolver),
      Duration.Inf
    )

    //Then
    order.creator shouldEqual SubscriptionOrderCreator(
      firstName = "Andy",
      lastName = "Sen",
      openId = "https://www.acme-marketplace.com/openid/id/a11a7918-bb43-4429-a256-f6d729c71033"
    )

    order.items should contain theSameElementsInOrderAs List(
      SubscriptionOrderItem(quantity = 10, unit = "USER"),
      SubscriptionOrderItem(quantity = 15, unit = "MEGABYTE")
    )
  }


}
