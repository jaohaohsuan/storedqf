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

  type Cueid = String Refined MatchesRegex[W.`"^[A-Za-z0-9_]+-[0-9]+"`.T]
  type Role = String Refined MatchesRegex[W.`"^[A-Za-z0-9_]+"`.T]

  implicit class castableLiteral(v: String) {

    def cueid: Cueid = Refined.unsafeApply(v)
    def role: Role = Refined.unsafeApply(v)

  }

  trait CueProperties extends Any {
    def cueid(raw: String): Cueid = """\w+-\d+(?=[\s\S]*)""".r.findAllMatchIn(raw).foldLeft("")(_ + _).cueid
    def role(raw: String): Role = """\w+(?=-\d+?[\s\S]*)""".r.findAllMatchIn(raw).foldLeft("")(_ + _).role
  }

  class CueVal(val raw: String) extends AnyVal with CueProperties {
    override def toString: String = {
      val p = """(\w+-\d+[ ])([\s\S]*)""".r("id","body")
      raw match {
        case p(cuid, body) => s"${cuid.trim}\n$body"
        case _ => raw
      }
    }
    def lines = s"${this}".split("""\n""")
  }

}



trait WebVtt {
  def vttConversation(vttOrigin: String): Option[Cue] = {
    lazy val pattern = {
      val rp: String Refined eu.timepit.refined.string.Regex = """(.+-\d+)\s+(\S+\s-->\s\S+)\s+([\s\S]*$)"""

      rp.value.r
    }

    vttOrigin match {
      case pattern(cueid, time, text) => Some(Cue(cueid, time, text))
      case _ => None
    }
  }
}


case class Cue(cueid: String, time: String, text: String)
