name := "Uno-Model"
organization  := "de.htwg.se"
version       := "0.1.0-SNAPSHOT"
scalaVersion  := "3.1.1"

scalacOptions ++= Seq("-Xignore-scala2-macros")

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
  dependencies.slick,
  dependencies.slickHikaricp,
  dependencies.mysql,
  dependencies.githubSlick
)

libraryDependencies ++= commonDependencies

resolvers += "jitpack" at "https://jitpack.io"