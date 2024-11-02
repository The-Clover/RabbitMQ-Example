package com.example.consumer.topicRouting;

import com.rabbitmq.client.*;

/**
 * @program: RabbitMQ-Learning
 * @description: Topic Routing 消费者
 * @author: Clover
 * @create: 2022/05/29 14:11
 */
public class TopicRoutingConsumer {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接信息
        factory.setHost("116.62.50.204");
        factory.setPort(5656);
        factory.setUsername("clover");
        factory.setPassword("MYlove592296");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        String bindingKey = "*.white.*";
        channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);


        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
