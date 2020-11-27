package com.github.twitterProducer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TwitterConsumer {

    final String groupId;
    final String bootstrapServers;
    final int totalDuration = 1000;

    public TwitterConsumer(String groupId, String bootstrapServers) {
        this.groupId = groupId;
        this.bootstrapServers = bootstrapServers;
    }

    public List<String> getMessages(String topic) {
        // create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        // subscribe to our topic(s)
        consumer.subscribe(Arrays.asList(topic));
        List<String> messages = new ArrayList<>();
        // poll for new data
        int timeOffset = 0;
        while (timeOffset <= totalDuration) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10)); // new in kafka 2.0.0
            for (ConsumerRecord<String, String> record : records) {
                String message = String.format("Key : %s, Value : %s, Partition: %d, Offset : %d",record.key(),record.value(),record.partition(),record.offset());
                //logger.info("Key: " + record.key() + ", Value: " + record.value());
                //logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                messages.add(message);
            }
            timeOffset += 10;
        }
        return messages;
    }

    public void consume(String topic) {
        Logger logger = LoggerFactory.getLogger(TwitterConsumer.class.getName());
    }
}
