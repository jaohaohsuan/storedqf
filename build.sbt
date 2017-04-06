import Library._


val cpJarsForDocker = taskKey[Unit]("prepare for building Docker image")


val root = (project in file(".")).settings(
  name := "storedqf",
  version := "1.0",
  scalaVersion := "2.11.8",
  exportJars := true,
  libraryDependencies  ++= Seq(
    logback,
    nscalaTime, json4sJackson,
    akkaHttp,
    akkaHttpTestkit,
    scalatest, scalacheck
  ),
  cpJarsForDocker := {

    val dockerDir = (target in Compile).value / "docker"

    val jar = (packageBin in Compile).value
    IO.copyFile(jar, dockerDir / "app" / jar.name)

    (dependencyClasspath in Compile).value.files.foreach { f => IO.copyFile(f, dockerDir / "libs" / f.name )}

    (mainClass in Compile).value.foreach { content => IO.write( dockerDir / "mainClass", content ) }

    IO.copyFile(baseDirectory.value / "Dockerfile", dockerDir / "Dockerfile")
  }
)
        
