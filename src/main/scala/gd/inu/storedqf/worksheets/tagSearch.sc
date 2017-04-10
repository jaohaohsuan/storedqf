import gd.inu.storedqf.text.ReplaceAllStartTag

import scala.util.matching.Regex
import scala.util.matching.Regex._
import eu.timepit.refined.auto._
import gd.inu.storedqf.text.ReplaceAllStartTag.Tag


val vtt = """WEBVTT
            |
            |agent0-5188
            |00:00:05.188 --> 00:00:08.532
            |<v R1>很 高興 為您 服務 您好  </v>
            |
            |customer0-9400
            |00:00:09.400 --> 00:00:10.008
            |<v R0>欸 您好  </v>
            |
            |agent0-15956
            |00:00:15.956 --> 00:00:16.404
            |<v R1>嘿嘿  </v>""".stripMargin

val hlresult = """customer0-9400 欸 <c>您好</c> """
// split into
// customer0-9400
// 欸 <c>您好</c>
"""(?<=\w+-\d+\s+)[\s\S]*""".r("cuid", "content").findFirstIn(hlresult)

val replacer = new ReplaceAllStartTag {
  override val origin = "欸 <c>您好</c>"
  override val newTag: Tag = "<c.customer0>"
}

val cueidx = "customer0-9400"

val cueP = new Regex(s"(?<=${cueidx}\\n)[\\s\\S]*?\\n{2,}")

var cc=0
cueP.replaceAllIn(vtt, (m: Regex.Match) => { cc+=1;  println(s"------${m.matched}-------"); "xx" })
println(s"cc: $cc")


val mapper2: (Match) => Option[String] = (m: Match) => {
  val c =
  """[^<>]*?(?=<\/.*>)""".r replaceSomeIn (m.group(0), ma => {

    Option(ma.matched).filterNot(_.trim.isEmpty).map { _ => replacer.result }
  })
  Some(s"${c}")
}
println(s"${cueP replaceSomeIn (vtt, mapper2)}")


"""[^<>]*?(?=<\/+\w*>)""".r.
  findAllMatchIn("customer0-1988 00:00:01.988 --> 00:00:02.468\n<v R0>對</v>\n")
  .foreach { m =>
    println(s"${m.matched}")
  }

