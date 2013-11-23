import sbt._, sbt.Keys._
object ScalaRedisQueueBuild extends Build {
  lazy val proj = Project(
    id = "scala-redis-queue",
    base = file("."),
    settings = Project.defaultSettings
  ).dependsOn(
    uri("git://github.com/debasishg/scala-redis.git#v2.11")
  )
}
