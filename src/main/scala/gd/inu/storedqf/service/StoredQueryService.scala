package gd.inu.storedqf.service

import akka.NotUsed
import akka.actor.{Actor, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethod, HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Source, ZipWith2}
import gd.inu.storedqf.flow.WebVttHighlightFlow
import gd.inu.storedqf.format.WebVtt
import gd.inu.storedqf.service.StoredQueryService.HighlightLog
import gd.inu.storedqf.utils.http.{HttpRequestFlow, ImperativeRequestContext}
import org.json4s.JsonAST
import org.json4s.JsonAST.{JNothing, JValue}

import scala.concurrent.Future

//WebVttHighlightFlow[JValue, JValue, JValue, WebVtt]

object StoredQueryService {

  def props(sid: String)(implicit mat: Materializer) = Props(new StoredQueryService())
  case class HighlightLog(id: String, ctx: ImperativeRequestContext)
}

class StoredQueryService extends Actor with Directives {

  def receive: Receive = {
    case HighlightLog(id, ctx) =>
      ctx.response {
        complete("webvtt")
      }
      context.stop(self)
  }

  import akka.http.scaladsl.model.HttpMethods._
  import gd.inu.storedqf.utils.http.HttpRequestBodyEncoder._
  import org.json4s.JsonDSL._

  //val storedQueryId: String
//  def `stored-query/{id}` =
//    GET./("stored-query/_search",
//      ("query" ->
//        ("ids" ->
//          ("type"   -> "queries") ~
//          ("values" -> List("1234"))
//        )
//      ): JValue
//    )
//  val percolate: ZipWith2[JsonAST.JValue, JsonAST.JValue, JsonAST.JValue] = ???
//
//  val assemble: ZipWith2[JsonAST.JValue, JsonAST.JValue, WebVtt] = ???
//
//  val doc: Source[JsonAST.JValue, NotUsed] = ???
//
//  lazy val query: Source[JValue, NotUsed] = {
//    val search = GET./("stored-query/_search",
//      s"""{
//           "query": {
//               "ids" : {
//                   "type" : "queries",
//                   "values" : [ "1" ]
//               }
//           }
//         }""".stripMargin
//    )
//
//    asSource(search)(_ \\ "query")
//  }
}
