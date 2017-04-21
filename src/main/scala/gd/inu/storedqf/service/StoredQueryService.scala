package gd.inu.storedqf.service

import akka.actor.{Actor, Props}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, ZipWith}
import gd.inu.storedqf.flow.WebVttHighlightFlow
import gd.inu.storedqf.format.{CueString, WebVtt}
import gd.inu.storedqf.service.ElasticsearchClientService.ElasticsearchClientFactory
import gd.inu.storedqf.service.StoredQueryService.HighlightLog
import gd.inu.storedqf.text.{HighlightFragment, Highlighter}
import gd.inu.storedqf.utils.http.ImperativeRequestContext
import org.json4s.JsonAST.{JString, JValue}
import org.json4s.jackson.JsonMethods._

import scala.util.{Failure, Success}

object StoredQueryService {

  def props(implicit esClientFactory: ElasticsearchClientFactory) = Props(new StoredQueryService())
  case class HighlightLog(storedqId: String,logPath: String, ctx: ImperativeRequestContext)
}

class StoredQueryService()(implicit val esClientFactory: ElasticsearchClientFactory) extends Actor with Directives {

  import ElasticsearchClient._
  import akka.http.scaladsl.model.HttpMethods._
  import gd.inu.storedqf.utils.http.HttpRequestBodyEncoder._

  private implicit val esClient = esClientFactory()

  def receive: Receive = {
    case cmd@HighlightLog(_, _, ctx) =>
      ctx.response {
        extractMaterializer { implicit mat =>
          onComplete(createHighlightLogGraph(cmd).runWith(Sink.head)) {
            case Success(value) => complete(s"$value")
            case Failure(err)   => complete(InternalServerError, s"$err")
          }
        }
      }
  }

  def createHighlightLogGraph(cmd: HighlightLog)(implicit mat: Materializer) = {

    val HighlightLog(storedqId, logPath, _) = cmd

    val docSource = (GET / logPath).viaES().via(OK.respFlow(identity))
    val percolate = Flow[JValue].map { doc =>

      val JString(docType) = doc \ "_type"

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

    } via esClient.endpoint via OK.respFlow(_ \\ "highlight" )

    val assemble = ZipWith((highlightFragments: JValue, doc: JValue) => {

      val vtt = (doc \ "_source" \ "vtt" \\ classOf[JString]).map(new CueString(_)).foldLeft(""){ (ac, el) => ac ++ s"\n$el"}
      val highlightResults = (highlightFragments \\ classOf[JString]).map(new HighlightFragment(_))
      import Highlighter._

      val highlightedVtt = highlightResults.foldLeft(vtt){ (ac, fragment) =>
        new WebVtt(ac).substitute(fragment)
      }

      s"WEBVTT\n$highlightedVtt"
    })

    WebVttHighlightFlow.create(docSource, percolate, assemble)
  }

}
