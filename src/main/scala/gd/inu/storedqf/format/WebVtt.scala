package gd.inu.storedqf.format

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined

import scala.util.matching.Regex
import eu.timepit.refined.auto._
import eu.timepit.refined.string.MatchesRegex

/**
  * Created by henry on 4/7/17.
  */
object WebVtt {

  class CueVoiceMissing extends Exception("<v ???>Hello world</v>")

  type Cueid = String Refined MatchesRegex[W.`"^[A-Za-z0-9_]+-[0-9]+"`.T]
  type Role = String Refined MatchesRegex[W.`"^[A-Za-z0-9_]+"`.T]

  trait CueProperties extends Any {
    def cueid(raw: String): Cueid = Refined.unsafeApply("""\w+-\d+(?=[\s\S]*)""".r.findAllMatchIn(raw).foldLeft("")(_ + _))
    def role(raw: String): Role = Refined.unsafeApply("""\w+(?=-\d+?[\s\S]*)""".r.findAllMatchIn(raw).foldLeft("")(_ + _))
  }
}

class CueString(val raw: String) extends AnyVal {
  override def toString: String = {
    """\w+-\d+\s*(?=[\s\S]*)""".r replaceAllIn(raw, m => {
      s"${m.matched.trim}\n"
    })
  }

  def styled = ("""<[\s\S]*\/+\w*>$""".r findFirstMatchIn raw).isDefined

  def text = lines.toSeq.drop(2).foldLeft("")(_ + _)
  def time = lines(1)

  def lines = s"${this}".split("""\n""")
}

class WebVtt(val raw: String) extends AnyVal {
  override def toString: String = raw
}
