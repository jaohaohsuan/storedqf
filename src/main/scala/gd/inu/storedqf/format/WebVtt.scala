package gd.inu.storedqf.format

import scala.util.matching.Regex

/**
  * Created by henry on 4/7/17.
  */
object WebVtt {



}

trait WebVtt {
  def vttConversation(vttOrigin: String): Option[Cue] = {
    val pattern: Regex = """(.+-\d+)\s+(\S+\s-->\s\S+)\s+([\s\S]*$)""".r
    vttOrigin match {
      case pattern(cueid, time, text) => Some(Cue(cueid, time, text))
      case _ => None
    }
  }
}

case class Cue(cueid: String, time: String, text: String)
