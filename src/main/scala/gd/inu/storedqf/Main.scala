package gd.inu.storedqf

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.pathEndOrSingleSlash
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Created by henry on 3/27/17.
  */
object Main {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val appName = "storedqf"

    implicit val system = ActorSystem(appName)
    implicit val materializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    val root = pathEndOrSingleSlash {
      complete("Up and running")
    }

    val httpHandler = Http().bindAndHandle(root,"localhost", config.getInt("http.port"))

    sys.addShutdownHook {
      Await.result(httpHandler.flatMap(_.unbind()), Duration.Inf)
      println(s"Unbinding port ${config.getInt("http.port")}")
      system.terminate()
      Await.result(system.whenTerminated,Duration.Inf)
      println(s"$appName has been shutdown gracefully.")
    }
  }
}
