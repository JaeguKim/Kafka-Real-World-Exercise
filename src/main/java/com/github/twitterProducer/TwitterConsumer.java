package com.github.twitterProducer;

import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

public class TwitterConsumer {

    final String groupId;
    final String bootstrapServers;
    final int totalDuration = 1000;

    public TwitterConsumer(String groupId, String bootstrapServers) {
        this.groupId = groupId;
        this.bootstrapServers = bootstrapServers;
    }

    public List<String> getMessages(String topic) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        consumer.subscribe(Arrays.asList(topic));
        List<String> messages = new ArrayList<>();
        int timeOffset = 0;
        while (timeOffset <= totalDuration) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10)); // new in kafka 2.0.0
            for (ConsumerRecord<String, String> record : records) {
                messages.add(record.value());
            }
            timeOffset += 10;
        }
        return messages;
    }

    public void consume(String topic) {
        Logger logger = LoggerFactory.getLogger(TwitterConsumer.class.getName());
        List<String> messages = getMessages(topic);
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                logger.info(indexResponse.getId());
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    logger.info("document is created!");
                }
            }

            @Override
            public void onFailure(Exception e) {
                logger.error(e.toString());
            }
        };

        logger.info("message size : " + messages.size());
        for (int i=0; i < messages.size(); i++){
            IndexRequest request = new IndexRequest("twitterposts","doc").source(messages.get(i), XContentType.JSON);
            logger.info("indexing : " + messages.get(i));
            client.indexAsync(request, RequestOptions.DEFAULT, listener);
        }

        synchronized (this) {
            try {
                wait(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
