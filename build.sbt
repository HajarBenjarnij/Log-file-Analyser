name := "Streaming"
version := "0.1"
scalaVersion := "2.13.10"
val AkkaVersion = "2.7.0"
libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % "2.13.10",
  "com.lightbend.akka" %% "akka-stream-alpakka-file" % "5.0.0",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test
)

