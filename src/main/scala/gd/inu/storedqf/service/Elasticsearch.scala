package gd.inu.storedqf.service

import akka.NotUsed
import akka.actor.Actor
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.{Flow, Source}
import com.typesafe.config.ConfigFactory
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import gd.inu.storedqf.flow.HighlightFlow
import gd.inu.storedqf.utils.http.HttpRequestBodyEncoder
import org.json4s.JsonAST.{JObject, JValue}
import org.json4s.jackson.JsonMethods.{compact, parse}

import scala.concurrent.{ExecutionContext, Future}
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.http.scaladsl.marshalling.Marshaller
import org.json4s


trait HttpRequestSupport {

//  implicit val serialization = org.json4s.jackson.Serialization // or native.Serialization
//  implicit val formats = org.json4s.DefaultFormats
//  implicit val writePretty = Json4sSupport.ShouldWritePretty.True
  implicit val endpoint: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]]
  implicit val ec: ExecutionContext
  implicit val mat: Materializer

  implicit val um: Unmarshaller[ResponseEntity, json4s.JValue] =
    Unmarshaller
      .stringUnmarshaller
      .forContentTypes(akka.http.scaladsl.model.MediaTypes.`application/json`)
      .map { s => parse(s) }

  def unhandledHttpResponse: PartialFunction[HttpResponse, Future[JValue]]= { case res: HttpResponse => Future.failed(new Exception(s"${res.entity.toString}")) }

  def okHttpResponse(f: JValue => JValue): PartialFunction[HttpResponse, Future[JValue]] = {
    case HttpResponse(StatusCodes.OK, _, entity, _) => Unmarshal(entity).to[JValue].map(f)
  }

  def asSource(request: HttpRequest)(f: JValue => JValue = identity ): Source[JValue, NotUsed] = {
      Source.single(request).via(endpoint).via(Flow[HttpResponse].mapAsync(1)(okHttpResponse(f) orElse unhandledHttpResponse))
  }

}


trait WebVttService extends HttpRequestSupport {

  import akka.http.scaladsl.model.HttpMethods.GET
  import gd.inu.storedqf.utils.http.HttpRequestBodyEncoder._
  import org.json4s.JsonDSL._

//  val searchQuery =
//    GET./("stored-query/_search") {
//      ("query" ->
//        ("ids" ->
//          ("type"   -> "queries") ~
//          ("values" -> List("1234"))
//        )
//      ): JObject
//    }


    val searchQuery =
    GET./("stored-query/_search") {
      """{
           "query": {
               "ids" : {
                   "type" : "queries",
                   "values" : ["1"]
               }
           }
         }""".stripMargin
    }

    asSource(searchQuery)(_ \\ "query")

}


class Elasticsearch extends Actor {

  val config = ConfigFactory.load().getConfig("service.elasticsearch")

  implicit val system = context.system
  implicit val materializer = ActorMaterializer()
  //implicit val ec: ExecutionContext = context.system.dispatcher

  val connectionFlow = Http().outgoingConnection(config.getString("address"), port = config.getInt("port"))

  def receive = {
    case _ =>

  }
}
