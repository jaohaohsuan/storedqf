package gd.inu.storedqf.service

import akka.actor.{Actor, Props}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, ZipWith}
import gd.inu.storedqf.flow.WebVttHighlightFlow
import gd.inu.storedqf.format.{PercolateSearchResult, WebVtt}
import gd.inu.storedqf.service.ElasticsearchClientService.ElasticsearchClientFactory
import gd.inu.storedqf.service.StoredQueryService.HighlightLog
import gd.inu.storedqf.utils.http.ImperativeRequestContext
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._

import scala.util.{Failure, Success}

object StoredQueryService {

  def props(implicit esClientFactory: ElasticsearchClientFactory) = Props(new StoredQueryService())
  case class HighlightLog(storedqId: String,logPath: String, docType: String, ctx: ImperativeRequestContext)
}

class StoredQueryService()(implicit val esClientFactory: ElasticsearchClientFactory) extends Actor with Directives {

  import ElasticsearchClient._
  import akka.http.scaladsl.model.HttpMethods._
  import gd.inu.storedqf.utils.http.HttpRequestBodyEncoder._

  private implicit val esClient = esClientFactory()

  def receive: Receive = {
    case cmd@HighlightLog(_, _, _, ctx) =>
      ctx.response {
        extractMaterializer { implicit mat =>
          onComplete(procHighlighting(cmd)) {
            case Success(value) => complete(s"$value")
            case Failure(err)   => complete(InternalServerError, s"$err")
          }
        }
      }
  }

  def procHighlighting(cmd: HighlightLog)(implicit mat: Materializer) = {

    val HighlightLog(storedqId, logPath, docType , _) = cmd

    val docSource = esClient.send(GET / logPath).via(OK.respFlow(identity))
    val percolate = esClient.flow { doc: JValue =>
      GET./("stored-query/_search", s"""{
                          "_source": false,
                          "query": {
                            "bool": {
                              "must": [
                                {
                                  "ids": {
                                    "type": "queries",
                                    "values": [ "$storedqId" ]
                                  }
                                },
                                {
                                  "percolate": {
                                    "field": "query",
                                    "document_type": "$docType",
                                    "document": ${compact(doc \ "_source")}
                                  }
                                }
                              ]
                            }
                          },
                          "highlight": {
                              "pre_tags" : ["<c>"],
                              "post_tags" : ["</c>"],
                              "fields" : {
                                  "agent*" :    { "number_of_fragments" : 0},
                                  "customer*" : { "number_of_fragments" : 0},
                                  "dialogs" :   { "number_of_fragments" : 0}
                              }
                          }
                        }""")
    }.via(OK.respFlow(_ \\ "highlight" ))

    val assemble = ZipWith((hits: JValue, doc: JValue) => new PercolateSearchResult(hits).mergeWith(WebVtt.fromJson(doc)))

    WebVttHighlightFlow.create(docSource, percolate, assemble).runWith(Sink.head)
  }
}
