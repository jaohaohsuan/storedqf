package gd.inu.storedqf.routing

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatterBuilder

import scala.util.matching.Regex

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

case class Index private (regex: Regex, data: String)

trait logs {

  import akka.http.scaladsl.server.Directives._

  private lazy val `logs-yyyy.MM.dd` = Index.createIndex().get.regex

  val `logs-yyyy.MM.dd/{type}/{docId}` =
    path(`logs-yyyy.MM.dd` / Segment / Segment ) { (index, typ, docId) =>

      complete("OK")
    }

}
