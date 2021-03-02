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

lazy val root = (project in file("."))
  .settings(
        name := "rest-crud-api"
      , libraryDependencies ++= testDeps
      , testFrameworks += TestFramework("hedgehog.sbt.Framework")
  )