# Distributed Ehcache 3.x using RabbitMQ (Incomplete)

Sample project for making EhCache 3.x distributed using RabbitMQ (without using Terracota server).
The idea is to broadcast cache related event using RabbitMQ `fanout` exchange. All the running applications which has subscribed to the exchange will receive cache object containing sufficient details to update its local version of cache so that all cache change is reflected in real-time.
## Getting Started

Clone the project using Git command line tool:

```
git clone git@github.com:bhagwat/amqp-ehcache3.git
cd amqp-ehcache3
```

The repository contains gradle wrapper files so you don't need to to install gradlew on your machine.

### Prerequisites

Make sure you have Java 8 installed on your machine. 

```
Bhagwats-MacBook-Pro:amqp-ehcache3 bhagwat$ java -version
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)
```

Also make sure that RabbitMQ is up and running with its default configuration. Admin dashboard is accessible from http://localhost:15672 with username `guest` and passsword `guest`. 


### Running the application

Use `gradle` wrapper to run the application:
 
```
./gradlew bootRun
```

Sample output:

```
2017-08-30 18:14:37.409  INFO 25932 --- [           main] com.ttn.amqp.ehcache3.Runner             : Sending message...
2017-08-30 18:14:37.410  INFO 25932 --- [           main] com.ttn.amqp.ehcache3.Runner             : Putting message: 1
2017-08-30 18:14:37.419  INFO 25932 --- [           main] com.ttn.amqp.ehcache3.Runner             : Putting message: 2
2017-08-30 18:14:37.420  INFO 25932 --- [           main] com.ttn.amqp.ehcache3.Runner             : Removing message: 1
2017-08-30 18:14:37.421  INFO 25932 --- [           main] com.ttn.amqp.ehcache3.Runner             : Sleeping
2017-08-30 18:14:37.482  INFO 25932 --- [    container-1] com.ttn.amqp.ehcache3.CacheListener      : Received byte[] bytes <>
2017-08-30 18:14:37.483  INFO 25932 --- [    container-1] com.ttn.amqp.ehcache3.CacheListener      : Received byte[] bytes <>
2017-08-30 18:14:37.485  INFO 25932 --- [    container-1] com.ttn.amqp.ehcache3.CacheListener      : Received byte[] bytes <>```

```


#### NOTE: The work is not complete yet. Its still in development phase.