import sbt._
import Keys._
import com.mojolly.scalate.ScalatePlugin._
import coffeescript.Plugin._
import ScalateKeys._

object AppBuild extends Build {
  val appName = "scala-template"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.2.0",
    "com.typesafe.akka" %% "akka-slf4j" % "2.2.0",
    "ch.qos.logback" % "logback-classic" % "1.0.13",
    "ch.qos.logback" % "logback-core" % "1.0.13",
    "io.spray" % "spray-can" % "1.2-20131004",
    "io.spray" % "spray-routing" % "1.2-20131004",
    "io.spray" % "spray-caching" % "1.2-20131004",
    "org.fusesource.scalate" %% "scalate-core" % "1.6.1")

  val main = Project(appName, file("."), settings = Project.defaultSettings ++ scalateSettings ++ coffeeSettings ++ Seq(
    resolvers ++= Seq("spray.io nightlies" at "http://nightlies.spray.io"),
    libraryDependencies ++= appDependencies,
    scalateTemplateConfig in Compile <<= (sourceDirectory in Compile) { base =>
      Seq(
        TemplateConfig(
          base / "views",
          Seq(
            "import models._"),
          Seq(),
          Some("webTmpl")))
    },
    distKey <<= (Keys.target, Keys.name, Keys.version, packageBin in Compile, dependencyClasspath in Compile) map copyDep)
  )

  def listDir(f: File): Seq[File] = if (f.isDirectory) IO.listFiles(f).toList.flatMap(listDir(_)) else List(f)

  val distKey = TaskKey[File]("dist", "create distribution package")
  def copyDep(target: File, name: String, version: String, bin: File, dependencies: Seq[Attributed[File]]): File = {
    println("Creating distribution package")
    val zipFile = target / (s"$name-$version.zip")
    val files = bin +: (dependencies map (_.data))
    val confFiles = listDir(file("conf"))
    val scriptFiles = listDir(file("scripts"))
    val zip = (files x flatRebase("libs")) ++ (confFiles x flatRebase("conf")) ++ (scriptFiles x flatRebase("scripts"))

    IO.zip(zip, zipFile)
    zipFile
  }
}
