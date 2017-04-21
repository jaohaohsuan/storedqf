package gd.inu.storedqf.service

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Source}
import com.typesafe.config.ConfigFactory
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.JsonAST.JValue
import org.json4s.{DefaultFormats, jackson}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henry on 4/21/17.
  */

trait ElasticsearchClient {

  def send(req: HttpRequest): Source[HttpResponse, NotUsed]
  def response[T](returnCode: StatusCode = OK)(f: JValue => T)(implicit mat: Materializer): Flow[HttpResponse, T, NotUsed]
  def flow[T](f: T => HttpRequest): Flow[T, HttpResponse, NotUsed]
}

object ElasticsearchClient {

  implicit class statusCode(status: StatusCode) {
    def response[T](f: JValue => T)(implicit client: ElasticsearchClient, mat: Materializer) = {
      client.response[T](status)(f)
    }
  }

//  implicit class httpRequest(req: HttpRequest) {
//    def viaES()(implicit client: ElasticsearchClient): Source[HttpResponse, NotUsed] = {
//      client.sendHttpReq(req)
//    }
//  }
}

class Elasticsearch5xClient(implicit val system: ActorSystem) extends ElasticsearchClient
  with Json4sSupport {

  implicit val ec: ExecutionContext = system.dispatcher
  implicit val serialization = jackson.Serialization // or native.Serialization
  implicit val formats = DefaultFormats

  private val config = {
    ConfigFactory.load().getConfig("service.elasticsearch")
  }

  private val endpoint: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = Http().outgoingConnection(config.getString("addr"), config.getInt("port"))

  def send(req: HttpRequest): Source[HttpResponse, NotUsed] = Source.single(req).via(endpoint)

  def response[T](returnCode: StatusCode = OK)(f: JValue => T)(implicit mat: Materializer): Flow[HttpResponse, T, NotUsed] = {
    Flow[HttpResponse].mapAsync(1) {
      case HttpResponse(status, _, entity, _) if returnCode == status =>
        Unmarshal(entity).to[JValue].map(f)
      case resp => Future.failed(new Exception(s"unexpected: $resp"))
    }
  }

  def flow[T](f: T => HttpRequest): Flow[T, HttpResponse, NotUsed] = {
    Flow[T].map(f).via(endpoint)
  }

}

object ElasticsearchClientService {
  type ElasticsearchClientFactory = () => ElasticsearchClient
}
