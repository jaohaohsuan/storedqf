package gd.inu.storedqf

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.testkit.ScalatestRouteTest
import gd.inu.storedqf.routing.Index
import org.joda.time.DateTime
import org.scalatest._
import org.scalatest.matchers._
import org.scalatest.prop._
import org.scalacheck._
/**
  * Created by henry on 3/27/17.
  */
class logsRouteTest extends FlatSpec with Matchers with ScalatestRouteTest with routing.logs {

  behavior of "logs api routing"

  "create dateIndex" should "not be None" in {

    val dateIndexOpt = routing.Index.createIndex(DateTime.now)
    assert(dateIndexOpt.isDefined)

    val Some(Index(regex, data)) = dateIndexOpt
    assert(regex.pattern.matcher(data).matches())

  }

  "logs" should "return 200 OK" in {
      Get("/logs-2017.10.10/amiast/12345") ~> `logs-yyyy.MM.dd/{type}/{docId}` ~> check {
      status shouldEqual StatusCodes.OK
    }
  }
}
