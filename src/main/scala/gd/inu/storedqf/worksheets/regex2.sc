import scala.util.matching.Regex

val vtt = """WEBVTT
            |
            |agent0-5188
            |00:00:05.188 --> 00:00:08.532
            |<v R1>很 高興 為您 服務 您好  </v>
            |
            |customer0-9400
            |00:00:09.400 --> 00:00:10.008
            |<v R0>欸 您好  </v>""".stripMargin

val cueid = "customer0-9400"

val cueP = new Regex(s"(?<=${cueid}\\n\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\s-->\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\n)(<\\w*\\s\\w*.)([\\s\\S]*?)(<\\/\\w*.)", "<", "txt", ">")


var cc=0

val mm: Regex.Match = cueP.findFirstMatchIn(vtt).get

//group 0 is safe

val f: Regex.Match => Option[String] = (m) => {
  val y = s"${m.group("<")}xx${m.group(">")}"
  Some(y)
}


cueP.replaceSomeIn(vtt, f)

println(s"cc: $cc")