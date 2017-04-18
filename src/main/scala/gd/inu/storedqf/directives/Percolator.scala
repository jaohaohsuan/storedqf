package gd.inu.storedqf.directives

import akka.NotUsed
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ClosedShape, SourceShape}
import akka.stream.scaladsl._
import gd.inu.storedqf.format.WebVtt
import org.json4s

import scala.concurrent.Future

/**
  * Created by henry on 4/17/17.
  */
trait Percolator {

//  val percolateQuery: Flow[HttpResponse, json4s.JValue, NotUsed] = Flow[HttpResponse].mapAsync(1) {
//    case HttpResponse(StatusCodes.OK, _, entity, _) =>
//      import org.json4s._
//      import org.json4s.jackson.JsonMethods._
//      Future[json4s.JValue] { parse(""" { "numbers" : [1, 2, 3, 4] } """) }
//
//    case res: HttpResponse =>
//      Future.failed(new Exception(s"${res.entity.toString}"))
//
//    case _ => Future.failed(new Exception("unexpected error"))
//  }
//
//  val highlightWebVttFlow: Flow[json4s.JValue, HttpRequest, NotUsed] = Flow[json4s.JValue].mapAsync(1) {
//    case json =>
//      Future { RequestBuilding.Get("/percolate") }
//    case _ => Future.failed(new Exception("unexpected error"))
  //}

}


