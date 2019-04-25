package com.mq.mqutils.mqdoc.hello;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Title: Recv
 * @Package: com.mq.mqutils.mqdoc
 * @Description: 接收指定队列的消息
 * @Author: tangquanbin
 * @Data: 2019/4/12 13:23
 * @Version: V1.0
 */
public class Recv {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.19.254");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

/*        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery delivery) throws IOException {
                System.out.println("DeliverCallback consumerTag:"+consumerTag);
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("CancelCallback consumerTag:"+consumerTag);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);*/

        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, "myConsumerTag", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String contentType = properties.getContentType();
                System.out.println("contentType=" + contentType);
                long deliveryTag = envelope.getDeliveryTag();
                System.out.println("deliveryTag=" + deliveryTag);
                String message = new String(body, "UTF-8");
                System.out.println("receive message=" + message);
                channel.basicAck(deliveryTag, false);
                if ("task message 4".equals(message)){
                    handleCancel(consumerTag);
                }
            }

            @Override
            public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
                System.out.println(consumerTag+":handleShutdownSignal");
                super.handleShutdownSignal(consumerTag, sig);
            }

            @Override
            public void handleConsumeOk(String consumerTag) {
                System.out.println(consumerTag+":handleConsumeOk");
                super.handleConsumeOk(consumerTag);
            }

            @Override
            public void handleCancelOk(String consumerTag) {
                System.out.println(consumerTag+" Cancel Ok ！");
            }

            @Override
            public void handleCancel(String consumerTag) throws IOException {
                System.out.println("start cancel consumer :"+consumerTag);
                channel.basicCancel(consumerTag);
            }
        });

    }
}
