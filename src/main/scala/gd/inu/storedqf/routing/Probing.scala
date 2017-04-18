package gd.inu.storedqf.routing

import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives.{complete, onSuccess, path, pathEndOrSingleSlash}
import akka.http.scaladsl.server.Route

import scala.concurrent.Future

/**
  * Created by henry on 4/17/17.
  */
trait Probing {

  val `/`: Route = pathEndOrSingleSlash {
    complete("Up and running.")
  }

  val shutdown: Route = path("shutdown") {
    onSuccess(graceShutdown) {
      complete(OK)
    }
  }

  def graceShutdown: Future[Unit]
}
