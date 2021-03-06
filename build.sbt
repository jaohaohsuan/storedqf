import Library._


val cpJarsForDocker = taskKey[Unit]("prepare for building Docker image")
val framework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")

val root = (project in file(".")).settings(
  name := "storedqf",
  version := "1.0",
  scalaVersion := "2.11.8",
  exportJars in compile := true,
  libraryDependencies  ++= Seq(
    refined,
    logback,
    nscalaTime, json4sJackson,
    akkaHttp,
    akkaHttpTestkit,
    scalatest, scalacheck, cucumberRunner
  ) ++ cucumber,
  cpJarsForDocker := {

    val dockerDir = (target in Compile).value / "docker"

    val jar = (packageBin in Compile).value
    IO.copyFile(jar, dockerDir / "app" / jar.name)

    (dependencyClasspath in Compile).value.files.foreach { f => IO.copyFile(f, dockerDir / "libs" / f.name )}

    (mainClass in Compile).value.foreach { content => IO.write( dockerDir / "mainClass", content ) }

    IO.copyFile(baseDirectory.value / "Dockerfile", dockerDir / "Dockerfile")
  },
  testFrameworks += framework,
  testOptions ++= Seq(
    Tests.Argument(framework,"--glue",""),
    Tests.Argument(framework,"--plugin","pretty"),
    Tests.Argument(framework,"--plugin","html:/tmp/html"),
    Tests.Argument(framework,"--plugin","json:target/cucumber/storedqf.json")
  ),
  CucumberPlugin.glue := "gd/inu/storedqf"
)
