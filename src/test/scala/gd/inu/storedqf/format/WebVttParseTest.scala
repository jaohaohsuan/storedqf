package gd.inu.storedqf.format

import gd.inu.storedqf.UnitSpec
import gd.inu.storedqf.format.WebVtt.CueProperties


class WebVttParseTest extends UnitSpec with CueProperties {

   feature("Export WebVtt format") {

     scenario("extract cue properties from raw string") {
       Given("a raw string sample \"customer0-1988 00:00:01.988 --> 00:00:02.468\\n<v R0>對</v>\\n\"")
       val raw = "customer0-1988 00:00:01.988 --> 00:00:02.468\n<v R0>對</v>\n"

       When("use CueProperties trait to get properties")
       val cue = new CueString(raw)

       Then("property 'cueid' should be 'customer0-1988'")
       val cueidValue = cueid(raw).value
       cueidValue shouldBe "customer0-1988"
       cue.lines(0) should equal (cueidValue)

       And(s"property 'times' must be written in hh:mm:ss.mmm format")
       val times = cue.time
       info(times)
       times shouldBe "00:00:01.988 --> 00:00:02.468"

       And(s"property 'text'")
       val text = cue.text
       info(text)
       text shouldBe "<v R0>對</v>"

       And("split into multiline")

     }

     scenario("Match WebVtt file format") {
       Given("a raw string sample \"customer0-1988 00:00:01.988 --> 00:00:02.468\\n<v R0>對</v>\\n\"")
       val cue = new CueString("customer0-1988 00:00:01.988 --> 00:00:02.468\n<v R0>對</v>\n")

       When("split with newline")
       cue.lines should have size 3

       Then("should divide into multilines likes below\n")
       cue.lines.foreach { l => info(l) }
     }

  }

}
