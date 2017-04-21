
import com.sun.tools.javac.code.Type.ForAll
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
import eu.timepit.refined.boolean.AnyOf
import eu.timepit.refined.char
import eu.timepit.refined.numeric._
import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.string._
import shapeless.{ ::, HNil }
import shapeless.nat._3


object VttParts {
  type  Cueid = String Refined MatchesRegex[W.`"^[A-Za-z0-9_]+-[0-9]+$"`.T]
  case class Cue(id: Cueid)
}

import VttParts._

val sample = Cue("agent0-1224")
//assert(sample.id.value == "agent0-1224")
//val bad = Cue("a-1a224")
val uri: String Refined Uri = "23391"

val s: String Refined And[NonEmpty, Forall[Not[Whitespace]]] = "ab1"


//package object types {
//
//  type VlanType = Interval.Closed[W.`0`.T, W.`4096`.T]
//  type VlanId = Int Refined VlanType
//
//  type NonBlank = And[NonEmpty, Forall[Not[Whitespace]]]
//  type NonBlankString = String Refined NonBlank
//
//  private val safeStringRegex: Regex = regex("[a-zA-Z0-9.:_ \\[\\]\\/(){}-]{0,255}")
//
//  // split b/c of https://stackoverflow.com/questions/41152928/compile-error-using-the-refined-constraint-interval-closed
//  type SafeStringRegex = MatchesRegex[W.`"[a-zA-Z0-9.:_ (){}-]{0,255}"`.T]
//  type SafeString = String Refined SafeStringRegex
//
//  type NonBlankSafe = And[NonBlank, SafeStringRegex]
//  type NonBlankSafeString = String Refined NonBlankSafe
//
//}
















