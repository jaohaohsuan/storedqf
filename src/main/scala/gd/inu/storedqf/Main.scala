package gd.inu.storedqf

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import gd.inu.storedqf.route.{ApiRouteService, Probing}
import gd.inu.storedqf.service.{Elasticsearch5xClient, StoredQueryService}
import gd.inu.storedqf.service.ElasticsearchClientService.ElasticsearchClientFactory

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Created by henry on 3/27/17.
  */
object Main extends App {

    val config = ConfigFactory.load()
    val appName = "storedqf"

    implicit val system = ActorSystem(appName)
    implicit val mat = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher
    implicit val esClientFactory: ElasticsearchClientFactory = () => new Elasticsearch5xClient()

    val storedq = system.actorOf(StoredQueryService.props, "storedq-service")
    val service = new ApiRouteService(storedq)


    val httpHandler =  Http().bindAndHandle(service.route, "0.0.0.0", config.getInt("http.port"))

    import utils.net.Connectivity._

    lazy val graceShutdown: Future[Unit] =
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
