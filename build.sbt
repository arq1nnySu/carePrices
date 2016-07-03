import NativePackagerKeys._

packageArchetype.java_application

organization := "ar.edu.unq.lids.arq2"
name := "carePrices"
version := "1.0"
scalaVersion := "2.11.8"
fork in run := true
parallelExecution in ThisBuild := false

incOptions := incOptions.value.withNameHashing(false)

resolvers += "twttr" at "https://maven.twttr.com/"
resolvers += "fwbrasil.net" at "http://repo1.maven.org/maven2"
resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Typesafe repository" at "https://dl.bintray.com/typesafe/maven-releases/"


lazy val versions = new {
  val finatra = "2.1.5"
  val guice = "4.0"
  val logback = "1.0.13"
  val finagleMetrics = "0.0.2"
  val newrelic = "3.28.0"
  val activateVersion = "1.7"
}

lazy val careprices = (project in file(".")).
  enablePlugins(GatlingPlugin).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "ar.edu.unq.lids.arq2",
    buildInfoOptions += BuildInfoOption.ToJson
  )

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % "2.11.8",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
  "commons-beanutils" % "commons-beanutils" % "1.9.2",
  "com.twitter.finatra" %% "finatra-http" % "2.1.5",
  "com.twitter.finatra" %% "finatra-httpclient" % "2.1.5",
  "com.twitter.finatra" %% "finatra-slf4j" % "2.1.5",
  "com.twitter.finatra" %% "finatra-jackson" % "2.1.5",
  "com.github.rlazoti" %% "finagle-metrics" % "0.0.2",
  "net.fwbrasil" %% "activate-core" % "1.7",
  "net.fwbrasil" %% "activate-mongo-async" % "1.7",
  "net.fwbrasil" %% "activate-mongo" % "1.7",
  "com.newrelic.agent.java" % "newrelic-api" % "3.28.0",
  "com.newrelic.agent.java" % "newrelic-agent" % "3.26.1",
  "com.google.code.gson" % "gson" % "2.6.2",
  "net.fwbrasil" %% "activate-jdbc" % "1.7",
  "commons-beanutils" % "commons-beanutils" % "1.9.2",
  "mysql" % "mysql-connector-java" % "5.1.16",
  "net.debasishg" %% "redisclient" % "3.0",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.logentries" % "logentries-appender" % "1.1.32",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.5" % "test",
  "io.gatling" % "gatling-test-framework" % "2.1.5" % "test",
  "org.scalaj" %% "scalaj-http" % "2.3.0"  % "test"
)

mainClass := Some("ar.edu.unq.lids.arq2.server.Server")

