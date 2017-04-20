package gd.inu.storedqf.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext

/**
  * Created by henry on 4/20/17.
  */
trait ApiRoute {

  implicit val actorSystem: ActorSystem
  implicit val ec: ExecutionContext = actorSystem.dispatcher

  val logsRoute = new LogsRoute

  def route: Route = logsRoute.route
}

class ApiRouteService()(implicit override val actorSystem: ActorSystem) extends ApiRoute
