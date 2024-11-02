package com.example.consumer.helloworld;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

/**
 * @program: RabbitMQ-Learning
 * @description: Hello RabbitMQ的消费者，一对一
 * @author: Clover
 * @create: 2022/05/27 01:09
 */
public class HelloWorldConsumer {
    private final static String QUEUE_NAME = "HELLO_QUEUE";

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
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        // 消息回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
            if (message.equals("4")) {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
            }
        };
        // 消费消息
        // 参数：队列名，消息确认，消息回调，取消消费回调
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
}
