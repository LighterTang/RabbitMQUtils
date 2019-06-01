package com.mq.mqutils.mqdoc.pubsub;

import com.rabbitmq.client.*;

/**
 * @Title: FanoutLogRecv
 * @Package: com.mq.mqutils.mqdoc.pubsub
 * @Description: 广播接收消息
 * @Author: monkjavaer
 * @Data: 2019/4/12 17:52
 * @Version: V1.0
 */
public class FanoutLogRecv {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.19.254");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // fanout 类型
        // 1、发送消息给所有绑定到exchange的队列；
        // 2、如果此时没有任何队列绑定到exchange，那么发送的消息就会丢失。
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //non-durable, exclusive, autodelete queue
        //不能持久化的，自动删除的，唯一随机的队列名字
        String queueName = channel.queueDeclare().getQueue();

        //绑定exchange和queue
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
