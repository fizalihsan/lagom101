
# Commands

* To start the services: `mvn lagom:runAll`
* GET: `curl http://localhost:9000/api/hello/World`

# Technology Stack

* Lagom Framework
    * Backend store: Cassandra
    * Message Broker: Kafka
    
# Event Processing Flow 

![Event Processing Flow](/images/LagomEventProcessing.png)