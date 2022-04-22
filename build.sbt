import sbt.Keys.libraryDependencies

val projectVersion = "0.1.0-SNAPSHOT"
val scala3Version = "3.1.1"

lazy val commonDependencies = Seq(
    dependencies.scalactic,
    dependencies.scalatest,
    dependencies.googleinject,
    dependencies.scalalangmodulesXml,
    dependencies.scalalangmodulesSwing,
    dependencies.typesafeplay
)

lazy val commonSettings = Seq(
  scalaVersion := scala3Version,
  organization := "de.htwg.se",
)

lazy val root = project
  .in(file("."))
  .aggregate(persistence)
  .dependsOn(tools, model, persistence)
  .settings(
    name := "Uno",
    version := projectVersion,
    commonSettings,
    libraryDependencies ++= commonDependencies,
  )

lazy val persistence = (project in file("Persistence"))
  .dependsOn(model)
  .settings(
    name := "Uno-Persistence",
    version := projectVersion,
    commonSettings,
    libraryDependencies ++= commonDependencies,
  )

lazy val tools = (project in file("Tools"))
  .settings(
    name := "Uno-Tools",
    version := projectVersion,
    commonSettings,
    libraryDependencies ++= commonDependencies,
  )

lazy val model = (project in file("Model"))
  .settings(
    name := "Uno-Model",
    version := projectVersion,
    commonSettings,
    libraryDependencies ++= commonDependencies,
  )