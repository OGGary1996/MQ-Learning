package com.itheima.publisher;

import com.itheima.publisher.dto.OrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringAMQPTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
    * 本方法测试单消息发送
    * */
    @Test
    public void testMessageSend() {
        // 1. 队列名称，注意与管理端的队列名称一致
        String queueName = "test.queue";

        // 2. 消息内容
        String message = "Hello from Spring AMQP!";

        // 3. 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }

    /*
    * 本方法测试批量消息发送
    * */
    @Test
    public void testBatchMessageSend() {
        String queueName = "work.queue";
        String baseMessage = "Batch Message - ";
        for (int i = 1 ; i <= 50 ; i ++) {
            String message = baseMessage + i;
            rabbitTemplate.convertAndSend(queueName, message);
        }
    }

    /*
    * 本方法测试发送消息到fanout交换机
    * */
    @Test
    public void testFanoutExchangeMessageSend() {
        String exchangeName = "test.fanout";
        String message = "Hello to all subscribers!";
        // 注意：发送到fanout交换机时，路由键参数可以传null或者直接留空
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    /*
    * 以下方法测试发送消息到direct交换机
    * */
    @Test
    public void testDirectExchangeMessageSend() {
        String exchangeName = "test.direct";
        String routingKey1 = "direct1";
        String message1 = "Hello to Direct Queue 1!";
        rabbitTemplate.convertAndSend(exchangeName, routingKey1, message1);

        String routingKey2 = "direct2";
        String message2 = "Hello to Direct Queue 2!";
        rabbitTemplate.convertAndSend(exchangeName, routingKey2, message2);
    }

    /*
    * 以下方法测试发送消息到topic交换机
    * */
    @Test
    public void testTopicExchangeMessageSend() {
        String exchangeName = "test.topic";

        // 命中 order.*， 和order.#
        String routingKey1 = "order.created";
        String message1 = "Order Created Message";
        String routingKey2 = "order.updated";
        String message2 = "Order Updated Message";
        // 只命中 order.#
        String routingKey3 = "order.pay.success";
        String message3 = "Order Payment Success Message";
        String routingKey4 = "order.pay.failed";
        String message4 = "Order Payment Failed Message";

        /* 发送消息 */
        rabbitTemplate.convertAndSend(exchangeName, routingKey1, message1);
        rabbitTemplate.convertAndSend(exchangeName, routingKey2, message2);
        rabbitTemplate.convertAndSend(exchangeName, routingKey3, message3);
        rabbitTemplate.convertAndSend(exchangeName, routingKey4, message4);
    }

    /*
    * 以下方法测试发送对象消息
    * */
    @Test
    public void testObjectMessageSend() {
        String exchangeName = "test.object";
        String routingKey = "order.create";
        OrderDTO order = new OrderDTO();
        order.setOrderId(1001L);
        order.setOrderName("Test Order");
        order.setOrderMoney(299.99);

        rabbitTemplate.convertAndSend(exchangeName, routingKey, order);
    }
}




