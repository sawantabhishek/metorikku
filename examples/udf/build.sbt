name := "udf-example"

version := "1.0"

scalaVersion := "2.12.11"

val sparkVersion = "3.0.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
)