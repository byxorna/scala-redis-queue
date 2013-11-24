package com.pipefail.redisqueue

case class Failure(message: String, exception: Option[Exception] = None)
