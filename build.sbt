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
  "com.twitter.finatra" %% "finatra-http" % versions.finatra,
  "com.twitter.finatra" %% "finatra-httpclient" % versions.finatra,
  "com.twitter.finatra" %% "finatra-slf4j" % versions.finatra,
  "com.twitter.finatra" %% "finatra-jackson" % versions.finatra,
  "com.github.rlazoti" %% "finagle-metrics" % versions.finagleMetrics,
  "net.fwbrasil" %% "activate-core" % versions.activateVersion,
  "net.fwbrasil" %% "activate-mongo-async" % versions.activateVersion,
  "net.fwbrasil" %% "activate-mongo" % versions.activateVersion,
  "com.newrelic.agent.java" % "newrelic-api" % versions.newrelic,
  "com.newrelic.agent.java" % "newrelic-agent" % "3.26.1",
  "com.google.code.gson" % "gson" % "2.6.2",
  "net.fwbrasil" %% "activate-jdbc" % versions.activateVersion,
  "commons-beanutils" % "commons-beanutils" % "1.9.2",
  "mysql" % "mysql-connector-java" % "5.1.16",
  "net.debasishg" %% "redisclient" % "3.0",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.logentries" % "logentries-appender" % "1.1.32"
)

mainClass := Some("ar.edu.unq.lids.arq2.server.Server")

