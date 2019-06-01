package com.mq.mqutils.mqdoc.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @Title: TopicLogSend
 * @Package: com.mq.mqutils.mqdoc.pubsub
 * @Description: TODO（添加描述）
 * @Author: monkjavaer
 * @Data: 2019/4/21 0021 22:49
 * @Version: V1.0
 */
public class TopicLogSend {
    private static final String EXCHANGE_NAME = "topic_logs";
    private static final List<String> routingKeys = Arrays.asList("corn.error", "corn.info","auth.info","auth.error","auth.test.info");

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.4");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            for (int i = 0; i < 10; i++) {
                String routingKey = routingKeys.get(new Random().nextInt(5));
                String message = routingKey + "-" + i;

                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }


        }
    }

}
