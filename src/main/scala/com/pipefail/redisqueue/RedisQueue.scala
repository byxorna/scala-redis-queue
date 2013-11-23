package com.pipefail.redisqueue

import com.redis._

object Queue {

  private var queue: Option[String] = None

  def getQueue(): Option[String] = queue

  def setQueue(q: String): String = {
    println("Setting queue to " + q)
    queue = Some(q)
    queue.get
  }

}

class Queue(client: RedisClient, namespace: String) {
  // get access to the attrs in the object
  import Queue._

  //val client = new RedisClient(host,port)
  def this(host: String, port: Int, namespace: String = "redisqueue") = this(new RedisClient(host, port), namespace)

  // set default values for connection
  def this() = this("localhost",6379)


  def put(message: String) = queue match {
    case None     => false
    case Some(q)  => {
      client.set(List(namespace,q).mkString("::"),message)
    }
  }
}
