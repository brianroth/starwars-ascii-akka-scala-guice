import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.PlayScala
import sbt._
import Keys._
import play.sbt.PlayImport._


object ApplicationBuild extends Build {

  val appName = "starwars-ascii-image-creator"
  val appVersion = "1.0"
  val akkaVersion  = "2.3.9"
  val appDependencies = Seq(
    jdbc,
    specs2 % Test,
    "com.typesafe.play" %% "play-slick" % "1.1.0",
    "commons-codec" % "commons-codec" % "1.7",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.google.inject" % "guice" % "4.0",
    "net.codingwell" %% "scala-guice" % "4.0.0",
    "org.scalaz.stream" % "scalaz-stream_2.11" % "0.8",
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "jline" % "jline" % "2.11",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  )

  val main = Project(appName, file("."))
    .enablePlugins(PlayScala)
    .settings(
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-Xlint",
      "-Ywarn-dead-code",
      "-language:_",
      "-target:jvm-1.7",
      "-encoding", "UTF-8"
    ),
    libraryDependencies ++= appDependencies,
    version := appVersion

  ).settings()

}

