package com.emiliorodo.ad.api.integration.dao

import com.emiliorodo.ad.util.UnitTestSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import com.emiliorodo.ad.readResourceFile
import com.emiliorodo.ad.security.OAuthTool
import org.mockito.Mockito.verify
import org.mockito.Mockito.when
import org.mockito.Matchers.{eq => mockEq, any}

import scala.language.postfixOps
/**
  * @author edafinov
  */
class SubscriptionEventDaoTest extends UnitTestSpec {
  behavior of "SubscriptionDao"

  val mockSigner = mock[OAuthTool]
  val testedSubscriptionDao = new SubscriptionEventDao(mockSigner)

  it should "parse the order correctly" in  {

    //Given
    val mockResponse = readResourceFile("/SubscriptionOrder.xml")
    val mockResponseResolver: String => Future[String] = {_ => Future{mockResponse}}

    //When
    val order = Await.result(
      testedSubscriptionDao.getSubscriptionOrderEvent("mockUrl", mockResponseResolver),
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

  it should "sign order requests" in {
    //Given
    val mockResponse = readResourceFile("/SubscriptionOrder.xml")
    var actualEventURLCalled = ""
    val mockResponseResolver: String => Future[String] = {
      url => {
        actualEventURLCalled = url
        Future{mockResponse}
      }
    }
    val mockEventUrl = "mockUrl"
    val mockSignedEventUrl = "signedMockEvent"

    when(mockSigner.sign(mockEq(mockEventUrl)))
      .thenReturn(mockSignedEventUrl)

    //When
    Await.result(
      testedSubscriptionDao.getSubscriptionOrderEvent(mockEventUrl, mockResponseResolver),
      Duration.Inf
    )

    //Then
    actualEventURLCalled shouldEqual mockSignedEventUrl
  }

  it should "sign order cancel requests" in {
    //Given
    val mockResponse = readResourceFile("/SubscriptionOrder.xml")
    var actualEventURLCalled = ""
    val mockResponseResolver: String => Future[String] = {
      url => {
        actualEventURLCalled = url
        Future{mockResponse}
      }
    }
    val mockEventUrl = "mockUrl"
    val mockSignedEventUrl = "signedMockEvent"

    when(mockSigner.sign(mockEq(mockEventUrl)))
      .thenReturn(mockSignedEventUrl)

    //When
    Await.result(
      testedSubscriptionDao.getCancelSubscriptionEvent(mockEventUrl, mockResponseResolver),
      Duration.Inf
    )

    //Then
    actualEventURLCalled shouldEqual mockSignedEventUrl
  }
}
