package com.itheima.consumer.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* 本配置类用于配置RabbitMQ的Queue、Exchange及Binding等信息
* */
@Configuration
public class rabbitConfig {

    /*
    * 以下配置为一整套完整的Fanout + Queue + Binding 的配置示例
    * */
    // 1. Queue配置
    // 1.1 创建第一个队列fanout.queue1
    @Bean
    public Queue fanoutQueue1() {
        return QueueBuilder
                .durable("fanout.queue1") // 队列名称为fanout.queue1，持久化
                .build();
    }
    // 1.2 创建第二个队列fanout.queue2
    @Bean
    public Queue fanoutQueue2() {
        return QueueBuilder
                .durable("fanout.queue2") // 队列名称为fanout.queue2，持久化
                .build();
    }

    // 2. Exchange配置
    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder
                .fanoutExchange("test.fanout") // Exchange名称为test.fanout
                .durable(true) // 持久化
                .build();
    }
    // 3. Binding配置, 注意 Fanout Exchange不需要指定RoutingKey
    // 3.1 将队列fanout.queue1绑定到Exchange test.fanout
    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder
                .bind(fanoutQueue1()) // 绑定队列fanout.queue1
                .to(fanoutExchange()); // 到Exchange test.fanout
    }
    // 3.2 将队列fanout.queue2绑定到Exchange test.fanout
    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder
                .bind(fanoutQueue2()) // 绑定队列fanout.queue2
                .to(fanoutExchange()); // 到Exchange test.fanout
    }
}
