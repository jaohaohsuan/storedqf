package gd.inu.storedqf.flow

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source, ZipWith}
import gd.inu.storedqf.{StopSystemAfterAll, UnitSpec}
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import akka.testkit.TestProbe
import akka.testkit.TestKit
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.{MustMatchers, WordSpecLike}

import scala.concurrent.duration._
/**
  * Created by henry on 4/20/17.
  */
class WebVttHighlightFlowTest extends TestKit(ActorSystem("HighlightFlowSpec"))
  with WebVttHighlightFlow[String, String, String, String]
  with ScalaDsl
  with EN
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {


  import system.dispatcher

  val query = Source.single("query")
  val doc = Source.single("doc")
  val percolate = ZipWith((query: String, doc: String) => s"$query-$doc")
  val assemble = ZipWith((highlightFragment: String, vttFieldOfDoc: String) => s"$highlightFragment+$vttFieldOfDoc")

  implicit val mat =  ActorMaterializer()

  "graph" must {
    "follow the pipe" in {
      val probe = TestProbe()
      webvtt.runWith(Sink.head).pipeTo(probe.ref)
      probe.expectMsg(3.seconds,"query-doc+doc")
    }
  }

}