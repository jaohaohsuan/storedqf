package gd.inu.storedqf.format

import org.scalatest.{FunSpec, GivenWhenThen, Matchers}


class WebVttParseTest extends FunSpec with Matchers with GivenWhenThen {
  describe("A cue") {

    it("contains 3 parts") {
      Given("customer0-1988 00:00:01.988 --> 00:00:02.468\\n<v R0>對\\n</v>")
      val cue = "customer0-1988 00:00:01.988 --> 00:00:02.468\n<v R0>對\n</v>"
      When("create a Cue")

      Then("the cueid should like 'customer0-1988'")

      And("the times must be written in hh:mm:ss.mmm format")

      And("the text should keep any whitespaces and newlines")

    }
  }

}
