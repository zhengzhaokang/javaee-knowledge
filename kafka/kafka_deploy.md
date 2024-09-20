1.zookeeper集群搭建   单机版本集群部署   多机版本同理

1.1  zk官网下载 apache-zookeeper-3.5.9-bin.tar.gz   注意带是带bin的压缩包

1.2在/opt/zookeeper目录下，创建三个目录，为安装三个zk服务做准备

1.3 将 压缩包解压到2187，2188，2189目录下, 并在每个目录下创建 zkdata zklogs目录，

并在每个zookeeper的 zkdata 目录下创建一个 myid 文件，内容分别是1、2、3 。这个文件就是记录每个服务器的ID。 为zk的集群配置做准备.

参考命令:

echo 1 >/opt/zookeeper/2187/zkdata/data/myid echo 2 >/opt/zookeeper/2188/zkdata/data/myid echo 3 >/opt/zookeeper/2189/zkdata/data/myid

1.4 ###重要部分，修改zk配置

进入conf目录下，cp zoo_sample.cfg  zoo.cfg  创建zoo.cfg。

参考配置如下:

\# The number of milliseconds of each tick tickTime=3000 initLimit=10 syncLimit=5 dataDir=/opt/zookeeper/2187/zkdata dataLogDir=/opt/zookeeper/2187/zklogs clientPort=2187 maxClientCnxns=100

server.1=xx.xx.188.40:2881:3881 server.2=xx.xx.188.40:2882:3882 server.3=xx.xx.188.40:2883:3883

主要修改点是：dataDir，dataLogDir，clientPort 和集群信息配置。2188和2189的配置文件，参考2187文件，只需修改对应的目录和端口号即可。

1.5 配置修改完之后，就是启动zk服务了.

到bin 目录下执行zkServer.sh start 命令启动 其他二个目录同理执行. 

1.6 检验是否执行成功 zkServer.sh status.  也可通过jps看zk启动的情况。

可参考

2.kafka集群搭建   单机版集群部署   多机同理

2.1  kafka集群可依赖已搭建的zk集群

官网下载压缩包 并解压 tar -zxvf    kafka_2.12-2.8.0.tgz 至   kafka_2.12-2.8.0

2.2  创建 /opt/kafka_2.12-2.8.0/kafka/logs1, /opt/kafka_2.12-2.8.0/kafka/logs2, /opt/kafka_2.12-2.8.0/kafka/logs3 为修改配置文件做准备. 其中启动后生成的  meta.properties 至关重要，包含了集群的cluster.id，version，broker.id。如果要切换zk集群，对应的cluster,id就会变化。

2.3  切换到config目录下， 基于server.properties文件，复制 server1.properties ,  server2.properties,   server3.properties. 并修改其中配置。

配置文件可参考入参:

[broker.id](http://broker.id/)=11 listeners=[PLAINTEXT://0.0.0.0:9097](plaintext://0.0.0.0:9097) [inter.broker.listener.name](http://inter.broker.listener.name/)=PLAINTEXT advertised.listeners=[PLAINTEXT://](plaintext://10.64.188.40:9097)xx.xx[.188.40:9097](plaintext://10.64.188.40:9097) num.network.threads=3 [num.io](http://num.io/).threads=8 socket.send.buffer.bytes=102400 socket.request.max.bytes=104857600 log.dirs=/opt/kafka_2.12-2.8.0/kafka/logs1 num.partitions=1 num.recovery.threads.per.data.dir=1 offsets.topic.replication.factor=3 transaction.state.log.replication.factor=3 transaction.state.log.min.isr=3 log.flush.interval.messages=10000 [log.flush.interval.ms](http://log.flush.interval.ms/)=1000 log.retention.hours=168 log.segment.bytes=1073741824 [log.retention.check.interval.ms](http://log.retention.check.interval.ms/)=300000 zookeeper.connect=xx.xx.188.40:2187,xx.xx.188.40:2188,xx.xx.188.40:2189 [zookeeper.connection.timeout.ms](http://zookeeper.connection.timeout.ms/)=18000 [group.initial.rebalance.delay.ms](http://group.initial.rebalance.delay.ms/)=3000

其中 [broker.id](http://broker.id/), listeners, advertised.listeners，log.dirs，zookeeper.connect,offsets.topic.replication.factor,transaction.state.log.replication.factor，transaction.state.log.min.isr

可根据具体情况进行修改。同理可配置其他二份文件，三份配置文件修改完成后，可准备启动。其中通过zookeeper.connect关联多个kafka服务为同一个集群。

2.4  切换到如下目录 /opt/kafka_2.12-2.8.0

\# 启动kafka ./bin/kafka-server-start.sh -daemon ./config/server1.properties

./bin/kafka-server-start.sh -daemon ./config/server2.properties

./bin/kafka-server-start.sh -daemon ./config/server3.properties

去掉-daemon可看具体的启动日志，若启动失败，可看具体的失败原因。

可通过jps命令检查kafka启动情况.

2.5 启动成功后，进行验证

\#创建kafka-topic ./bin/kafka-topics.sh --create --topic hello-zk1 --partitions 3 --replication-factor 3 --bootstrap-server localhost:9098

一个终端 #生产者 bin/kafka-console-producer.sh --broker-list localhost:9099 --topic hello-zk1 另一个终端 #消费者 bin/kafka-console-consumer.sh --bootstrap-server localhost:9097 --topic hello-zk1

也可用代码进行验证

FYI  

```
ConsumerDemo.java
public static void main(String[] args) {
    try {
        Properties properties = new Properties();
        // 配置集群节点信息
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"xx.xx.188.40:9097,xx.xx.188.40:9098,xx.xx.188.40:9099");

        // 消费分组名
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "demo_group");
        // 序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);
        // 消费者订阅主题
        consumer.subscribe(Arrays.asList("hello-zk1"));

        while (true) {
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String,String> record:records) {
                System.out.printf("收到消息：partition=%d, offset=%d, key=%s, value=%s%n",record.partition(),
                        record.offset(),record.key(),record.value());
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}
ProducerDemo.java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    Properties properties = new Properties();
    //配置集群节点信息
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"xx.xx.188.40:9097,xx.xx.188.40:9098,xx.xx.188.40:9099");
    //配置序列化
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

    Producer<String, String> producer = new KafkaProducer<>(properties);

    //topic 名称是demo_topic 10/64
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>("hello-zk1", "123");
    RecordMetadata recordMetadata = producer.send(producerRecord).get();
    System.out.println(recordMetadata.topic());
    System.out.println(recordMetadata.partition());
    System.out.println(recordMetadata.offset());
}

以上是未带安全认证的kafka集群部署的流程, FYI。
```