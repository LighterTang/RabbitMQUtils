package com.mq.mqutils.tutorial.pubsub;

import com.rabbitmq.client.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Title: DirectLogRecv1
 * @Package: com.mq.mqutils.tutorial.pubsub
 * @Description: TODO（添加描述）
 * @Author: tangquanbin
 * @Data: 2019/4/19 13:40
 * @Version: V1.0
 */
public class DirectLogRecv2 {

    private static final String EXCHANGE_NAME = "direct_logs";
    private static final List<String> routingKeys = Arrays.asList("error");

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.19.254");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        for (String severity : routingKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
