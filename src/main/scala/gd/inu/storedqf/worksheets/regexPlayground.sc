
import gd.inu.storedqf.format.WebVtt.CueVal
import gd.inu.storedqf.text.ReplaceAllStartTag

val p1 = """(.+-\d+)\s+(\S+\s-->\s\S+)\s+([\s\S]*$)""".r
val t1 = "customer0-28476 00:00:28.476 --> 00:00:32.148 \n<v R0>那個 您送 撥您 的電話 三十 五 % 是 一樣 </v>\n"
t1 match {
  case p1(cudeid, time, text) =>
    println(cudeid)
    println(time)
    print(text)
    print("OK")
}



val xx = """(.+-\d+)""".r.findAllMatchIn("agent0-1224")
println(xx)


"""\w+(?=-\d*?[\s\S]*)""".r.findAllMatchIn("agent0-21233 kokdos dkos <>dko?").foldLeft(""){_ + _}

val raw = """customer0-28476 那個 您送 撥您 的電話 三十 五 % 是 一樣"""
val p = """(\w+-\d+[ ])([\s\S]*)""".r("id","body")
raw match {
  case p(cuid, body) => s"${cuid.trim}\n$body"
  case _ => raw
}

val c1 = new CueVal("customer0-28476 那個 您送 撥您 的電話 三十 五 % 是 一樣 ")
c1.id
//info(s"\n\n${c1}")

//new CueVal("customer0-28476 那個 您送 撥您 的電話 三十 五 % 是 一樣 ") with ReplaceAllStartTag {
//  override val origin = raw
//  override val newTag: Tag = s"c${id}"
//}

//    val xxx = new CueVal("customer0-28476 那個 您送 撥您 的電話 三十 五 % 是 一樣 ") with ReplaceAllStartTag {
//      override val origin = raw
//      override val newTag: Tag = s"c${id}"
//    }
//
//    info(xxx.result)







