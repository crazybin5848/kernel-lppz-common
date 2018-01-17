./kafka_2.11-0.8.2.1/bin/kafka-topics.sh --zookeeper hanode3:2181,hanode4:2181,hanode5:2181,hanode6:2181,hanode7:2181 --create --topic hbaseidxsyncFlush --partitions 1 --replication-factor 1
./kafka_2.11-0.8.2.1/bin/kafka-topics.sh --zookeeper hanode3:2181,hanode4:2181,hanode5:2181,hanode6:2181,hanode7:2181 --create --topic hbaseidxsyncMap --partitions 1 --replication-factor 1
./kafka_2.11-0.8.2.1/bin/kafka-topics.sh --zookeeper hanode3:2181,hanode4:2181,hanode5:2181,hanode6:2181,hanode7:2181 --create --topic hbaseidxsyncZK --partitions 1 --replication-factor 1
./kafka_2.11-0.8.2.1/bin/kafka-topics.sh --zookeeper hanode3:2181,hanode4:2181,hanode5:2181,hanode6:2181,hanode7:2181 --create --topic idxHBaseKafkaEsMasterSync --partitions 1 --replication-factor 1
./kafka_2.11-0.8.2.1/bin/kafka-topics.sh --zookeeper hanode3:2181,hanode4:2181,hanode5:2181,hanode6:2181,hanode7:2181 --create --topic idxHBaseKafkaEsRegionServerSync --partitions 800 --replication-factor 1
./kafka_2.11-0.8.2.1/bin/kafka-topics.sh --zookeeper hanode3:2181,hanode4:2181,hanode5:2181,hanode6:2181,hanode7:2181 --create --topic kafkahbaselocked --partitions 1 --replication-factor 1


./kafka_2.11-0.8.2.1/bin/kafka-topics.sh --zookeeper 10.8.102.205:2181,10.8.102.211:2181,10.8.102.212:2181 --alter --topic dubbolog --partitions 16


./kafka_2.11-0.8.2.1/bin/kafka-topics.sh --zookeeper 10.8.102.205:2181,10.8.102.211:2181,10.8.102.212:2181 --create --topic dalMycatSendaryTopic --partitions 64 --replication-factor 1