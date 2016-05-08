package com.emiliorodo

import scala.io.Source

/**
  * @author edafinov
  */
package object ad {

  def readResourceFile(testResource: String): String = {
    Source.fromURL(getClass.getResource(testResource)) mkString
  }
}
