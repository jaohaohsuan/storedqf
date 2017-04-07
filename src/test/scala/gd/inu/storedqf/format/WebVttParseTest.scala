package gd.inu.storedqf.format

import org.scalatest.{FeatureSpec, FlatSpec, GivenWhenThen, Matchers}


class WebVttParseTest extends FeatureSpec with Matchers with GivenWhenThen {

   feature("Export WebVtt format") {

     scenario("extract cue properties from raw string") {
       Given("a sample \"customer0-1988 00:00:01.988 --> 00:00:02.468\\n<v R0>對\\n</v>\"")
       val cue = "customer0-1988 00:00:01.988 --> 00:00:02.468\n<v R0>對\n</v>"
       When("parsing")

       Then("the cueid should be 'customer0-1988'")

       And("the times must be written in hh:mm:ss.mmm format")

       And("the text should keep any whitespaces and newlines")
     }

  }

}
