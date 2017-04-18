package gd.inu.storedqf.service

import akka.actor.Actor
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

/**
  * Created by henry on 4/17/17.
  */
class Elasticsearch extends Actor {

  val config = ConfigFactory.load().getConfig("service.elasticsearch")

  implicit val system = context.system
  implicit val materializer = ActorMaterializer()
  //implicit val ec: ExecutionContext = context.system.dispatcher

  val connectionFlow = Http().outgoingConnection(config.getString("address"), port = config.getInt("port"))

  def receive = {
    case _ =>

  }
}
