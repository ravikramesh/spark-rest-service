name := "spark-rest-service"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.livy" % "livy-server" % "0.4.0-incubating",
  "org.apache.livy" % "livy-client-http" % "0.4.0-incubating",
  "org.apache.livy" % "livy-api" % "0.4.0-incubating",
  "org.apache.httpcomponents" % "httpclient" % "4.5.3",
  "org.json" % "json" % "20170516"
)


target in assembly := file("build")

assemblyJarName in assembly := s"${name.value}.jar"