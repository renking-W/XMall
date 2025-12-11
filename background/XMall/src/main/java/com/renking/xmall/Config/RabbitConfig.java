package com.renking.xmall.Config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    //订单交换机名称
    public static final String ORDER_EXCHANGE = "order_exchange";
    //生成订单绑定key
    public static final String ORDER_GENERATE_ROUTING_KEY = "order.generate";
    //生成订单队列
    public static final String ORDER_GENERATE_QUEUE = "order_generate_queue";
    //商品交换机
    public static final String PRODUCT_EXCHANGE = "product_exchange";
    //扣减库存绑定key
    public static final String PRODUCT_STOCK_REDUCE_ROUTING_KEY = "product.reduce.stock";
    //扣减库存队列
    public static final String PRODUCT_REDUCE_QUEUE = "product_reduce_queue";
    //用户交换机
    public static final String USER_EXCHANGE = "user_exchange";
    //扣减用户余额绑定key
    public static final String USER_MONEY_REDUCE_ROUTING_KEY = "user.reduce.money";
    //扣减用户余额队列
    public static final String USER_REDUCE_QUEUE = "user_reduce_queue";
    //保存订单物品交换机
    public static final String ORDER_ITEM_SAVE_EXCHANGE = "order_item_save_exchange";
    //保存订单物品绑定key
    public static final String ORDER_ITEM_SAVE_ROUTING_KEY = "order.item.save";
    //保存订单物品队列
    public static final String ORDER_ITEM_SAVE_QUEUE = "order_item_save_queue";

    //订单队列
    @Bean
    public Queue orderGenerateQueue() {
        return new Queue(ORDER_GENERATE_QUEUE, true);
    }

    //保存订单物品队列
    @Bean
    public Queue orderItemSaveQueue() {
        return new Queue(ORDER_ITEM_SAVE_QUEUE, true);
    }

    //扣减库存队列
    @Bean
    public Queue productReduceQueue() {
        return new Queue(PRODUCT_REDUCE_QUEUE, true);
    }

    //扣减用户余额队列
    @Bean
    public Queue userReduceQueue() {
        return new Queue(USER_REDUCE_QUEUE, true);
    }

    //订单交换机
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    //保存订单物品交换机
    @Bean
    public TopicExchange orderItemSaveExchange() {
        return new TopicExchange(ORDER_ITEM_SAVE_EXCHANGE);
    }

    //商品交换机
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    //用户交换机
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    //订单生成绑定
    @Bean
    public Binding orderGenerateBinding() {
        return BindingBuilder.bind(orderGenerateQueue()).to(orderExchange())
                .with(ORDER_GENERATE_ROUTING_KEY);
    }

    //保存订单物品绑定
    @Bean
    public Binding orderItemSaveBinding() {
        return BindingBuilder.bind(orderItemSaveQueue()).to(orderItemSaveExchange())
                .with(ORDER_ITEM_SAVE_ROUTING_KEY);
    }

    //扣减库存绑定
    @Bean
    public Binding productReduceBinding() {
        return BindingBuilder.bind(productReduceQueue()).to(productExchange())
                .with(PRODUCT_STOCK_REDUCE_ROUTING_KEY);
    }

    //扣减用户余额绑定
    @Bean
    public Binding userReduceBinding() {
        return BindingBuilder.bind(userReduceQueue())
                .to(userExchange()).with(USER_MONEY_REDUCE_ROUTING_KEY);
    }

}
