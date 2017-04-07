package gd.inu.storedqf.text

/**
  * Created by henry on 4/7/17.
  */
import org.scalatest.{FunSpec, GivenWhenThen, Matchers}

class WebVttHighlightingTest extends FunSpec with GivenWhenThen with Matchers {

  describe("add css class to cue") {

    Given("a cue of WebVtt highlighting result from elasitcsearch like <c>hello</c>")

    When("replace the tag with css style 'agent0'")

    Then("should like this <c.agent0>hello</c>")

  }
}
