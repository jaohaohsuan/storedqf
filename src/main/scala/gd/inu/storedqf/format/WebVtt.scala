package gd.inu.storedqf.format

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.string.MatchesRegex
import gd.inu.storedqf.format.WebVtt.{Cueid, Role}
import gd.inu.storedqf.text.{HighlightFragment, Highlighter}
import org.json4s.JsonAST
import org.json4s.JsonAST.{JArray, JObject, JString, JValue}

/**
  * Created by henry on 4/7/17.
  */
object WebVtt {

  type Cueid = String Refined MatchesRegex[W.`"^[A-Za-z0-9_]+-[0-9]+"`.T]
  type Role = String Refined MatchesRegex[W.`"^[A-Za-z0-9_]+"`.T]

  def fromJson(x: JValue) = {

    // validation
    (for(
      JsonAST.JArray(arr) <- (x \ "_source" \ "vtt").toOption
      if arr.nonEmpty
    ) yield true).getOrElse(false)

    (x \ "_source" \ "vtt" \\ classOf[JString]).map(new CueString(_)).foldLeft("WEBVTT\n"){ (ac, el) => ac ++ s"\n$el"}
  }

}

class CueString(val raw: String) extends AnyVal {
  override def toString: String = {
    """\w+-\d+\s*(?=[\s\S]*)""".r replaceAllIn(raw, m => {
      s"${m.matched.trim}\n"
    })
  }

  def text = lines.toSeq.drop(2).foldLeft("")(_ + _)
  def time = lines(1)

  def lines = s"${this}".split("""\n""")
}

trait CueProperties extends Any {
  def cueid(raw: String): Cueid = Refined.unsafeApply("""\w+-\d+(?=[\s\S]*)""".r.findAllMatchIn(raw).foldLeft("")(_ + _))
  def role(raw: String): Role = Refined.unsafeApply("""\w+(?=-\d+?[\s\S]*)""".r.findAllMatchIn(raw).foldLeft("")(_ + _))
}

class WebVtt(val raw: String) extends AnyVal {
  override def toString: String = raw
}

class PercolateSearchResult(val json: JValue) {

  import Highlighter._

  private val highlightFragments = (json \\ classOf[JString]).map(new HighlightFragment(_))

  def mergeWith(webvtt: String) = {
    highlightFragments.foldLeft(webvtt){ (ac, fragment) =>
      new WebVtt(ac).substituteWith(fragment)
    }
  }

}
