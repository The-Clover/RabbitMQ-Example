package com.example.consumer.workqueue;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

/**
 * @program: RabbitMQ-Learning
 * @description: 工作队列Demo的消费者，多对一
 * @author: Clover
 * @create: 2022/05/27 14:52
 */
public class WorkQueueConsumer {
    private final static String TASK_QUEUE_NAME = "TASK_QUEUE";

    public static void main(String[] argv) throws Exception {
        // RabbitMQ连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接信息
        factory.setHost("116.62.50.204");
        factory.setPort(5656);
        factory.setUsername("clover");
        factory.setPassword("MYlove592296");
        // 获取连接
        Connection connection = factory.newConnection();
        // 根据连接创建频道
        Channel channel = connection.createChannel();
        // 在频道上声明一个队列
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        // 一次只接受一条未确认的消息
        channel.basicQos(1);
        // 消息回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            if (Integer.parseInt(message) == 6 ) {
                System.out.println(message + "被拒绝");
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            } else {
                System.out.println(message + "被确认");
                //doWork(message);
                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
            }
        };
        // 消费消息
        // 参数：队列名，消息确认，消息回调，取消消费回调
        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }

    private static void doWork(String task) {
        for (char ch: task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
