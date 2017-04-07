package gd.inu.storedqf.routing

/**
  * Created by henry on 3/31/17.
  */
object StoredQueryIndex {

  import org.json4s._
  import org.json4s.JsonDSL._
  import org.json4s.jackson.JsonMethods._

  def percolatQuery(doc: JValue, docType: String) = {
    "query" ->
      "percolate" ->
        ("field"         -> "query") ~
        ("document_type" -> docType) ~
        ("document"      -> doc)
  }
}
