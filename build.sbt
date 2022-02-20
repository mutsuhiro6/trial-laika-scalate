val scala3Version = "3.1.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "trial-laika",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    // scalacOptions ++= Seq("-new-syntax", "-rewrite"),
    scalacOptions ++= Seq("-indent", "-rewrite"),
    libraryDependencies ++= Seq(
      "org.planet42" %% "laika-core" % "0.18.1",
      "org.scalatra.scalate" % "scalate-core_2.13" % "1.9.7"
    )
  )
