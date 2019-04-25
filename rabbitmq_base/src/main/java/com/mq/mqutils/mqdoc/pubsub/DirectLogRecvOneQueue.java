package com.mq.mqutils.mqdoc.pubsub;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Title: DirectLogRecv1
 * @Package: com.mq.mqutils.mqdoc.pubsub
 * @Description: TODO（添加描述）
 * @Author: tangquanbin
 * @Data: 2019/4/19 13:40
 * @Version: V1.0
 */
public class DirectLogRecvOneQueue {
    private static final String QUEUE_NAME = "direct_queue";
    private static final String EXCHANGE_NAME = "direct_logs2_send2_exchange";
    private static final String ROUTINGKEY = "error";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.19.254");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        AMQP.Queue.DeclareOk response = channel.queueDeclarePassive(QUEUE_NAME);
        //返回队列中处于Ready状态的消息数
        int messageCount = response.getMessageCount();
        System.out.println("messageCount===="+messageCount);
        //返回队列拥有的消费者数量
        int consumerCount = response.getConsumerCount();
        System.out.println("consumerCount===="+consumerCount);

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,true);
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTINGKEY);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });

    }
}
