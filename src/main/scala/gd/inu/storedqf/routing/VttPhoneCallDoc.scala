package gd.inu.storedqf.routing

import org.json4s._

import scala.util.Try

/**
  * Created by henry on 3/30/17.
  */

object VttPhoneCallDoc {

  implicit val formats = DefaultFormats

  def create(raw: JValue): Try[VttPhoneCallDoc] = {
    
    def extract(field: String): List[String] = raw \ field \\ classOf[JString]

    Try(VttPhoneCallDoc(extract("dialogs"), extract("agent0"), extract("customer0")))
  }

}
case class VttPhoneCallDoc(dialogs: List[String] , agent0: List[String], customer0: List[String]) {
  require((agent0 ++ customer0).size == dialogs.size)
}
