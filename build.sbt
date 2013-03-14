import AssemblyKeys._

name := "urlPing"

organization := "com.skechers"

version := "1.1"

scalaVersion := "2.10.0"

description := "ping a url continuously"

assemblySettings

resolvers ++= Seq(
	"Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
     "commons-httpclient" % "commons-httpclient" % "3.1"
    ,"org.slf4j" % "slf4j-log4j12" % "1.7.2"
    ,"org.slf4j" % "slf4j-api" % "1.7.2"
    ,"com.github.scopt" %% "scopt" % "2.1.0"
)
