scala-redis-queue
=================

A simple queue in Scala backed by Redis

This is written to provide a simple interface for https://github.com/byxorna/frosting to enqueue/dequeue work.

## TODO

https://github.com/debasishg/scala-redis supports RPOPLPUSH, but not authentication to redis. https://github.com/top10/scala-redis-client supports the opposite. I am using scala-redis for now.

* register consumers on the queue so we can inspect active consumers
* Make unique active queue for a consumer, so only one active job is allowed at a time (and other consumers cant clobber your message)
* Implement counters to track actions taken by the library
* Implement logging functionality


## Credits

* http://redis.io/commands/rpoplpush
* http://big-elephants.com/2013-09/building-a-message-queue-using-redis-in-go/
* https://github.com/debasishg/scala-redis and https://github.com/top10/scala-redis-client
* The internet
