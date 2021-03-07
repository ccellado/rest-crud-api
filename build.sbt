ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := ""
ThisBuild / organizationName := ""

val hedgehogVersion = "0.6.5"

lazy val testDeps = Seq(
    "org.scalatest" %% "scalatest" % "3.2.5" % Test,
    "org.scalacheck" %% "scalacheck" % "1.15.2" % Test,
    "org.scalatestplus" %% "scalacheck-1-15" % "3.2.5.0" % Test,

    "qa.hedgehog" %% "hedgehog-core" % hedgehogVersion,
    "qa.hedgehog" %% "hedgehog-runner" % hedgehogVersion,
    "qa.hedgehog" %% "hedgehog-sbt" % hedgehogVersion
)

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.4"

lazy val akkaDeps = Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
)

lazy val dataDeps = Seq(
    "com.github.pureconfig" %% "pureconfig" % "0.14.1",
    "org.postgresql" % "postgresql" % "42.2.8",
    "io.getquill" %% "quill-jdbc" % "3.6.1",
    "io.getquill" %% "quill-jasync-postgres" % "3.6.1",
    "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.1",
    "org.slf4j" % "slf4j-log4j12" % "1.7.15"
)

lazy val root = (project in file("."))
  .settings(
        name := "rest-crud-api"
      , libraryDependencies ++= testDeps ++ akkaDeps ++ dataDeps
      , testFrameworks += TestFramework("hedgehog.sbt.Framework")
  )