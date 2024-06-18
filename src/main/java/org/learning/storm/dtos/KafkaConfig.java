package org.learning.storm.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConfig {
    String bootstrapServers;
    ProducerConfig producer;
    ConsumerConfig consumer;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ProducerConfig {
        String keySerializer;
        String valueSerializer;
        String acks;
        Integer retries;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ConsumerConfig {
        String groupId;
        String keyDeserializer;
        String valueDeserializer;
        String autoOffsetReset;
        boolean enableAutoCommit;
    }

}
