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
      new WebVtt(vtt).substitute(fragment)
    }
    highlightedVtt should include (arg0)
  }

  Given("""^一个未标记的hightlight数据 "([^"]*)"$"""){ (arg0:String) =>
    highlightFragment = new HighlightFragment(arg0)
  }
  When("""^cuid "([^"]*)" 的前缀 "([^"]*)" 作为css样式的class时$"""){ (arg0:String, arg1:String) =>
    (highlightFragment: CueProperties).cueid(highlightFragment.fragment).value should be (arg0)
    (highlightFragment: CueProperties).role(highlightFragment.fragment).value should be (arg1)
  }

  Then("""^highlight结果的内容必须添加css样式, 内容 "([^"]*)" 必须改为 "([^"]*)" 的格式$"""){ (arg0:String, arg1:String) =>
    highlightFragment.origin should be (arg0)
    val result = highlightFragment.replace
    result should be (arg1)
  }


}
