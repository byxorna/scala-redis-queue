package com.pipefail.redisqueue

object Consumer {

  // TODO: we need to create a separate working list for each consumer, to ensure
  // consumers are only failing/acking their own messages, not someone elses

}

class Consumer(queue: Queue) {

  // tries to grab a message from the input queue
  def get: Option[Message] = queue.getClient.rpoplpush(queue.inputKey, queue.activeKey) match {
    case Some(s) => Message.parse(s)
    case None => None
  }

  // we have handled this message; get rid of it!
  def ack(message: Message): Boolean = queue.getClient.rpop(queue.activeKey) match {
    case None => false
    // dont parse response, just assume Some(_) is the message we wanted to pop
    case Some(_) => true
  }

  def reject(message: Message): Boolean = queue.getClient.rpoplpush(queue.activeKey, queue.failedKey) match {
    case None => false
    // TODO: add logging for message we failed
    case Some(s) => true
  }

  def hasUnacked: Either[Failure, Boolean] = queue.getClient.llen(queue.activeKey) match {
    case None => Left(Failure("Unable to determine if consumer has any unacked messages"))
    case Some(n) => Right(n != 0)
  }

  //def getBlocking(timeout: Int): Option[Message] = TODO

}
