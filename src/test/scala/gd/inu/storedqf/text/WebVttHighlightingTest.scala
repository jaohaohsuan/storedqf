package gd.inu.storedqf.text
import eu.timepit.refined.auto._
import gd.inu.storedqf.UnitSpec
import gd.inu.storedqf.format.CueString
import gd.inu.storedqf.format.WebVtt.CueVoiceMissing
import gd.inu.storedqf.text.ReplaceAllStartTag._

class WebVttHighlightingTest extends UnitSpec {

  scenario("add css class to cue") {

    Given("a cue of WebVtt highlighting result from elasitcsearch like <c>hello</c>")
    val act = new ReplaceAllStartTag {
      val newTag: Tag = "<c.agent0>"
      val origin = "<c>hello</c><c>world</c>"
    }

    When("add css style 'agent0' to <c>")
    act.matches should have size 2

    Then("tag should be <c.agent0>hello</c>")
    act.result should be ("<c.agent0>hello</c><c.agent0>world</c>")

  }

  scenario("apply highlight result to cue") {
    Given("a highlight result: \"customer0-9400 欸 <c>您好</c> \"")
    val marker = new CueMarker("customer0-9400 欸 <c>您好</c> ")
    marker.newTag.value should be ("<c.customer0>")
    marker.content should not equal("")
    info(s"'${marker.cueid(marker.raw)}'")

    And("cue fragment")
    val cue = new CueString("""customer0-9400
                |00:00:09.400 --> 00:00:10.008
                |<v R0>欸 您好  </v>""".stripMargin)

    When("marker replace cue")
    val result = marker.replace(cue)

    Then("looks like")
    result.lines should have size 3
    result.lines.foreach { l => info(l) }


  }

  scenario("apply highlight result to cue without voice") {
    Given("a highlight result: \"customer0-9400 欸 <c>您好</c> \"")
    val marker = new CueMarker("customer0-9400 欸 <c>您好</c> ")
    marker.newTag.value should be ("<c.customer0>")
    marker.content should not equal("")

    And("cue fragment")
    val cue = new CueString("""customer0-9400
                              |00:00:09.400 --> 00:00:10.008
                              |欸 您好  """.stripMargin)

    When("marker replace cue should throw CueVoiceMissing")
    intercept[CueVoiceMissing] {
      marker.replace(cue)
    }
  }

}
