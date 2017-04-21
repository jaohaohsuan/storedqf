package gd.inu.storedqf.route

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import gd.inu.storedqf.service.StoredQueryService
import gd.inu.storedqf.service.StoredQueryService.HighlightLog
/**
  * Created by henry on 3/27/17.
  */

//object Index {
//
//  def createIndex(date: DateTime = DateTime.now): Option[Index] = {
//
//    val regex = """^logs-\d{4}\.\d{2}\.\d{2}$""".r
//    val fmt = new DateTimeFormatterBuilder()
//      .appendLiteral("logs-")
//      .appendYear(4, 4)
//      .appendLiteral('.')
//      .appendMonthOfYear(2)
//      .appendLiteral('.')
//      .appendDayOfMonth(2)
//      .toFormatter
//
//    val data = date.toString(fmt)
//    if(regex.pattern.matcher(data).matches)
//      Some(Index(regex, data))
//    else
//      None
//  }
//
//}
//
//case class Log(index: String, typ: String, id: String)
//
//case class Index private (regex: Regex, data: String)

class LogsRoute(storedqService: ActorRef)(implicit system: ActorSystem) extends BaseRoute {

  import gd.inu.storedqf.directives.ActorPerRequestLikeSpray._

  def doRoute(implicit mat: Materializer): Route = {
    path("""^logs-\d{4}\.\d{2}\.\d{2}$""".r / Segment / Segment) { (index, typ, id) =>
      get {
        parameters('id.? ) { storedqId =>
          imperativelyComplete { ctx =>
            storedqService ! HighlightLog(storedqId.get, s"$index/$typ/$id", typ, ctx)
          }
        }
      }
    }
  }
}
