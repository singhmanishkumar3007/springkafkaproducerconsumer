# springkafkaproducerconsumer
<i>Spring Boot +kakfa +Producer + Consumer


Assuming that you have jdk 8 installed already let us start with installing and configuring zookeeper on Windows.
</br>
<b><U> Steps to Install zookeper and Kafka : </b> </U>
</br>
</br>
1. 
Download zookeeper from https://zookeeper.apache.org/releases.html. </br>
I have downloaded zookeeper version 3.4.10 as in the kafka lib directory, 
the existing version of zookeeper is 3.4.10.Once downloaded, follow following steps:</br>

Extract it and in my case I have extracted kafka and zookeeper in following directory:
D:\softwares\kafka_2.12-1.0.1 --kafka location D:\softwares\kafka-new\zookeeper-3.4.10 --zookeeper location </br>

Once this is extracted, let us add zookeeper in the environment variables.For this go to Control Panel\All Control Panel Items\System and click on the Advanced System Settings and then Environment Variables and then edit the system variables as below:
zookeeper-windows-config </br>

Also, edit the PATH variable and add new entry as %ZOOKEEPER_HOME%\bin\ for zookeeper. </br>

Rename file D:\softwares\kafka-new\zookeeper-3.4.10\zookeeper-3.4.10\conf\zoo_sample.cfg to zoo.cfg </br>

Now, in the command prompt, enter the command zkserver and the zookeeper is up and running on http://localhost:2181 </br>


<b> Kafka Setup On windows - <b> </br>
</br>

Head over to http://kafka.apache.org/downloads.html and download Scala 2.12. This version has scala and zookepper already included in it.
</br>Follow below steps to set up kafka.</br>

Unzip the downloaded binary. In my case it is - D:\softwares\kafka_2.12-1.0.1 </br>

Go to folder D:\softwares\kafka_2.12-1.0.1\config and edit server.properties </br>

log.dirs=.\logs </br>

Now open a new terminal at D:\softwares\kafka_2.12-1.0.1. </br>

Execute .\bin\windows\kafka-server-start.bat .\config\server.properties to start Kafka. </br>
Since, we have not made any changes in the default configuration, Kafka should be up and running on http://localhost:9092 </br>



<b>Let us create a topic with a name manish-test.t <b> </br>
</br>

cd D:\softwares\kafka_2.12-1.0.1\bin\windows </br>

kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic manish-test.t </br>

Above command will create a topic named manish-test with single partition and hence with a replication-factor of 1. 
This will be a single node - single broker kafka cluster. </br>

</br>
<b> Command to check messages in kakfa topic- </b></br>

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic manish-test.t --from-beginning </br>

</br>
</br>

<b>Explanation Of Producer Code : </b> </br>
</br>
The creation of the KafkaTemplate and Sender is handled in the SenderConfig class. </br>
The class is annotated with @Configuration which indicates that the class can be used by the Spring IoC container 
as a source of bean definitions.</br>

In order to be able to use the Spring Kafka template, we need to configure a ProducerFactory 
and provide it in the template’s constructor.</br>

The producer factory needs to be set with a number of mandatory properties 
amongst which the 'BOOTSTRAP_SERVERS_CONFIG' property that specifies a list of host:port pairs 
used for establishing the initial connections to the Kafka cluster. </br>
Note that this value is configurable as it is fetched from the application.yml configuration file.</br>

A message in Kafka is a key-value pair with a small amount of associated metadata. </br>
As Kafka stores and transports Byte arrays, we need to specify the format from which the key and value will be serialized.</br>
In this example we are sending a String as payload, as such we specify the StringSerializer class which will take care of the
needed transformation.</br>

<i><b>
ProducerConfig.BOOTSTRAP_SERVERS_CONFIG specifies a list of host/port pairs to use for 
establishing the initial connection to the Kafka cluster. </br>
The client will make use of all servers irrespective of which servers are specified here for 
bootstrapping/this list only impacts the initial hosts used to discover the full set of servers. 
This list should be in the form host1:port1,host2:port2,.... </br>
Since these servers are just used for the initial connection to 
discover the full cluster membership (which may change dynamically), this list need not contain the 
full set of servers (you may want more than one, though, in case a server is down).</br>
ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG specifies the serializer class for key that implements 
the org.apache.kafka.common.serialization.Serializer interface.</br>
ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG specifies the serializer class for value 
that implements the org.apache.kafka.common.serialization.Serializer interface.</br></i></b>
</br>


<b> Explanation Of Consumer Code <b> </br>

</br>

Like with any messaging-based application, you need to create a receiver( class kafkaReceiver in our case) 
that will handle the published messages. </br>
The Receiver is nothing more than a simple POJO that defines a method for receiving messages.</br>

In the below example we named the method receive(), but you can name it anything you like.</br>

The @KafkaListener annotation creates a ConcurrentMessageListenerContainer message listener container behind the scenes 
for each annotated method. </br>
In order to do so, a factory bean with name kafkaListenerContainerFactory is expected that
we will configure in the next section. </br>

Using the topics element, we specify the topics for this listener. </br>
The name of the topic is injected from the application.yml properties file.</br>

<b> Explanation Of Consumer Configuration Code : <b> </br>

The creation and configuration of the different Spring Beans needed for the KafkaReceiver POJO are grouped 
in the ReceiverConfig class. Similar to the SenderConfig it is annotated with @Configuration. </br>

Note the @EnableKafka annotation which enables the detection of the @KafkaListener annotation
that was used on the previous Receiver class.</br>

The kafkaListenerContainerFactory() is used by the @KafkaListener annotation from the Receiver 
in order to configure a MessageListenerContainer. In order to create it, a ConsumerFactory 
and accompanying configuration Map is needed. </br>

In this example, a number of mandatory properties are set amongst which the initial connection and deserializer parameters.</br>

We also specify a 'GROUP_ID_CONFIG' which allows to identify the group this consumer belongs to. </br>
Messages will effectively be load balanced over consumer instances that have the same group id.</br>

On top of that we also set 'AUTO_OFFSET_RESET_CONFIG' to "earliest". </br>
This ensures that our consumer reads from the beginning of the topic even if some messages were already 
sent before it was able to startup.</br>

<i><b>ConsumerConfig.AUTO_OFFSET_RESET_CONFIG specifies what to do when there is no initial offset in Kafka or 
if the current offset does not exist any more on the server (e.g. because that data has been deleted): </br>
earliest: automatically reset the offset to the earliest offset
latest: automatically reset the offset to the latest offset
none: throw exception to the consumer if no previous offset is found for the consumer’s group
anything else: throw exception to the consumer.
</br>
Consumers label themselves with a consumer group name, and each record published to a 
topic is delivered to one consumer instance within each subscribing consumer group.</br>
Consumer instances can be in separate processes or on separate machines.</br>

If all the consumer instances have the same consumer group, then the records will effectively be load balanced over the 
consumer instances. If all the consumer instances have different consumer groups, then each record will 
be broadcasted to all the consumer processes.</br> </i></b> 
</br>
</br>
