name := "twitter-stream-cats-fs2"

scalaVersion in ThisBuild := "2.11.8"

scalaOrganization := "org.typelevel"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.3",
  "com.eed3si9n" %% "repatch-twitter-core" % "dispatch0.11.1_0.1.0", // https://github.com/eed3si9n/repatch-twitter

  "org.typelevel" %% "cats" % "0.8.1",
  "co.fs2" %% "fs2-core" % "0.9.2",

  "org.typelevel" %% "discipline" % "0.7.1" % Test,
  "org.specs2" %% "specs2-core" % "3.8.5.1" % Test,
  "org.specs2" %% "specs2-scalacheck" % "3.8.5.1" % Test,
  "org.typelevel" %% "cats-laws" % "0.8.1" % Test,
  "org.typelevel" %% "cats-kernel-laws" % "0.8.1" % Test
)

addCommandAlias("formatCode", "scalafmt -i -f src")

addCommandAlias("testExcludeSlow", "testOnly * -- exclude slow")

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-language:implicitConversions"
)
