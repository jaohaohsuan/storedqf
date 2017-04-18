package gd.inu.storedqf.flow

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.testkit.TestKit
import cucumber.api.scala.{EN, ScalaDsl}
import gd.inu.storedqf.StopSystemAfterAll
import org.scalatest.{MustMatchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by henry on 4/17/17.
  */
class HighlightTest extends TestKit(ActorSystem("testsystem"))
  with ScalaDsl
  with EN
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

  val elasticsearch =  Http().outgoingConnection("localhost", 9200)

  implicit val mat =  ActorMaterializer()

  "query elasticsearch" must {
    "stored-query must have field query" in {
      val response = Await.result(Source.single(RequestBuilding.Get("/_cat/health")).via(elasticsearch).runWith(Sink.head), Duration.Inf)
      info(s"$response")
    }
  }

}
