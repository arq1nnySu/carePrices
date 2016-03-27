//port com.typesafe.sbt.SbtNativePackager._

//ckageArchetype.java_application

organization := "ar.edu.unq.lids.arq2"
name := "carePrices"
version := "1.0"
scalaVersion := "2.11.8"
//fork in run := true
//parallelExecution in ThisBuild := false

resolvers += "twttr" at "https://maven.twttr.com/"


lazy val versions = new {
  val finatra = "2.1.5"
  val guice = "4.0"
  val logback = "1.0.13"
  val finagleMetrics = "0.0.2"
}

libraryDependencies ++= Seq(
  "com.twitter.finatra" %% "finatra-http" % versions.finatra,
  "com.twitter.finatra" %% "finatra-httpclient" % versions.finatra,
  "ch.qos.logback" % "logback-classic" % versions.logback,
  "com.github.rlazoti" %% "finagle-metrics" % versions.finagleMetrics
)