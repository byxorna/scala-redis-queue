package com.pipefail.redisqueue

import com.redis._
import com.pipefail.redisqueue._

class Queue(client: RedisClient, namespace: String, queue: String) {

  def this(host: String, port: Int, queue: String, namespace: String = "redisqueue") = this(new RedisClient(host, port), queue, namespace)

  // set default values for connection
  def this(queue: String, namespace: String = "redisqueue") = this("localhost", 6379, queue, namespace)

  def queueKey = makeKey(None)
  def failedKey = makeKey(Some("failed"))
  def activeKey = makeKey(Some("active"))

  private def makeKey(key: Option[String]) = key match {
    case None => List(namespace,queue).mkString("::")
    case Some(k) => List(namespace,queue,k).mkString("::")
  }

  def getClient: RedisClient = client

  private def clearQueue(queueKey: String) = client.del(queueKey)
  def clearInput = clearQueue(queueKey)
  def clearFailed = clearQueue(failedKey)

  private def length(queueKey: String) = client.llen(queueKey) match {
    case Some(n) => n
    case _ => 0
  }
  def lengthInput = length(queueKey)
  def lengthFailed = length(failedKey)

  def deleteAll = {
    //TODO: check for consumers before deleting queue
    List(queueKey, failedKey).map { k => clearQueue(k) }
  }

  //TODO: write function to wrap up talking with client, wraps exceptions
  def put(message: String) = client.lpush(queueKey, message) match {
    case Some(num) => (true,num)
    case _ => (false,0)
  }

  def requeueFailed: Int = lengthFailed match {
    case 0 => 0
    case _ => client.rpoplpush(failedKey,queueKey) match {
      case None => 0
      case Some(_) => 1 + requeueFailed
    }
  }

}
