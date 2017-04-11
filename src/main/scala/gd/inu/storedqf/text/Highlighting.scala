package gd.inu.storedqf.text

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import gd.inu.storedqf.format._
import gd.inu.storedqf.text.ReplaceAllStartTag.Tag

import scala.util.matching.Regex
import eu.timepit.refined.auto._
import gd.inu.storedqf.format.{CueString, WebVtt}

object Highlighter {

  implicit class webVttHighlighter(src: WebVtt) extends Highlighter[WebVtt] {
    val doc: String = src.raw
  }
  implicit class cueHighlighter(src: CueString) extends Highlighter[WebVtt] {
    val doc: String = src.raw
  }

}

trait Highlighter[A] {

  val doc: String

  def highlight(fragment: HighlightFragment): String = {
    val searchTarget = new Regex(s"(?<=${fragment.id}\\n\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\s-->\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\n)(<\\w*\\s\\w*.)([\\s\\S]*?)(<\\/\\w*.)", "<", "txt", ">")
    s"${searchTarget replaceAllIn (s"$doc", m => s"${m group "<"}${fragment.replace}${m group ">"}")}"
  }

}

object ReplaceAllStartTag {

  type Tag = String Refined MatchesRegex[W.`"<[A-Za-z0-9.]+>"`.T]

}

class HighlightFragment(val fragment:String) extends CueProperties with ReplaceAllStartTag {

  val id: String = cueid(fragment)

  val newTag: Tag = Refined.unsafeApply(s"<c.${role(fragment).value}>")
  val origin = """(?<=\w+-\d+\s+)[\s\S]*""".r.findFirstIn(fragment).getOrElse("")
}

trait ReplaceAllStartTag {

  import ReplaceAllStartTag._

  private val targetTag = """<\w+\b>""".r

  val origin: String

  val newTag: Tag

  lazy val matches: Regex.MatchIterator = targetTag.findAllIn(origin)

  def replace: String = origin.replaceAll(s"$targetTag", newTag.value)

}





