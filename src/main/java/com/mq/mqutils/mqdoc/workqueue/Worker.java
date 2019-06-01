package com.mq.mqutils.mqdoc.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Title: Worker
 * @Package: com.mq.mqutils.mqdoc.workqueue
 * @Description: TODO（添加描述）
 * @Author: monkjavaer
 * @Data: 2019/4/12 13:55
 * @Version: V1.0
 */
public class Worker {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.19.254");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

//        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        //一次只接受一条未包装的消息
        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");

            boolean result = true;
            try {
                 result = doWork(message);
            } finally {
                if (result) {
                    System.out.println(" [x] Done");
                    //手动确认,第二个参数为true,批量确认;false,一次确认一条
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
                }else {
                    System.out.println("[x] Reject");
                    //否定确认，第二个参数为true,消息会重新排队；false会丢弃该消息
                    channel.basicReject(delivery.getEnvelope().getDeliveryTag(),false);
                }
            }
        };
        //true为自动确认，false需要手动确认
        boolean autoAck = false ;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
        });
    }

    /**
     * 模拟超时任务
     * @param task
     * @return
     */
    private static boolean doWork(String task) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
        return !"task message 3".equals(task);
    }
}
