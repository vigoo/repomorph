name := "repomorph"

version := "1.0"

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

libraryDependencies += "com.googlecode.scalascriptengine" % "scalascriptengine" % "1.3.6-2.10.3"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.3"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.2.0"

libraryDependencies += "com.jsuereth" %% "scala-arm" % "1.3"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "com.aragost.javahg" % "javahg" % "0.4"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test"

resolvers += Resolver.sonatypeRepo("public")