package org.learning.storm.bolts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.learning.storm.dtos.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.learning.storm.constants.Constant.COUNT;
import static org.learning.storm.constants.Constant.MESSAGE;


/**
 * this type of bolt actually runs based on load balancing design
 * which bolt has less consumption the spout will emit message to that bolt
 */
public class KafkaShuffleGroupingBolt extends BaseRichBolt {
    private final static String APPEND_MESSAGE = "PROCESSED using shuffle grouping bolt";
    private static final Logger log = LoggerFactory.getLogger(KafkaShuffleGroupingBolt.class);
    private OutputCollector collector;
    private ObjectMapper objectMapper;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        // if u further want to publish to another bolt
        //  outputFieldsDeclarer.declare(new Fields(MESSAGE, COUNT));
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void execute(Tuple tuple) {
        log.info("Executing KafkaShuffleGroupingBolt --------------------- >  :::::::::::::: {}", tuple.getValue(0));

        MessageDto messageDto;
        try {
            messageDto = objectMapper.readValue(tuple.getString(4), MessageDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        messageDto.setData(messageDto.getData() + APPEND_MESSAGE);
        messageDto.setCounter(messageDto.getCounter() + 100);
        log.info("NEW MESSAGE RETRIEVED FROM BOLT IS :: {}", messageDto);
        collector.emit(new Values(messageDto.getCounter(), messageDto.getData()));
    }
}
