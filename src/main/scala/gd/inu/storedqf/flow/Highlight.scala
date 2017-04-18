package gd.inu.storedqf.flow

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, ClosedShape, Materializer}
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



trait HttpRequestFlow extends Json4sSupport {

  implicit val serialization = org.json4s.jackson.Serialization // or native.Serialization
  implicit val formats = org.json4s.DefaultFormats
  implicit val writePretty = Json4sSupport.ShouldWritePretty.True
  implicit val es: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]]
  implicit val ec: ExecutionContext
  implicit val mat: Materializer

  def flow[A](request: HttpRequest, extract: JValue => A)(): Source[A, NotUsed] = {

    val extractFlow = Flow[HttpResponse].mapAsync(1) {

      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Unmarshal(entity).to[JValue].map(extract)

      case res: HttpResponse =>
        Future.failed(new Exception(s"${res.entity.toString}"))
    }

    Source.single(request).via(es).via(extractFlow)
  }

}


trait Highlight extends HttpRequestFlow {

  //  def elasticsearchFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]]

  val query:          Source[JValue, NotUsed] = flow(Get("/stored-query/1"), _ \\ "query")
  val doc:            Source[JValue, NotUsed] = flow(Get("/logs-2017-01-01/amiast"), _ \\ "")
  val percolate:      ZipWith2[JValue, JValue, HttpResponse]
  val highlightFlow:  ZipWith2[HttpResponse, JValue, WebVtt]

  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    query ~> percolate.in0
    doc   ~> percolate.in1

    percolate.out ~> highlightFlow.in0
    doc           ~> highlightFlow.in1

    highlightFlow.out ~> Sink.head[WebVtt]

    ClosedShape
  })

}
