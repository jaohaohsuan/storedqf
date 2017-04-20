package gd.inu.storedqf.utils.http

import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.{HttpEntity, HttpMethod, HttpRequest}
import org.json4s.JsonAST.{JNothing, JValue}
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

  implicit class httpMethodIfx[A](val verb: HttpMethod) {

    def /(path: String,body: => A)(implicit enc: HttpRequestBodyEncoder[A]): HttpRequest =
      HttpRequest(method = verb, uri = path, entity = HttpEntity(defaultMedia, enc.encode(body)))

//    def /[A](path: String): HttpRequest =
//      HttpRequest(method = verb, uri = path)
  }

}

trait HttpRequestBodyEncoder[-A] {
  def encode(value: A): String
}
