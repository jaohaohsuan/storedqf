package gd.inu.storedqf

import gd.inu.storedqf.routing._
import org.scalatest.{FlatSpec, Matchers}
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  * Created by henry on 3/30/17.
  */
class VttPhoneCallDocTest extends FlatSpec with Matchers {

  behavior of "vtt doc format"

  "json" should "content dialogs, agent* and customer* fields" in {

    val raw =
      """
        |{
        |  "customer0": [
        |    "customer0-26882 蛤 好 吧 好 吧 "
        |  ],
        |  "dialogs": [
        |    "customer0-26882 蛤 好 吧 好 吧 "
        |  ]
        |}
      """.stripMargin
    val json: JValue = parse(raw)

    val result = VttPhoneCallDoc.create(json)

    if(result.isFailure){
      info(result.failed.get.getMessage)
    }

    result.isSuccess shouldBe true

  }
}
