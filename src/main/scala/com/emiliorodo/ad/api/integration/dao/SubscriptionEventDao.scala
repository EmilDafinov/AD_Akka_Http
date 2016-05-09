package com.emiliorodo.ad.api.integration.dao

import com.emiliorodo.ad.readResourceFile
import com.emiliorodo.ad.security.OAuthTool
import dispatch._

import scala.concurrent.ExecutionContext
import scala.xml.XML

/**
  * @author edafinov
  */
class SubscriptionEventDao(signer: OAuthTool)(implicit ec: ExecutionContext) {

  val mockSubscriptionOrderResponseResolver: String => Future[String] = {
    url => Future {
      readResourceFile("/mockADResponses/MockSubscriptionOrder.xml")
    }
  }

  val mockCancelSubscriptionResponseResolver: String => Future[String] = {
    url => Future {
      readResourceFile("/mockADResponses/MockCancelSubscription.xml")
    }
  }

  def getSubscriptionOrderEvent(eventUrl: String,
                                resolveRequest: String => Future[String] = eventUrl => Http(url(eventUrl).GET OK as.String))
  : Future[SubscriptionOrder] = {

    val signedUrl = signer.sign(eventUrl)
    resolveRequest(signedUrl) map toParsedSubscriptionOrder
  }

  private def toParsedSubscriptionOrder(responseBody: String): SubscriptionOrder = {
    val xmlResponseBody = XML.loadString(responseBody)
    val creatorElement = xmlResponseBody \ "creator"
    val creator = SubscriptionOrderCreator(
      firstName = (creatorElement \ "firstName").text,
      lastName = (creatorElement \ "lastName").text,
      openId = (creatorElement \ "openId").text
    )

    val companyElement = xmlResponseBody \ "payload" \ "company"
    val company = Company(
      uuid = (companyElement \ "uuid").text,
      name = (companyElement \ "name").text,
      email = (companyElement \ "email").text
    )
    val items = (xmlResponseBody \ "payload" \ "order" \\ "item") map { xmlItem =>
      SubscriptionOrderItem(
        quantity = (xmlItem \ "quantity").text.toInt,
        unit = (xmlItem \ "unit").text
      )
    }
    SubscriptionOrder(company, creator, items)
  }

  def getCancelSubscriptionEvent(eventUrl: String,
                                 resolveRequest: String => Future[String] = eventUrl => Http(url(eventUrl).GET OK as.String)) = {

    val signedUrl = signer.sign(eventUrl)
    resolveRequest(signedUrl) map toParsedAccountIdentifier
  }

  private def toParsedAccountIdentifier(responseBody: String): String = {
    val accountIdentifierElement = XML.loadString(responseBody) \ "payload" \ "account" \ "accountIdentifier"
    accountIdentifierElement.text
  }
}

case class SubscriptionOrder(company: Company, creator: SubscriptionOrderCreator, items: Seq[SubscriptionOrderItem] = Seq.empty  )

case class SubscriptionOrderCreator(firstName: String, lastName: String, openId: String)

case class SubscriptionOrderItem(quantity: Int, unit: String)

case class Company(uuid: String, name: String, email: String)
