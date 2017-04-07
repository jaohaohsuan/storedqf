package gd.inu.storedqf.text

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex

import scala.util.matching.Regex

/**
  * Created by henry on 4/7/17.
  */

trait ReplaceAllStartTag {

  type Tag = String Refined MatchesRegex[W.`"<[A-Za-z0-9.]+>"`.T]

  private val targetTag = """<\w+\b>""".r

  val origin: String

  val newTag: Tag

  lazy val matches: Regex.MatchIterator = targetTag.findAllIn(origin)

  def result: String = origin.replaceAll(s"$targetTag", newTag.value)

}





