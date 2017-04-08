package gd.inu.storedqf.text

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex

import scala.util.matching.Regex

/**
  * Created by henry on 4/7/17.
  */

trait ExtracCueidAsCssClass {

  lazy val className = """\w+(?=-\d*)""".r.findAllMatchIn("agent0-21233").foldLeft(""){_ + _}
}

object ReplaceAllStartTag {

  type Tag = String Refined MatchesRegex[W.`"<[A-Za-z0-9.]+>"`.T]

  implicit class cast(v: String) {
    def cast: Tag = {
      //require(v == 0 || v == 1, "0 or 1 accepted only")
      Refined.unsafeApply(v)
    }
  }
}

trait ReplaceAllStartTag {

  import ReplaceAllStartTag._

  private val targetTag = """<\w+\b>""".r

  val origin: String

  val newTag: Tag

  lazy val matches: Regex.MatchIterator = targetTag.findAllIn(origin)

  def result: String = origin.replaceAll(s"$targetTag", newTag.value)

}





