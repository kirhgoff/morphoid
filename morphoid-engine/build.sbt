name := "morphoid-engine"

organization := "org.kirhgoff"

version := "2.0"

scalaVersion := "2.12.6"

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += Resolver.mavenLocal

crossPaths := false
isSnapshot := true

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",

    "ch.qos.logback" %  "logback-classic"   % "1.1.3",
    "org.kirhgoff"  %  "morphoid-bridge"   % "2.0"
)

parallelExecution in Test := false

scalacOptions ++= List("-feature","-deprecation", "-unchecked", "-Xlint")

javaOptions += "-Xmx4G"






