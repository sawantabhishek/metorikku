name := "metorikku"
organization := "com.yotpo"
homepage := Some(url("https://github.com/YotpoLtd/metorikku"))
licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.html"))
scmInfo := Some(
  ScmInfo(url("https://github.com/YotpoLtd/metorikku"),
    "scm:git:git@github.com:YotpoLtd/metorikku.git"))
developers := List(
  Developer(id="amitco1", name="Amit Cohen", email="", url=url("http://www.yotpo.com")),
  Developer(id="avichay", name="Avichay Etzioni", email="", url=url("http://www.yotpo.com")),
  Developer(id="dporat", name="Doron Porat", email="", url=url("http://www.yotpo.com")),
  Developer(id="etrabelsi", name="Eyal Trabelsi", email="", url=url("http://www.yotpo.com")),
  Developer(id="lyogev", name="Liran Yogev", email="", url=url("http://www.yotpo.com")),
  Developer(id="ofirventura", name="Ofir Ventura", email="", url=url("http://www.yotpo.com")),
  Developer(id="nuriyan", name="Nadav Bar Uriyan", email="", url=url("http://www.yotpo.com")),
  Developer(id="ronbarab", name="Ron Barabash", email="", url=url("http://www.yotpo.com")),
  Developer(id="shirbr", name="Shir Bromberg", email="", url=url("http://www.yotpo.com"))
)

scalaVersion := "2.12.11"
val sparkVersion = Option(System.getProperty("sparkVersion")).getOrElse("3.0.0")
val jacksonVersion = "2.10.0"

lazy val excludeJpountz = ExclusionRule(organization = "net.jpountz.lz4", name = "lz4")
lazy val excludeNetty = ExclusionRule(organization = "io.netty", name = "netty")
lazy val excludeNettyAll = ExclusionRule(organization = "io.netty", name = "netty-all")
lazy val excludeAvro = ExclusionRule(organization = "org.apache.avro", name = "avro")
lazy val excludeSpark = ExclusionRule(organization = "org.apache.spark")
lazy val excludeLog4j = ExclusionRule(organization = "org.apache.logging.log4j")
lazy val excludeParquet = ExclusionRule(organization = "org.apache.parquet")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-hive" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-avro" % sparkVersion % "provided",

  "com.holdenkarau" %% "spark-testing-base" % "2.4.5_0.14.0" % "test" excludeAll excludeSpark,

  "com.github.scopt" %% "scopt" % "3.7.1",
  "org.scala-lang" % "scala-library" % scalaVersion.value,
  "com.typesafe.play" %% "play-json" % "2.9.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion,
  "org.apache.commons" % "commons-text" % "1.8",
  "org.influxdb" % "influxdb-java" % "2.19",
  // Wait for https://github.com/spark-redshift-community/spark-redshift/pull/72
  //  "io.github.spark-redshift-community" %% "spark-redshift" % "4.0.1",
  //  "com.databricks" %% "spark-redshift" % "3.0.0-preview1" excludeAll excludeAvro,
  "com.segment.analytics.java" % "analytics" % "2.1.1" % "provided",
  "com.amazon.redshift" % "redshift-jdbc42" % "1.2.41.1065" % "provided",
  "com.datastax.spark" %% "spark-cassandra-connector" % "3.0.0-alpha2" % "provided",
  "com.redislabs" %% "spark-redis" % "2.5.0" % "provided",
  "org.apache.kafka" %% "kafka" % "2.2.0" % "provided",
  "za.co.absa" %% "abris" % "3.2.0"  % "provided" excludeAll(excludeAvro, excludeSpark),
  "org.apache.hudi" %% "hudi-spark-bundle" % "0.5.3" % "provided",
  "org.apache.parquet" % "parquet-avro" % "1.10.1" % "provided",
  "org.apache.avro" % "avro" % "1.8.2" % "provided",
  "org.apache.hadoop" % "hadoop-aws" % "2.7.4" % "provided"
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("public"),
  Resolver.bintrayRepo("spark-packages", "maven"),
  "redshift" at "https://s3.amazonaws.com/redshift-maven-repository/release",
  "confluent" at "https://packages.confluent.io/maven/"
)

fork := true
javaOptions in Test ++= Seq("-Dspark.master=local[*]", "-Dspark.sql.session.timeZone=UTC", "-Duser.timezone=UTC")

// Assembly settings
Project.inConfig(Test)(baseAssemblySettings)

assemblyMergeStrategy in (Test, assembly) := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case PathList("LICENSE", xs@_*) => MergeStrategy.discard
  case PathList("META-INF", "services", xs@_*) => MergeStrategy.filterDistinctLines
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case "log4j.properties" => MergeStrategy.first
  case _ => MergeStrategy.first
}

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case PathList("LICENSE", xs@_*) => MergeStrategy.discard
  case PathList("META-INF", "services", xs@_*) => MergeStrategy.filterDistinctLines
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case "log4j.properties" => MergeStrategy.first
  case _ => MergeStrategy.first
}

assemblyShadeRules in (Test, assembly) := Seq(
  ShadeRule.rename("com.google.**" -> "shadeio.@1").inAll
)
assemblyJarName in assembly := "metorikku.jar"
assemblyJarName in (Test, assembly) := s"${name.value}-standalone.jar"
assemblyOption in assembly := (assemblyOption in assembly).value.copy(cacheOutput = false)
assemblyOption in assembly := (assemblyOption in assembly).value.copy(cacheUnzip = false)
assemblyOption in (Test, assembly) := (assemblyOption in (Test, assembly)).value.copy(cacheOutput = false)
assemblyOption in (Test, assembly) := (assemblyOption in (Test, assembly)).value.copy(cacheUnzip = false)


logLevel in assembly := Level.Error
logLevel in (Test, assembly) := Level.Error

// Publish settings
publishMavenStyle := true

credentials += Credentials("Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  sys.env.getOrElse("REPO_USER", ""),
  sys.env.getOrElse("REPO_PASSWORD", ""))

// Add sonatype repository settings
publishTo := Some(
  if (isSnapshot.value){
    Opts.resolver.sonatypeSnapshots
  }
  else {
    Opts.resolver.sonatypeStaging
  }
)

pgpPublicRing := baseDirectory.value / "project" / ".gnupg" / "pubring.asc"
pgpSecretRing := baseDirectory.value / "project" / ".gnupg" / "secring.asc"
pgpPassphrase := sys.env.get("PGP_PASS").map(_.toArray)

// Release settings (don't automatically publish upon release)
import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
//  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges
)

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.withClassifier(Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

// Fix for SBT run to include the provided at runtime
run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated
