package com.example.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: RabbitMQDemo
 * @description: RabbitMQ Configuration
 * @author: Clover
 * @create: 2022/05/26 00:35
 */
@Configuration
public class RabbitMQConfig {

    // Topic exchange
    @Bean
    public TopicExchange exchange (){
        return new TopicExchange("topicExchangeName");
    }

    // fanout exchange
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    // binding
    // BindingBuilder.bing.to.with
    @Bean
    Binding bindingExchangeOne(Queue queue_one, TopicExchange exchange){
        return BindingBuilder.bind(queue_one).to(exchange).with("topic.one");
    }

    // binding
    // BindingBuilder.bing.to.with
    @Bean
    Binding bindingExchangeTwo(Queue queue_two, TopicExchange exchange){
        // # 表示零个或多个词
        // * 表示一个词
        return BindingBuilder.bind(queue_two).to(exchange).with("topic.#");
    }

    @Bean
    public Queue Queue(){
        return new Queue("hello");
    }
}
