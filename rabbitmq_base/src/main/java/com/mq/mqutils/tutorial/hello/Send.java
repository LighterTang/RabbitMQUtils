package com.mq.mqutils.tutorial.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Title: Send
 * @Package: com.mq.mqutils.tutorial
 * @Description: 发送消息到指定队列
 * @Author: tangquanbin
 * @Data: 2019/4/12 13:12
 * @Version: V1.0
 */
public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.19.254");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            for (int i = 0; i < 100; i++) {
                String message = "task message " + i;

                //第一个参数是exchange,默认的""交换器
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));

                System.out.println(" [x] Sent '" + message + "'");
                Thread.sleep(1000);
            }

        }
    }
}
