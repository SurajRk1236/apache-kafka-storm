package org.learning.storm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.FirstPollOffsetStrategy;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.learning.storm.bolts.KafkaShuffleGroupingBolt;
import org.learning.storm.configs.ApplicationConfig;
import org.learning.storm.dtos.KafkaConfig;

import java.util.Properties;
import java.util.UUID;

import static org.learning.storm.constants.Constant.*;

public class Main {
    public static void main(String[] args) {

        KafkaConfig config = ApplicationConfig.loadConfig(KAFKA_YAML_FILE);
        ObjectMapper objectMapper = new ObjectMapper();

        // Create Kafka producer properties
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getProducer().getKeySerializer());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getProducer().getValueSerializer());
        producerProperties.put(ProducerConfig.ACKS_CONFIG, config.getProducer().getAcks());
        producerProperties.put(ProducerConfig.RETRIES_CONFIG, config.getProducer().getRetries());

        // Create Kafka producer
        Producer<String, String> producer = new KafkaProducer<>(producerProperties);

        // Push a few messages to the Kafka topic
        MESSAGE_DATA_FOR_KAFKA.forEach(
                messageDto -> {
                    ProducerRecord<String, String> record;
                    try {
                        record = new ProducerRecord<>(KAFKA_TOPIC_NAME, UUID.randomUUID().toString(), objectMapper.writeValueAsString(messageDto));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    producer.send(record, (metadata, exception) -> {
                        if (exception != null) {
                            System.err.println("Error sending message: " + exception.getMessage());

                        } else {

                            System.out.println("Message sent to topic: " + metadata.topic() + " partition: " + metadata.partition() + " offset: " + metadata.offset());
                            throw new RuntimeException("done");
                        }
                    });
                }
        );
        // Close the producer
        producer.close();


        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers()); // Replace with your Kafka brokers
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, config.getConsumer().getGroupId());
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getConsumer().getKeyDeserializer());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getConsumer().getValueDeserializer());

        // Kafka Spout configuration
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig.builder(config.getBootstrapServers(), KAFKA_TOPIC_NAME)
                .setFirstPollOffsetStrategy(FirstPollOffsetStrategy.UNCOMMITTED_EARLIEST)
                .setProp(consumerProperties)
                .build();

        LocalCluster cluster = new LocalCluster();
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(STORM_KAFKA_SPOUT, new KafkaSpout<>(kafkaSpoutConfig));
        builder.setBolt(STORM_KAFKA_BOLT, new KafkaShuffleGroupingBolt(),3).shuffleGrouping(STORM_KAFKA_SPOUT);

        Config conf = new Config();
        conf.setDebug(true);

        cluster.submitTopology(STORM_KAFKA_TOPOLOGY, conf, builder.createTopology());
    }
}