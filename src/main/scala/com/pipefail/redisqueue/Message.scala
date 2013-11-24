package com.pipefail.redisqueue

import argonaut._, Argonaut._

sealed trait JsonTrait {
  def toJson: Json
}

case class Message(payload: String, timestamp: Long, error: Option[Error] = None) extends JsonTrait {

  // this is super kludgy, but I dont know a better way to give consumers of
  // com.pipefail.redisqueue.Message access to .asJson without needing to import
  // the argonaut library. FIXME
  override def toJson: Json = this.asJson

}

object Message {

  //argonaut defines asJson and decodeOption[T]

  def parse(json: String): Option[Message] = json.decodeOption[Message]

  implicit def jsonCodec: CodecJson[Message] =
    casecodec3(Message.apply, Message.unapply)("payload", "timestamp", "error")

}

case class Error(message: String, lastFailure: Long, failures: Int) extends JsonTrait {

  override def toJson: Json = this.asJson

}

object Error {

  implicit def jsonCodec: CodecJson[Error] =
    casecodec3(Error.apply, Error.unapply)("message", "lastFailure", "failures")

}
