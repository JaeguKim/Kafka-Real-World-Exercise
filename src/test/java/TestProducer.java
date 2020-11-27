import com.github.twitterProducer.TwitterProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestProducer {

    final static String consumerKey = "Lhmk9pmGpPnrKasck03irJ31O";
    final static String consumerSecret = "9cBKkoKTdqDJcf3Tu3UPz6xmfVAc8wLOy3Tzg2Egu3uDMcr7or";
    final static String token = "927520294188630018-gwoYkQZIWBIpOx2nXspuiTJBAfzd2Xw";
    final static String tokenSecret = "LJIssvKAHx3EIZumIS2SG531OhjMJlPPkrQN0rKFHiBnB";
    static TwitterProducer producer;

    @BeforeAll
    static void setup() {
        producer = new TwitterProducer(consumerKey,consumerSecret,token,tokenSecret);
    }

    @Test
    public void testProduce() {
        Assertions.assertTrue(producer.produce().size() > 0);
    }

}
