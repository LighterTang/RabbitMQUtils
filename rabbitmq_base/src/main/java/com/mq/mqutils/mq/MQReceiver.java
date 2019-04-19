
package com.mq.mqutils.mq;


import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;


public class MQReceiver extends Observable implements Runnable, Consumer {

    public String queueName;
    private static ThreadPoolExecutor MQThreadPool = new ThreadPoolExecutor(BlockQueue.corePookSize, BlockQueue.maximumPoolSize,
            BlockQueue.keepActiveTime, BlockQueue.timeUnit, BlockQueue.taskServiceQueue, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("MQReceiver");
            return t;
        }
    });
    private static final Logger log = LogManager.getLogger(MQReceiver.class.getName());

    Connection connection = null;
    Channel channel = null;

    private static Map<String,MQReceiver> instanceMap = new HashMap<>();

    private MQReceiver() {
    }

    public static MQReceiver listen(Observer observer,String queueName) {
        MQReceiver instance = null;
        if (!instanceMap.containsKey(queueName)){
            instance = new MQReceiver();
            instance.queueName = queueName;
            instanceMap.put(queueName,instance);
        }else {
            instance = instanceMap.get(queueName);
        }
        MQThreadPool.execute(instance);
        instance.addObserver(observer);
        return instance;

    }

    private void conn() {
        while(true) {
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost(EnvConstant.HOST);
            factory.setPort(EnvConstant.MESSAGE_QUEUE_PORT);
            factory.setUsername(EnvConstant.MESSAGE_QUEUE_USERNAME);
            factory.setPassword(EnvConstant.MESSAGE_QUEUE_PASSWORD);
            //factory.setAutomaticRecoveryEnabled(true);
            //factory.setNetworkRecoveryInterval(60 * 60 * 24);
            try {
                connection = factory.newConnection();
                channel = connection.createChannel();
                break;
            } catch (IOException e) {
                //e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        try {
            conn();
            channel.basicConsume(queueName, true, this);

            log.info("MQ configuration in MQReceiver succeeded. QUEUE NAME = " + EnvConstant.PASSDETEC_WEB_QUEUE_NAME + " Waiting for messages...");

        } catch (IOException ioEx) {

        }

    }

    @Override
    public void handleConsumeOk(String s) {

    }

    @Override
    public void handleCancelOk(String s) {

    }

    @Override
    public void handleCancel(String s) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String s, ShutdownSignalException e) {

    }

    @Override
    public void handleRecoverOk(String s) {

    }

    @Override
    public void handleDelivery(String s, Envelope envelope,
            AMQP.BasicProperties basicProperties,
            byte[] bytes) throws IOException {


        log.info("MQReceiver received message type = ");
        log.info("MQReceiver received and observers = ");

        setChanged();
        notifyObservers(bytes);
    }
}
