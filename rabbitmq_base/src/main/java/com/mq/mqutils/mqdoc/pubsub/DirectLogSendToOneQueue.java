package com.mq.mqutils.mqdoc.pubsub;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Title: DirectLogSend
 * @Package: com.mq.mqutils.mqdoc.pubsub
 * @Description: 直接路由发送
 * @Author: tangquanbin
 * @Data: 2019/4/19 13:32
 * @Version: V1.0
 */
public class DirectLogSendToOneQueue {
    private static final String EXCHANGE_NAME = "direct_logs2_send2_exchange";
    private static final String QUEUE_NAME = "direct_queue";
    private static final String ROUTINGKEY = "error";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.19.254");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,true);
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);

            for (int i = 0; i < 10; i++) {

                String message = ROUTINGKEY + "-" + i;

                channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTINGKEY);

                channel.basicPublish(EXCHANGE_NAME, ROUTINGKEY,true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));

                System.out.println(" [x] Sent '" + ROUTINGKEY + "':'" + message + "'");
            }

        }
    }


}
