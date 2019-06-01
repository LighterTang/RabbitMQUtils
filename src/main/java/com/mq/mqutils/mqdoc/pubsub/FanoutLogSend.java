package com.mq.mqutils.mqdoc.pubsub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Title: FanoutLogSend
 * @Package: com.mq.mqutils.mqdoc.pubsub
 * @Description: 广播发送消息
 * @Author: monkjavaer
 * @Data: 2019/4/12 17:50
 * @Version: V1.0
 */
public class FanoutLogSend {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.19.254");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            for (int i = 0;i<10;i++) {
                String message = "log info :"+i;

                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));

                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }
}
