package gd.inu.storedqf.flow

import akka.NotUsed
import akka.stream.SourceShape
import akka.stream.scaladsl.{Flow, GraphDSL, Source, ZipWith2}

/**
  * Created by henry on 4/17/17.
  */

object WebVttHighlightFlow {

  def create[Q, D, H, W](doc: Source[D, NotUsed], percolate: Flow[D, H, NotUsed], assemble: ZipWith2[H, D, W]) = Source.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val assembleZip = b.add(assemble)

    doc ~> percolate ~> assembleZip.in0
                 doc ~> assembleZip.in1

    SourceShape(assembleZip.out)
  })

}
