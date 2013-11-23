package org.pipefail.redisqueue

import com.redis._

object Main {
  def main(args: Array[String]){
    println("butts")
    val client = new RedisClient("localhost", 6379)
    client.set("bugger","testing")
    val a = client.get("bugger")
    println(a)
  }
}
