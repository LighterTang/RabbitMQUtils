package com.mq.mqutils.mqdoc.pubsub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @Title: DirectLogSend
 * @Package: com.mq.mqutils.mqdoc.pubsub
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

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);

            //ReturnListener 处理不可路由的消息
            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int replyCode,
                                         String replyText,
                                         String exchange,
                                         String routingKey,
                                         AMQP.BasicProperties properties,
                                         byte[] body)
                        throws IOException {
                    System.out.println("return message :" + new String(body, "UTF-8"));
                }


            });

            String routingKey = routingKeys.get(new Random().nextInt(3));

            String message = "DirectLogSend Class";

            channel.basicPublish(EXCHANGE_NAME, routingKey, true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));

            System.out.println(" Sent '" + routingKey + "':'" + message + "'");

            Thread.sleep(10000);

        }
    }


}
