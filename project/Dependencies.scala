import sbt._

object Version {
  val akka     = "2.5.0"
  val akkaHttp = "10.0.5"
}

object Library {
  lazy val akkaActor       = "com.typesafe.akka" %% "akka-actor" % Version.akka
  lazy val akkaHttp        = "com.typesafe.akka" %% "akka-http" % Version.akkaHttp
  lazy val logback         = "ch.qos.logback" % "logback-classic" % "1.1.7"
  lazy val nscalaTime      = "com.github.nscala-time" %% "nscala-time" % "2.16.0"
  lazy val json4sJackson   = "org.json4s" %% "json4s-jackson" % "3.5.1"
  lazy val refined         = "eu.timepit" %% "refined" % "0.8.0"
  lazy val scalatest       = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  lazy val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp % "test"
  lazy val scalacheck      = "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
  lazy val cucumberRunner  = "com.waioeka.sbt" %% "cucumber-runner" % "0.0.8"
  lazy val akkaHttpJson4s  = "de.heikoseeberger" %% "akka-http-json4s" % "1.15.0"


  lazy val cucumber = Seq(
    "info.cukes" % "cucumber-core" ,
    "info.cukes" %% "cucumber-scala",
    "info.cukes" % "cucumber-jvm",
    "info.cukes" % "cucumber-junit"
  ).map(_ % "1.2.5").map(_ % "test")
}
