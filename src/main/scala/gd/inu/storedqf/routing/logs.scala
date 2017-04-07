package gd.inu.storedqf.routing

import akka.http.scaladsl.model.{HttpEntity, HttpRequest}
import akka.http.scaladsl.model.MediaTypes.`application/json`
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatterBuilder

import scala.concurrent.Future
import scala.util.matching.Regex
import org.json4s._

/**
  * Created by henry on 3/27/17.
  */

object Index {

  def createIndex(date: DateTime = DateTime.now): Option[Index] = {

    val regex = """^logs-\d{4}\.\d{2}\.\d{2}$""".r
    val fmt = new DateTimeFormatterBuilder()
      .appendLiteral("logs-")
      .appendYear(4, 4)
      .appendLiteral('.')
      .appendMonthOfYear(2)
      .appendLiteral('.')
      .appendDayOfMonth(2)
      .toFormatter

    val data = date.toString(fmt)
    if(regex.pattern.matcher(data).matches)
      Some(Index(regex, data))
    else
      None
  }

}

case class Log(index: String, typ: String, id: String)

case class Index private (regex: Regex, data: String)

trait logs {

  import akka.http.scaladsl.server.Directives._

  lazy val `logs-yyyy.MM.dd` = Index.createIndex().get.regex

  def getVtt: Future[JObject] = ???

  val log = path(`logs-yyyy.MM.dd` / Segment / Segment ).tmap { case (index, typ, id) =>
    provide(Log(index, typ, id))

      //s"/$index/$typ/$docId"
  }


  val `logs-yyyy.MM.dd/{type}/{docId}` =
    log { doc =>

      complete("OK")
    }

}
