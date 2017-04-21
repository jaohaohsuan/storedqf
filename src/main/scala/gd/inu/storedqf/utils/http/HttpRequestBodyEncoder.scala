package gd.inu.storedqf.utils.http

import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.{HttpEntity, HttpMethod, HttpRequest, MediaType}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Uri
import org.json4s.JsonAST.{JNothing, JValue}
import org.json4s.jackson.JsonMethods.compact
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
/**
  * Created by henry on 4/17/17.
  */


object HttpRequestBodyEncoder {

  implicit val defaultMedia: MediaType.WithFixedCharset = `application/json`

  implicit class httpMethodIfx(val verb: HttpMethod) {

    def /(path: String, body: String = "")(implicit mime:MediaType.WithFixedCharset) : HttpRequest = {
      //val uri: String Refined Uri = Refined.unsafeApply(s"/$path")
      HttpRequest(method = verb, uri = s"/$path", entity = HttpEntity(mime, body))
    }
  }
}
