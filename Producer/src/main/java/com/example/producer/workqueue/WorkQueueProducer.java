package com.example.producer.workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: RabbitMQ-Learning
 * @description: 工作队列Demo的生产者，一对多
 * @author: Clover
 * @create: 2022/05/28 16:02
 */
public class WorkQueueProducer {
    private final static String TASK_QUEUE_NAME = "TASK_QUEUE";
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
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            // 消息
            for(int i=0; i < 10; i++) {
                String message = "" + i;
                // 推送和消息到队列
                // 参数：消息发布到exchange，队列名，消息的属性，消息的字节数组
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
