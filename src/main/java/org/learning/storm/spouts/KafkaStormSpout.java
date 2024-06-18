package org.learning.storm.spouts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.kafka.spout.FirstPollOffsetStrategy;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.learning.storm.dtos.KafkaConfig;
import org.learning.storm.dtos.MessageDto;
import org.apache.storm.tuple.Values;


import java.util.Map;
import java.util.Properties;

import static org.learning.storm.constants.Constant.KAFKA_TOPIC_NAME;

public class KafkaStormSpout extends BaseRichSpout {

    private KafkaSpout<String, String> kafkaSpout;
    private ObjectMapper objectMapper;
    private SpoutOutputCollector collector;


    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        // Configure Kafka consumer properties
        KafkaConfig kafkaConfig = (KafkaConfig) map;
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers()); // Replace with your Kafka brokers
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getConsumer().getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getConsumer().getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConfig.getConsumer().getValueDeserializer());

        // Kafka Spout configuration
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig.builder(kafkaConfig.getBootstrapServers(), KAFKA_TOPIC_NAME)
                .setFirstPollOffsetStrategy(FirstPollOffsetStrategy.UNCOMMITTED_EARLIEST)
                .setProp(props)
                .build();

        // Create Kafka Spout
        this.kafkaSpout = new KafkaSpout<>(kafkaSpoutConfig);
        this.objectMapper = new ObjectMapper();
        this.collector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
       //need to write custom logic for consuming the data
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
