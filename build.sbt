val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Durak",
    version := "0.0.1",
    scalaVersion := scala3Version,
    Test / parallelExecution := false,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
      "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
      "org.openjfx" % "javafx-media" % "20", // or latest version
      "org.openjfx" % "javafx-base" % "20",
      "org.openjfx" % "javafx-controls" % "20",
      "org.openjfx" % "javafx-swing" % "20"
    )
  )

