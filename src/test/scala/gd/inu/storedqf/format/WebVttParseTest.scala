package gd.inu.storedqf.format

import gd.inu.storedqf.UnitSpec
import gd.inu.storedqf.format.WebVtt.CueProperties


class WebVttParseTest extends UnitSpec with WebVtt with CueProperties {

   feature("Export WebVtt format") {

     scenario("extract cue properties from raw string") {
       Given("a raw string sample \"customer0-1988 00:00:01.988 --> 00:00:02.468\\n<v R0>對</v>\\n\"")
       val raw = "customer0-1988 00:00:01.988 --> 00:00:02.468\n<v R0>對</v>\n"

       When("use CueProperties trait to get properties")
       val cue = new WebVtt.CueVal(raw)

       Then("property 'cueid' should be 'customer0-1988'")
       val cueidValue = cueid(raw).value
       cueidValue shouldBe "customer0-1988"
       cue.lines(0) should equal (cueidValue)

       And(s"property 'times' must be written in hh:mm:ss.mmm format")
       cue.lines(1) shouldBe "00:00:01.988 --> 00:00:02.468"

       And(s"property 'text'")
       cue.lines.toSeq.drop(2).foldLeft("")(_ + _) shouldBe "<v R0>對</v>"

       And("split into multiline")

     }

     scenario("Match WebVtt file format") {
       Given("a raw string sample \"customer0-1988 00:00:01.988 --> 00:00:02.468\\n<v R0>對</v>\\n\"")
       val cue = new WebVtt.CueVal("customer0-1988 00:00:01.988 --> 00:00:02.468\n<v R0>對</v>\n")

       When("split with newline")

       Then("should divide into multilines likes below\n")

       info("customer0-1988")
       info("00:00:01.988 --> 00:00:02.468")
       info("<v R0>對</v>\n")
       cue.lines should have size 3
     }

  }

}
