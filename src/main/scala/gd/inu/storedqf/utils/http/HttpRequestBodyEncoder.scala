package gd.inu.storedqf.utils.http

import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.{HttpEntity, HttpMethod, HttpRequest}
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods.compact

/**
  * Created by henry on 4/17/17.
  */

class JsonString(val str: String) extends AnyVal

object HttpRequestBodyEncoder {

  implicit val defaultMedia = `application/json`

  implicit val json4sHttpRequestBodyEncoder: HttpRequestBodyEncoder[JValue] =  new HttpRequestBodyEncoder[JValue] {
    def encode(d: JValue): String = compact(d)
  }

  implicit val stringHttpRequestBodyEncoder: HttpRequestBodyEncoder[String] =  new HttpRequestBodyEncoder[String] {
    def encode(d: String): String = d
  }

  implicit class httpMethodIfx(val verb: HttpMethod) {
    def /[A](path: String)(body: => A)(implicit enc: HttpRequestBodyEncoder[A]) =
      HttpRequest(method = verb, uri = path, entity = HttpEntity(defaultMedia, enc.encode(body)))

//    def `//`[A](path: String)(body: => String = { "" }): HttpRequest =
//      HttpRequest(method = verb, uri = path, entity = HttpEntity(`application/json`, body))
  }

}

trait HttpRequestBodyEncoder[-A] {
  def encode(value: A): String
}
