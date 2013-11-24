package com.pipefail.redisqueue

import com.redis._
import com.pipefail.redisqueue._

class Queue(client: RedisClient, queue: String, namespace: String) {

  def this(host: String, port: Int, queue: String, namespace: String = "redisqueue") = this(new RedisClient(host, port), queue, namespace)

  // set default values for connection
  def this(queue: String, namespace: String = "redisqueue") = this("localhost", 6379, queue, namespace)

  def inputKey = makeKey(None)
  def failedKey = makeKey(Some("failed"))
  def activeKey = makeKey(Some("active"))

  private def makeKey(key: Option[String]) = key match {
    case None => List(namespace,queue).mkString("::")
    case Some(k) => List(namespace,queue,k).mkString("::")
  }

  def getClient: RedisClient = client

  // TODO: check if sismember, change to boolean response
  //def removeActive(message: Message) = client.srem(message.toJson, activeKey) match {
  //  case Some(n) => n
  //  case _ => 0
  //}

  private def clearQueue(key: String) = client.del(key)
  def clearInput = clearQueue(inputKey)
  def clearFailed = clearQueue(failedKey)

  private def cardinality(key: String) = client.scard(key) match {
    case Some(n) => n
    case _ => 0
  }
  private def length(key: String) = client.llen(key) match {
    case Some(n) => n
    case _ => 0
  }
  def inputLength = length(inputKey)
  def failedLength= length(failedKey)
  def activeLength= length(activeKey)

  def deleteAll = {
    //TODO: check for consumers before deleting queue
    List(inputKey, failedKey).map { k => clearQueue(k) }
  }

  //TODO: write function to wrap up talking with client, wraps exceptions
  def put(message: Message) = client.lpush(inputKey, message.toJson) match {
    case Some(num) => (true,num)
    case _ => (false,0)
  }

  def requeueFailed: Int = failedLength match {
    case 0 => 0
    case _ => client.rpoplpush(failedKey,inputKey) match {
      case None => 0
      case Some(_) => 1 + requeueFailed
    }
  }

}
