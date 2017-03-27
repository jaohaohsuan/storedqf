import sbt._

object Version {
  val akka = "2.4.17"
  val akkaHttp = "10.0.5"
}

object Library {
  val akkaActor                = "com.typesafe.akka"           %% "akka-actor"                           % Version.akka
  val akkaHttp                 = "com.typesafe.akka"           %% "akka-http"                            % Version.akkaHttp
  val logback                  = "ch.qos.logback"               % "logback-classic"              % "1.1.7"

  // testing parts

  val scalatest                = "org.scalatest"               %% "scalatest"                            % "3.0.1"             % "test"
  val akkaHttpTestkit          = "com.typesafe.akka"           %% "akka-http-testkit"                    % Version.akkaHttp    % "test"
}