package gd.inu.storedqf.format

import org.scalatest.{FeatureSpec, FlatSpec, GivenWhenThen, Matchers}


class WebVttParseTest extends FeatureSpec with Matchers with GivenWhenThen with WebVtt {

   feature("Export WebVtt format") {

     scenario("extract cue properties from raw string") {
       Given("a sample \"customer0-1988 00:00:01.988 --> 00:00:02.468\\n<v R0>對</v>\\n\"")
       val cue = "customer0-1988 00:00:01.988 --> 00:00:02.468\n<v R0>對</v>\n"

       When("parsing")
       val result = vttConversation(cue).get

       Then("the cueid should be 'customer0-1988'")
       result.cueid shouldBe "customer0-1988"

       And("the times must be written in hh:mm:ss.mmm format")
       result.time shouldBe "00:00:01.988 --> 00:00:02.468"

       And("the text should keep any whitespaces and newlines")
       result.text shouldBe "<v R0>對</v>\n"
     }

  }

}
