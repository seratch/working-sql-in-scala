scalaVersion := "2.11.5"
libraryDependencies ++= Seq(
  "org.scalikejdbc"    %% "scalikejdbc"     % "2.2.4",
  "com.typesafe.slick" %% "slick"           % "2.1.0",
  "com.typesafe.play"  %% "anorm"           % "2.3.8",
  "com.h2database"     %  "h2"              % "1.4.185",
  "ch.qos.logback"     %  "logback-classic" % "1.1.2"
)
// for Anorm 2.3
resolvers += "typesafe repo" at "https://repo.typesafe.com/typesafe/releases"
scalariformSettings
