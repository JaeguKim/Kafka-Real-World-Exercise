package com.github.twitterProducer;

public class Test {

    public static void main(String[] args) {
        String consumerKey = "Lhmk9pmGpPnrKasck03irJ31O";
        String consumerSecret = "9cBKkoKTdqDJcf3Tu3UPz6xmfVAc8wLOy3Tzg2Egu3uDMcr7or";
        String token = "927520294188630018-gwoYkQZIWBIpOx2nXspuiTJBAfzd2Xw";
        String tokenSecret = "LJIssvKAHx3EIZumIS2SG531OhjMJlPPkrQN0rKFHiBnB";
        TwitterProducer producer = new TwitterProducer(consumerKey,consumerSecret,token,tokenSecret);
        producer.produce();
    }
}
