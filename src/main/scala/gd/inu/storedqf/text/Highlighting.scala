package gd.inu.storedqf.text

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import gd.inu.storedqf.format.WebVtt.{CueProperties, CueVoiceMissing}
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
    val searchTarget = new Regex(s"(?<=${fragment.head}\\n\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\s-->\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\n)(<\\w*\\s\\w*.)([\\s\\S]*?)(<\\/\\w*.)", "<", "txt", ">")
    s"${searchTarget replaceAllIn (s"$doc", m => s"${m group "<"}${fragment.result}${m group ">"}")}"
  }

}



class CueMarker(val raw: String) extends CueProperties with ReplaceAllStartTag {

  val content      = """(?<=\w+-\d+\s+)[\s\S]*""".r.findFirstIn(raw).getOrElse("")

  override val origin      = content
  override val newTag: Tag = Refined.unsafeApply(s"<c.${role(raw).value}>")

  def replace(cue: CueString): CueString = {
    if (!cue.styled)
      throw new CueVoiceMissing
    new CueString(proceed(cue.raw))
  }

  def proceed(vtt: String): String = {
    val searchTarget = new Regex(s"(?<=${cueid(raw)}\\n\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\s-->\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\n)(<\\w*\\s\\w*.)([\\s\\S]*?)(<\\/\\w*.)", "<", "txt", ">")
    s"${searchTarget replaceAllIn (s"$vtt", m => s"${m group "<"}$result${m group ">"}")}"
  }

}

object ReplaceAllStartTag {

  type Tag = String Refined MatchesRegex[W.`"<[A-Za-z0-9.]+>"`.T]

}

class HighlightFragment(val fragment:String) extends CueProperties with ReplaceAllStartTag {

  val head: String = cueid(fragment)

  val newTag: Tag = Refined.unsafeApply(s"<c.${role(fragment).value}>")
  val origin = """(?<=\w+-\d+\s+)[\s\S]*""".r.findFirstIn(fragment).getOrElse("")
}

trait ReplaceAllStartTag {

  import ReplaceAllStartTag._

  private val targetTag = """<\w+\b>""".r

  val origin: String

  val newTag: Tag

  lazy val matches: Regex.MatchIterator = targetTag.findAllIn(origin)

  def result: String = origin.replaceAll(s"$targetTag", newTag.value)

}





