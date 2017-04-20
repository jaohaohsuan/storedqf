package gd.inu.storedqf.utils.http

import akka.NotUsed
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, ResponseEntity, StatusCodes}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Source}
import org.json4s
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods.parse

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henry on 4/20/17.
  */


trait HttpRequestFlow {

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

  def asSource(request: HttpRequest)(f: JValue => JValue = identity): Source[JValue, NotUsed] = {
      Source.single(request).via(endpoint).via(Flow[HttpResponse].mapAsync(1)(okHttpResponse(f) orElse unhandledHttpResponse))
  }

}
