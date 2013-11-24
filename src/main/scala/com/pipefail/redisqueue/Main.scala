package com.pipefail.redisqueue

import com.pipefail.redisqueue._

object Main {
  def main(args: Array[String]){
    val queueName = "testing"
    val q = new Queue(queueName)

    println("input queue length: " + q.inputLength)
    println("failed queue length: " + q.failedLength)

    for( x <- 1 until 4 ) {
      println("Adding message " + x)
      q.put(Message("message " + x,123))
      println("input queue length: " + q.inputLength)
    }

    println("Creating consumer for " + queueName)
    val c = new Consumer(q)
    for( x <- 1 until 4 ) {
      println("Attempting to consume message...")
      var m = c.get
      m match {
        case None => println("got " + m.toString)
        case Some(m) => {
          println("got a message: " + m)
          c.ack(m)
        }
      }
      println("input queue length: " + q.inputLength)
      println("failed queue length: " + q.failedLength)
    }
  }
}
