package gd.inu.storedqf.text
import eu.timepit.refined.auto._
import gd.inu.storedqf.UnitSpec
import gd.inu.storedqf.format.WebVtt.CueVal
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

}
