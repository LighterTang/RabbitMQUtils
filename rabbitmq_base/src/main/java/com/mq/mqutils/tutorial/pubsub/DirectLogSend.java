package com.mq.mqutils.tutorial.pubsub;

import com.rabbitmq.client.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @Title: DirectLogSend
 * @Package: com.mq.mqutils.tutorial.pubsub
 * @Description: 直接路由发送
 * @Author: tangquanbin
 * @Data: 2019/4/19 13:32
 * @Version: V1.0
 */
public class DirectLogSend {
    private static final String EXCHANGE_NAME = "direct_logs2";
    private static final List<String> routingKeys = Arrays.asList("info", "error", "debug");

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.19.254");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,true);

            for (int i = 0; i < 10; i++) {
                String routingKey = routingKeys.get(new Random().nextInt(3));
                String message = routingKey + "-" + i;

                channel.basicPublish(EXCHANGE_NAME, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));

                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }

        }
    }


}
