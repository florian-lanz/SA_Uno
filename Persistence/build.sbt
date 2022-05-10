name := "Uno-Persistence"
organization  := "de.htwg.se"
version       := "0.1.0-SNAPSHOT"
scalaVersion  := "3.1.1"

lazy val commonDependencies = Seq(
  dependencies.scalactic,
  dependencies.scalatest,
  dependencies.googleinject,
  dependencies.scalalangmodulesXml,
  dependencies.scalalangmodulesSwing,
  dependencies.typesafeplay,
  dependencies.akkaActorTyped,
  dependencies.akkaStream,
  dependencies.akkaActor,
  dependencies.akkaHttp,
  dependencies.slf4jNop,
  dependencies.mysql,
  dependencies.githubSlick
)

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= commonDependencies