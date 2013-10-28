import AssemblyKeys._
import com.typesafe.startscript.StartScriptPlugin

name := "sendhub"

version := "0.1-SNAPSHOT"

organization := "org.roger"

scalaVersion := "2.10.2"

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xlog-reflective-calls",
  //"-Xlint",
  //"-Xfatal-warnings",
  "-language:implicitConversions",    // allow implicit defs
  "-encoding", "utf8"
)

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype snapshots"  at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Spray Repository"    at "http://repo.spray.io",
  "Spray Nightlies"     at "http://nightlies.spray.io/")

libraryDependencies ++= {
  val akkaVersion       = "2.2.0"
  val sprayVersion      = "1.2-20130712"
  Seq(
    "com.typesafe.akka"             %% "akka-actor"           % akkaVersion,
    "com.typesafe.akka"             %% "akka-slf4j"           % akkaVersion,
    "com.typesafe.akka"             %%  "akka-testkit"        % akkaVersion   % "test",
    "io.spray"                      %  "spray-can"            % sprayVersion ,
    "io.spray"                      %  "spray-http"           % sprayVersion ,
    "io.spray"                      %  "spray-httpx"          % sprayVersion ,
    "io.spray"                      %  "spray-routing"        % sprayVersion ,
    "io.spray"                      %  "spray-client"         % sprayVersion ,
    "io.spray"                      %% "spray-json"           % "1.2.5"  ,
    "ch.qos.logback"                %  "logback-classic"      % "1.0.10",
    "com.typesafe.akka"             %%  "akka-testkit"        % akkaVersion   % "test",
    "org.json4s"                    %%  "json4s-native"       % "3.2.2"
  )
}

conflictWarning in ThisBuild := ConflictWarning.disable

// Assembly settings
mainClass in Global := Some("com.roger.Main")

jarName in assembly := "roger-server.jar"

assemblySettings

// StartScript settings
seq(StartScriptPlugin.startScriptForClassesSettings: _*)