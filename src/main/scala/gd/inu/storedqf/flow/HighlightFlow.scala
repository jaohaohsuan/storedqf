package gd.inu.storedqf.flow

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, ClosedShape, Materializer, SourceShape}
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source, ZipWith2}
import gd.inu.storedqf.format.WebVtt
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henry on 4/17/17.
  */

trait HighlightFlow[Q, D, H, W] {

  val query:          Source[Q, NotUsed]
  val doc:            Source[D, NotUsed]
  val vtt:            Source[D, NotUsed]
  val percolate:      ZipWith2[Q, D, H]
  val highlightFlow:  ZipWith2[H, D, W]

  def webvtt = Source.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val pz = b.add(percolate)
    val hz = b.add(highlightFlow)

    query ~> pz.in0
    doc   ~> pz.in1

    pz.out ~> hz.in0
    vtt    ~> hz.in1

    SourceShape(hz.out)
  })

}
