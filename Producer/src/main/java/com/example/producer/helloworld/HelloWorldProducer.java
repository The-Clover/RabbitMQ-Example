package com.example.producer.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: RabbitMQ-Learning
 * @description: Hello RabbitMQ的生产者，一对一
 * @author: Clover
 * @create: 2022/05/27 00:25
 */
public class HelloWorldProducer {
    private final static String QUEUE_NAME = "HELLO_QUEUE";
    public static void main(String[] args) {
        // RabbitMQ连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接信息
        factory.setHost("116.62.50.204");
        factory.setPort(5656);
        factory.setUsername("clover");
        factory.setPassword("MYlove592296");
        // 从工厂获取连接
        // 根据连接创建频道
        // try-with-resources：自动关闭资源
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 在频道上声明一个队列
            // 参数：队列名、队列是否一直存在，是否为独占队列，是否自动删除，其它参数
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.confirmSelect();
            // 消息
            for (int i=0; i<5; i++) {
                String message = "" + i;
                // 推送和消息到队列
                // 参数：消息发布到exchange，队列名，消息的属性，消息的字节数组
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
