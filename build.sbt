name := "TripleCounter"

version := "0.1"

scalaVersion := "2.13.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test"


/*mainClass in (Compile, run) := Some("triplecounter.TripleCounter")
mainClass in (Compile, packageBin) := Some("triplecounter.TripleCounter")*/

artifactPath in packageBin in Compile := baseDirectory.value / "triplecounter.jar"

