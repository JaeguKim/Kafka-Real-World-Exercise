import com.github.twitterProducer.TwitterConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestConsumer {

    static TwitterConsumer consumer;

    @BeforeAll
    static void setup() {
        consumer = new TwitterConsumer("Twitter_Consumer_Application","127.0.0.1:9092");
    }

    @Test
    public void testGetMessages() {
        List<String> messages = consumer.getMessages("twitter_topic");
        Assertions.assertTrue(messages.size() >= 0);
    }

    @Test
    public void testConsumeToElasticsearch() {
        consumer.consume("twitter_topic");
    }


}
