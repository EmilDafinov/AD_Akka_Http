package com.emiliorodo.ad.api

import com.google.common.base.CaseFormat

/**
  *
  * @author edafinov
  */

case class ADApiException( private val errorCode: ErrorCode = UnknownError,
                           errorMessage: String = "An unknown error has occurred") extends Exception(errorMessage) {
  def getErrorCode = errorCode.errorCodeString
}

/**
  * Case objects representing the various error codes we return to the AppDirect platform.
  * Keep in mind that the string value of the error code returned for each [[ErrorCode]]
  */

sealed trait ErrorCode {
  /**
    * The return value of this method must match the error codes defined at:
    * http://info.appdirect.com/developers/docs/event_references/api_error_code
    * @return The string representation of the [[ErrorCode]], as expected by the AppDirect platform
    */
  def errorCodeString = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.toString)
}

case object UnknownError extends ErrorCode
case object UserAlreadyExists extends ErrorCode
case object UserNotFound extends ErrorCode
case object AccountNotFound extends ErrorCode
case object MaxUsersReached extends ErrorCode
case object Unauthorized extends ErrorCode
case object OperationCancelled extends ErrorCode
case object ConfigurationError extends ErrorCode
case object InvalidResponse extends ErrorCode
case object Pending extends ErrorCode


