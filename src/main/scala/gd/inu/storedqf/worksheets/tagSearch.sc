val pf1 = new PartialFunction[Int,String] {
  def isDefinedAt(x: Int): Boolean = x > 5
  def apply(v1: Int): String = s"pf1 $v1"
}

val pf2 = new PartialFunction[Int,String] {
  def isDefinedAt(x: Int): Boolean = x < 10
  def apply(v1: Int): String = s"pf2 $v1"
}


pf1 orElse pf2 apply 5