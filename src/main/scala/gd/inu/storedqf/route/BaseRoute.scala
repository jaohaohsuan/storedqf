package gd.inu.storedqf.route

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer

/**
  * Created by henry on 4/20/17.
  */
trait BaseRoute {

    implicit val serialization = org.json4s.jackson.Serialization // or native.Serialization
    implicit val formats = org.json4s.DefaultFormats

    protected def doRoute(implicit mat: Materializer): Route
    protected def prefix = Slash ~ "api" / "v1"

    def route: Route = encodeResponse {
      extractMaterializer { implicit  mat =>
        rawPathPrefix(prefix) {
          doRoute(mat)
        }
      }
    }
}
