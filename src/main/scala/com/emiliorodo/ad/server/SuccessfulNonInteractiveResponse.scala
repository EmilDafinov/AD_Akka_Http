package com.emiliorodo.ad.server

import com.emiliorodo.ad.api.ADApiException

import scala.xml.NodeSeq

/**
  * Object representing the accepted response formats to AD events
  * @author edafinov
  */
object SuccessfulNonInteractiveResponse {
  
  def apply(message: String)(accountIdentifier: String): NodeSeq = {
    <result>
      <success>true</success>
      <message>{message}</message>
      <accountIdentifier>{accountIdentifier}</accountIdentifier>
    </result>
  }
}

object UnidentifiedSuccessfulNonInteractiveResponse {

  def apply(message: String): NodeSeq = {
    <result>
      <success>true</success>
      <message>{message}</message>
    </result>
  }
}

object FailureNonInteractiveResponse {
  def apply(exception: ADApiException): NodeSeq = {
    <result>
      <success>false</success>
      <errorCode>{exception.getErrorCode}</errorCode>
      <message>{exception.errorMessage}</message>
    </result>
  }
}
