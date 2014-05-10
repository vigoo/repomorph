name := "repomorph"

version := "1.0"

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

//libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test"

libraryDependencies ++= Seq(
  "com.googlecode.scalascriptengine" % "scalascriptengine" % "1.3.6-2.10.3",
  "org.scala-lang" % "scala-compiler" % "2.10.3",
  "com.github.scopt" %% "scopt" % "3.2.0",
  "com.jsuereth" %% "scala-arm" % "1.3",
  "commons-io" % "commons-io" % "2.4",
  "com.aragost.javahg" % "javahg" % "0.4",
  "org.scalatest" %% "scalatest" % "2.1.4" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.1.RC1" % "test"
)

resolvers += Resolver.sonatypeRepo("public")