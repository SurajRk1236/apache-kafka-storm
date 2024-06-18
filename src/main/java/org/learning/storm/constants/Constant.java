package org.learning.storm.constants;

import org.learning.storm.dtos.MessageDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constant {
    public final static String MESSAGE = "message";
    public final static String COUNT = "count";
    public final static String STORM_KAFKA_SPOUT = "StormKafkaSpout";
    public final static String STORM_KAFKA_BOLT = "StormKafkaBolt";
    public final static String STORM_KAFKA_TOPOLOGY = "StormKafkaTopology";

    public final static String KAFKA_TOPIC_NAME = "apache-storm-kafka-topic";
    public final static String KAFKA_YAML_FILE = "kafka.yaml";
    public final static List<MessageDto> MESSAGE_DATA_FOR_KAFKA = Collections.unmodifiableList(MessageDto.getMessageData());


}
