package com.emiliorodo.ad.server

import com.emiliorodo.ad.api.ADApiException

/**
  * Object representing the accepted response formats to AD events
  * @author edafinov
  */
object SuccessfulNonInteractiveResponse {
  def apply(message: String)(accountIdentifier: Option[String] = None) = {
    <result>
      <success>true</success>
      <message>{message}</message>
      {if (accountIdentifier.isDefined) <accountIdentifier>{accountIdentifier.get}</accountIdentifier>}
    </result>
  }
}

object FailureNonInteractiveResponse {
  def apply(exception: ADApiException) = {
    <result>
      <success>false</success>
      <errorCode>{exception.getErrorCode}</errorCode>
      <message>{exception.errorMessage}</message>
    </result>
  }
}
