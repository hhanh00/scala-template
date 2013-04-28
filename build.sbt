name := "template"

scalaVersion := "2.10.1"
 
resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
 
seq(com.github.siasia.WebPlugin.webSettings :_*)

libraryDependencies ++= Seq(
  "org.scalaz" % "scalaz-core" % "7.0.0-M9" cross CrossVersion.fullMapped {
  case "2.10.1" => "2.10"
  case x => x
  },
  "com.typesafe.akka" %% "akka-actor" % "2.1.0",
  "com.typesafe.akka" %% "akka-cluster-experimental" % "2.1.2"
)

libraryDependencies ++= {
  val liftVersion = "2.5-RC1"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftmodules"   %% "lift-jquery-module" % (liftVersion + "-2.2"),
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6"
  )
}

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")
 
initialCommands in console := "import scalaz._, Scalaz._"
