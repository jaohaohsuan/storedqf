package gd.inu.storedqf.collection

/**
  * Created by henry on 4/7/17.
  */
trait CueCollection[A,B] {

  val values: Map[Int, A]

  def out: Seq[A] = { values.values.toList.sortWith(compare) }

  val compare: (A,A) => Boolean

}
