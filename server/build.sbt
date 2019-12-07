name := "server"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.0"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-typed" % "2.6.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http"   % "10.1.10"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime
