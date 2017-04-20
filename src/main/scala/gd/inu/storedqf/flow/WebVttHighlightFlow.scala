package gd.inu.storedqf.flow

import akka.NotUsed
import akka.stream.SourceShape
import akka.stream.scaladsl.{GraphDSL, Source, ZipWith2}

/**
  * Created by henry on 4/17/17.
  */

trait WebVttHighlightFlow[Q, D, H, W] {

  val query:          Source[Q, NotUsed]
  val doc:            Source[D, NotUsed]
  val percolate:      ZipWith2[Q, D, H]
  val assemble:  ZipWith2[H, D, W]

  def webvtt = Source.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val percolateZip = b.add(percolate)
    val assembleZip = b.add(assemble)

    query ~> percolateZip.in0
    doc   ~> percolateZip.in1; percolateZip.out ~> assembleZip.in0
                                            doc ~> assembleZip.in1

    SourceShape(assembleZip.out)
  })

}
