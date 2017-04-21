package gd.inu.storedqf.utils.http

import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.{HttpEntity, HttpMethod, HttpRequest, MediaType}
import org.json4s.JsonAST.{JNothing, JValue}
import org.json4s.jackson.JsonMethods.compact

/**
  * Created by henry on 4/17/17.
  */


object HttpRequestBodyEncoder {

  implicit val defaultMedia: MediaType.WithFixedCharset = `application/json`

  implicit class httpMethodIfx(val verb: HttpMethod) {

    def /(path: String, body: String = "")(implicit mime:MediaType.WithFixedCharset) : HttpRequest =
      HttpRequest(method = verb, uri = path, entity = HttpEntity(mime, body))
  }
}
