package gd.inu.storedqf.text

import cucumber.api.PendingException
import cucumber.api.scala.{EN, ScalaDsl}
import gd.inu.storedqf.format.{CueProperties, CueString, WebVtt}
import org.json4s.JsonAST.JString
import org.scalatest.Matchers
import org.json4s._
import org.json4s.jackson.JsonMethods._

class HighlightWebVttSteps extends ScalaDsl with EN with Matchers {

  private var highlightResults: () => Iterable[HighlightFragment] = () => List.empty
  private var vtt: String = ""
  private var highlightFragment: HighlightFragment = null


  Given("""^a raw of webvtt like fragment:$"""){ (arg0:String) =>
    vtt = (parse(arg0) \\ classOf[JString]).map(new CueString(_)).foldLeft(""){_ ++ _.toString}
  }
  When("""^a query highlight result:$"""){ (arg0:String) =>
    highlightResults = () => (parse(arg0) \\ classOf[JString]).map(new HighlightFragment(_))
  }
  Then("""^webvtt output must append a css class on cue text "([^"]*)"$"""){ (arg0:String) =>
    import Highlighter._

    val highlightedVtt = highlightResults().foldLeft(vtt){ (ac, fragment) =>
      new WebVtt(vtt).highlight(fragment)
    }
    highlightedVtt should include (arg0)
  }

  Given("""^convert a small piece of highlight result "([^"]*)" to HighlightFragment$"""){ (arg0:String) =>
    highlightFragment = new HighlightFragment(arg0)
  }
  Given("""^we can use CueProperties trait to get cueid "([^"]*)" which will help use to locate cue position in webvtt$"""){ (arg0:String) =>
    (highlightFragment: CueProperties).cueid(highlightFragment.fragment).value should be (arg0)
  }
  Given("""^css class id "([^"]*)" come from the prefix of cueid without dash & numbers$"""){ (arg0:String) =>
    (highlightFragment: CueProperties).role(highlightFragment.fragment).value should be (arg0)
  }
  Given("""^to support css class level styling like this "([^"]*)" we call replace method to get result "([^"]*)"$"""){ (arg0:String, arg1:String) =>
    highlightFragment.newTag.value should be (arg0)
    val result = highlightFragment.replace
    result should be (arg1)
  }


}
