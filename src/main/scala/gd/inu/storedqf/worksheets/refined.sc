
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.api.RefType.applyRef
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Interval
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

// (.+-\d+)\s([\s\S]+)\s$
object VttParts {
  type  Cueid = String Refined MatchesRegex[W.`".+-[0-9]+$"`.T]

  case class Cue(id: Cueid)
}

import VttParts._

val sample = Cue("agent0-1224")
assert(sample.id.value == "agent0-1224")
val bad = Cue("a-1a224")











