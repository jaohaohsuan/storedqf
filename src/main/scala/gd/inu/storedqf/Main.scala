package gd.inu.storedqf

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives.{pathEndOrSingleSlash, _}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.model.StatusCodes._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Created by henry on 3/27/17.
  */
object Main extends App with routing.Probing {

    val config = ConfigFactory.load()
    val appName = "storedqf"

    implicit val system = ActorSystem(appName)
    implicit val materializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    lazy val root: Route = `/` ~ shutdown

    val httpHandler =  Http().bindAndHandle(root, "0.0.0.0", config.getInt("http.port"))

    import utils.net.Connectivity._

    val graceShutdown: Future[Unit] =
      for {
        (ip, port) <- httpHandler.flatMap(_.unbinding())
        _ <- system.terminate()
        _ <- system.whenTerminated
      } yield println(s"unbinding $ip port $port")


    httpHandler.onSuccess { case binding =>
      println(s"${binding.ip} listen on port ${binding.port}")
    }

    sys.addShutdownHook {
      println("shutdown hook handled")
      Await.result(graceShutdown,Duration.Inf)
      println(s"$appName has been shutdown gracefully.")
    }
}
