package com.emiliorodo.ad.db

import com.emiliorodo.ad.ApplicationContext
import com.emiliorodo.ad.api.integration.dao.{Company, SubscriptionOrder, SubscriptionOrderCreator}
import com.emiliorodo.ad.util.UnitTestSpec

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author edafinov
  */
class SubscriptionDaoIntegrationTest extends UnitTestSpec {

  behavior of "SubscriptionDaoIntegrationTest"

  ignore should "createSubscription" in {
    //Given
    val ac = new ApplicationContext {}

    val testedDao = ac.subscriptionDao
    val mockOrder = SubscriptionOrder(
      company = Company(
        uuid = "fsdfsdfs",
        name = "BigCorp",
        email = "zeMail@hotmail.com"
      ),
      creator = SubscriptionOrderCreator(
        "first","last","df sdhsdg"
      )
    )
    //When
    val resultFuture = testedDao.createSubscription(mockOrder)
    val result = Await.result(resultFuture, Duration.Inf)

    //Then
    val a = 5

  }

}
