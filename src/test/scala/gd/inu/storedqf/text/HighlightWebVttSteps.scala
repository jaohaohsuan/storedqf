package gd.inu.storedqf.text

import cucumber.api.PendingException
import cucumber.api.scala.{EN, ScalaDsl}
import gd.inu.storedqf.format.{CueString, WebVtt}
import org.json4s.JsonAST.JString
import org.scalatest.Matchers
import org.json4s._
import org.json4s.jackson.JsonMethods._

class HighlightWebVttSteps extends ScalaDsl with EN with Matchers {

  private var highlightResults: () => Iterable[HighlightFragment] = () => List.empty
  private var vtt: String = ""


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

}
