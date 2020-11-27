import com.github.twitterProducer.TwitterProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

public class TestProducer {

    static String consumerKey;
    static String consumerSecret;
    static String token;
    static String tokenSecret;
    static TwitterProducer producer;

    @BeforeAll
    static void setup() throws IOException {
        Properties properties = new Properties();
        properties.load(TestProducer.class.getResourceAsStream("/twitterCredentials.properties"));
        consumerKey = properties.getProperty("consumerKey");
        consumerSecret = properties.getProperty("consumerSecret");
        token = properties.getProperty("token");
        tokenSecret = properties.getProperty("tokenSecret");
        producer = new TwitterProducer(consumerKey,consumerSecret,token,tokenSecret);
    }

    @Test
    public void testGenerateData() {
        Assertions.assertTrue(producer.generateData().size() > 0);
    }

}
