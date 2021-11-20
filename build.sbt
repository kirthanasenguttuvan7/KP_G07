name := """Gitterific"""
organization := "Concordia"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"
libraryDependencies += "org.mockito" % "mockito-core" % "2.10.0" % "test"

libraryDependencies += guice
libraryDependencies ++= Seq(
  ws
)