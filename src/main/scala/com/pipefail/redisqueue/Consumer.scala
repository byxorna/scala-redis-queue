package com.pipefail.redisqueue

import java.util.UUID

//TODO implement this
//def doRedisTask[Option[T]](func: => Option[T]): Either[Failure, Option[T]] = {
//  try {
//    block match {
//      case Some(t) => Right(Some(t))
//      case None => Left(Failure("Unable to get value"))
//    }
//  } catch {
//    case ex => Left(Failure(ex.message,ex))
//  }
//}

class Consumer(queue: Queue) {

  val id = UUID.randomUUID.toString

  def activeKey = queue.makeKey(Some(id))
  def heartbeatKey = queue.makeKey(List(id,"heartbeat"))

  // tries to grab a message from the input queue
  def get: Option[Message] = hasUnacked match {
    case Left(f) => None
    case Right(o) => if (o) {
      None
      //Left(Failure("Cannot get message while there are unacked messages"))
    } else {
        queue.getClient.rpoplpush(queue.inputKey, activeKey) match {
        case Some(s) => Message.parse(s)
        case None => None
      }
    }
  }

  // we have handled this message; get rid of it!
  def ack(message: Message): Boolean = queue.getClient.rpop(activeKey) match {
    case None => false
    case Some(_) => true
  }

  def reject(message: Message): Either[Failure, Boolean] = queue.getClient.rpoplpush(activeKey, queue.failedKey) match {
    case None => Left(Failure("Unable to reject message"))
    // TODO: add logging for message we failed
    case Some(s) => Right(true)
  }

  def hasUnacked: Either[Failure, Boolean] = queue.getClient.llen(activeKey) match {
    case None => Left(Failure("Unable to determine if consumer has any unacked messages"))
    case Some(n) => Right(n != 0)
  }

  //def getBlocking(timeout: Int): Option[Message] = TODO

}
