import sbt._

object dependencies {
    val scalactic = "org.scalactic" %% "scalactic" % versionNumber.scalactic
    val scalatest = "org.scalatest" %% "scalatest" % versionNumber.scalatest % "test"
    val scalalangmodulesXml = "org.scala-lang.modules" %% "scala-xml" % versionNumber.scalalangmodulesXml
    val scalalangmodulesSwing = "org.scala-lang.modules" % "scala-swing_2.13" % versionNumber.scalalangmodulesSwing
    val googleinject = "com.google.inject" % "guice"% versionNumber.googleinject
    val typesafeplay = "com.typesafe.play" %% "play-json" % versionNumber.typesafeplay
}

object versionNumber {
    val scalactic = "3.2.11"
    val scalatest = "3.2.11"
    val googleinject = "5.1.0"
    val scalalangmodulesXml = "2.0.1"
    val scalalangmodulesSwing = "3.0.0"
    val typesafeplay = "2.10.0-RC5"
}