package com.itheima.consumer.mq;

import com.itheima.consumer.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/*
* 本类作为RabbitMQ消息监听器使用
* 需要添加@Component注解交给Spring容器管理
* */
@Component
@Slf4j
public class SpringRabbitListener {
    /*
    * 监听方法，当指定队列有消息时，会自动触发该方法执行
    * 监听方法需要添加@RabbitListener注解，指定监听的队列名称
    * 方法参数：根据消息的类型决定，消息为String类型，则方法参数为String类型
    * */
    @RabbitListener(queues = "test.queue")
    public void listenMessage(String message) {
        log.info("Consumer,收到消息：{}", message);
    }

    /*
    * 以下方法监听work.queue队列，模拟工作队列场景下的多个消费者消费，并未绑定到交换机
    * */
    @RabbitListener(queues = "work.queue")
    public void listenMessageFromWorkQueue1(String message) {
        try {
            // 模拟消费者1的处理耗时
            Thread.sleep(25); // 每秒处理40条消息
            log.info("Consumer1,收到消息：{}", message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @RabbitListener(queues = "work.queue")
    public void listenMessageFromWorkQueue2(String message) {
        try {
            // 模拟消费者2的处理耗时
            Thread.sleep(200); // 每秒处理5条消息
            log.info("Consumer2,收到消息：{}", message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * 以下方法监听fanout.queue1和fanout.queue2队列，这两个队列绑定到了test.fanout交换机
    * */
    @RabbitListener(queues = "fanout.queue1")
    public void listenMessageFromFanoutQueue1(String message) {
        log.info("Fanout Queue1,收到消息：{}", message);
    }
    @RabbitListener(queues = "fanout.queue2")
    public void listenMessageFromFanoutQueue2(String message) {
        log.info("Fanout Queue2,收到消息：{}", message);
    }

    /*
    * 以下方法监听direct.queue1和direct.queue2队列，这两个队列绑定到了test.direct交换机
    * */
//    @RabbitListener(queues = "direct.queue1")
//    public void listenMessageFromDirectQueue1(String message) {
//        log.info("Direct Queue1,收到消息：{}", message);
//    }
//    @RabbitListener(queues = "direct.queue2")
//    public void listenMessageFromDirectQueue2(String message) {
//        log.info("Direct Queue2,收到消息：{}", message);
//    }
    // 优化为使用注解方式声明 Queue + Exchange + Binding, 避免手动创建队列和交换机的麻烦
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "test.direct", type = ExchangeTypes.DIRECT, durable = "true"),
            key = "direct1"
    ))
    public void listenMessageFromDirectQueue1(String message) {
        log.info("Direct Queue1,收到消息：{}", message);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "test.direct", type = ExchangeTypes.DIRECT, durable = "true"),
            key = "direct2"
    ))
    public void listenMessageFromDirectQueue2(String message) {
        log.info("Direct Queue2,收到消息：{}", message);
    }



    /*
    * 以下方法监听topic.queue1和topic.queue2队列，这两个队列绑定到了test.topic交换机
    * */
//    @RabbitListener(queues = "topic.queue1")
//    public void listenMessageFromTopicQueue1(String message) {
//        log.info("Topic Queue1,收到消息：{}", message);
//    }
//    @RabbitListener(queues = "topic.queue2")
//    public void listenMessageFromTopicQueue2(String message) {
//        log.info("Topic Queue2,收到消息：{}", message);
//    }
    // 优化为使用注解方式声明 Queue + Exchange + Binding, 避免手动创建队列和交换机的麻烦
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "test.topic", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "order.*"
    ))
    public void listenMessageFromTopicQueue1(String message) {
        log.info("Topic Queue1,收到消息：{}", message);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "test.topic", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "order.#"
    ))
    public void listenMessageFromTopicQueue2(String message) {
        log.info("Topic Queue2,收到消息：{}", message);
    }

    /*
    * 以下方法监听object.queue队列，接收消息并将其转换为OrderDTO对象
    * */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "object.queue"),
            exchange = @Exchange(name = "test.object", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "order.*"
    ))
    public void listenMessageFromObjectQueue(OrderDTO orderDTO) {
        log.info("Object Queue,收到OrderDTO对象：{}", orderDTO);
    }

}
