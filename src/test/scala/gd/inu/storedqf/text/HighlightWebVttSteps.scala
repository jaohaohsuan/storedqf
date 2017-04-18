package gd.inu.storedqf.text

import cucumber.api._
import cucumber.api.scala.{EN, ScalaDsl}
import gd.inu.storedqf.format.{CueProperties, CueString, WebVtt}
import org.json4s.JsonAST.JString
import org.scalatest.Matchers
import org.json4s._
import org.json4s.jackson.JsonMethods._
import collection.JavaConverters._

class HighlightWebVttSteps extends ScalaDsl with EN with Matchers {

  private var highlightResults = List.empty[HighlightFragment]
  private var vtt: String = ""
  private var highlightFragment: HighlightFragment = null

  Given("""^对话内容$"""){ (arg0:DataTable) =>
    vtt = arg0.asList(classOf[String]).asScala.map(new CueString(_)).foldLeft(""){_ ++ _.toString}
  }

  When("""^用搜寻到的hightlight片段 "([^"]*)" 取代对话内容$"""){ (arg0:String) =>
    highlightResults = new HighlightFragment(arg0) :: highlightResults
  }

  Then("""^输出符合w(\d+)c webvtt格式文件, 并找出关键字 "([^"]*)"$"""){ (arg0: String, arg1:String) =>
    import Highlighter._

    val highlightedVtt = highlightResults.foldLeft(vtt){ (ac, fragment) =>
      new WebVtt(ac).substitute(fragment)
    }
    highlightedVtt should include (arg1)
  }

  Given("""^一个未标记的hightlight片段 "([^"]*)"$"""){ (arg0:String) =>
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
