name := "repomorph"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.googlecode.scalascriptengine" %% "scalascriptengine" % "1.3.10",
  "org.scala-lang" % "scala-compiler" % "2.11.1",
  "com.github.scopt" %% "scopt" % "3.2.0",
  "com.jsuereth" %% "scala-arm" % "1.4",
  "commons-io" % "commons-io" % "2.4",
  "com.aragost.javahg" % "javahg" % "0.5-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "2.1.4" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.1.4" % "test"
)

resolvers += Resolver.sonatypeRepo("public")

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

fork in ( Test, run ) := true
