scala-redis-queue
=================

A simple queue in Scala backed by Redis

This is written to provide a simple interface for https://github.com/byxorna/frosting to enqueue/dequeue work.

## Overview

* namespace::queue - Input queue for 'queue'
* namespace::queue::failed - Messages that were rejected are here
* namespace::queue::consumers - Set of consumer ids
* namespace::queue::${ID} - Active messsages for consumer id:$ID
* namespace::queue::${ID}::heartbeat - heartbeat message that a consumer is active

## TODO

https://github.com/debasishg/scala-redis supports RPOPLPUSH, but not authentication to redis. https://github.com/top10/scala-redis-client supports the opposite. I am using scala-redis for now.

* register consumers on the queue so we can inspect active consumers
* Implement counters to track actions taken by the library
* Implement logging functionality


## Credits

* http://redis.io/commands/rpoplpush
* http://big-elephants.com/2013-09/building-a-message-queue-using-redis-in-go/
* https://github.com/debasishg/scala-redis and https://github.com/top10/scala-redis-client
* The internet
