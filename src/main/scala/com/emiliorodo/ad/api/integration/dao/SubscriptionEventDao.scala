package com.emiliorodo.ad.api.integration.dao

import oauth.signpost.basic.DefaultOAuthConsumer
import dispatch._
import java.net.URL

import scala.concurrent.ExecutionContext
import scala.xml.XML

/**
  * @author edafinov
  */
class SubscriptionEventDao(consumerKey: String, consumerSecret: String)(implicit ec: ExecutionContext) {

  val mockResponseResolver: String => Future[String] = {
    url => Future {
      <event>
        <type>SUBSCRIPTION_ORDER</type>
        <marketplace>
          <partner>ACME</partner>
          <baseUrl>https://www.acme-marketplace.com</baseUrl>
        </marketplace>
        <creator>
          <email>andysen@gmail.com</email>
          <firstName>Andy</firstName>
          <lastName>Sen</lastName>
          <openId>https://www.acme-marketplace.com/openid/id/a11a7918-bb43-4429-a256-f6d729c71033</openId>
          <language>en</language>
        </creator>
        <payload>
          <company>
            <uuid>d15bb36e-5fb5-11e0-8c3c-00262d2cda03</uuid>
            <email>admin@example.com</email>
            <name>Example Company</name>
            <phoneNumber>1-415-555-1212</phoneNumber>
            <website>www.appdirect.com</website>
          </company>
          <order>
            <editionCode>BASIC</editionCode>
            <item>
              <quantity>10</quantity>
              <unit>USER</unit>
            </item>
            <item>
              <quantity>15</quantity>
              <unit>MEGABYTE</unit>
            </item>
          </order>
        </payload>
      </event>.toString
    }
  }

  def getSubscriptionOrder(eventUrl: String,
                           resolveRequest: String => Future[String] = eventUrl => Http(url(eventUrl).GET OK as.String))
  : Future[SubscriptionOrder] = {

    new URL(eventUrl)
    val signedUrl = new DefaultOAuthConsumer(
      consumerKey,
      consumerSecret
    ).sign(eventUrl)

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
}

case class SubscriptionOrder(company: Company, creator: SubscriptionOrderCreator, items: Seq[SubscriptionOrderItem] = Seq.empty  )

case class SubscriptionOrderCreator(firstName: String, lastName: String, openId: String)

case class SubscriptionOrderItem(quantity: Int, unit: String)

case class Company(uuid: String, name: String, email: String)
