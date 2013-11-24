package com.pipefail.redisqueue

import argonaut._, Argonaut._

case class Message(payload: String, timestamp: Long, error: Option[Error])

object Message {

  //argonaut defines asJson and decodeOption[T]

  def parse(json: String): Option[Message] = json.decodeOption[Message]

  implicit def jsonCodec: CodecJson[Message] =
    casecodec3(Message.apply, Message.unapply)("payload", "timestamp", "error")

}

case class Error(message: String, lastFailure: Long, failures: Int)

object Error {

  implicit def jsonCodec: CodecJson[Error] =
    casecodec3(Error.apply, Error.unapply)("message", "lastFailure", "failures")

}
