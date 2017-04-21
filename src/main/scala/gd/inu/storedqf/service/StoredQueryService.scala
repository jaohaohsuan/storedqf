package gd.inu.storedqf.service

import akka.actor.{Actor, Props}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives
import akka.stream.scaladsl.Sink
import gd.inu.storedqf.service.ElasticsearchClientService.ElasticsearchClientFactory
import gd.inu.storedqf.service.StoredQueryService.HighlightLog
import gd.inu.storedqf.utils.http.ImperativeRequestContext
import org.json4s

import scala.concurrent.Future

object StoredQueryService {

  def props(sid: String)(implicit esClientFactory: ElasticsearchClientFactory) = Props(new StoredQueryService())
  case class HighlightLog(storedqId: String,logPath: String, ctx: ImperativeRequestContext)
}

class StoredQueryService()(implicit val esClientFactory: ElasticsearchClientFactory) extends Actor with Directives {

  import ElasticsearchClient._
  import akka.http.scaladsl.model.HttpMethods._
  import gd.inu.storedqf.utils.http.HttpRequestBodyEncoder._

  private implicit val esClient = esClientFactory()

  def receive: Receive = {
    case HighlightLog(storedqId, logPath, ctx) =>

      ctx.response {
        extractMaterializer { implicit mat =>
          val x: Future[json4s.JValue] = (GET / s"stored-query/amiast/$storedqId").send().via(OK.flow(_ \\ "query")).runWith(Sink.head)

          complete("webvtt")
        }

      }
      //context.stop(self)
  }

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
}
