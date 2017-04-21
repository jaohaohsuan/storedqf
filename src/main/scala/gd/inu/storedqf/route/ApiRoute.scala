package gd.inu.storedqf.route

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Route
import scala.concurrent.ExecutionContext

/**
  * Created by henry on 4/20/17.
  */
trait ApiRoute {

  implicit val actorSystem: ActorSystem
  implicit val ec: ExecutionContext = actorSystem.dispatcher

   val storedqService: ActorRef
  val logsRoute = new LogsRoute(storedqService)

  def route: Route = logsRoute.route
}

class ApiRouteService(val storedqService: ActorRef)(implicit val actorSystem: ActorSystem) extends ApiRoute
