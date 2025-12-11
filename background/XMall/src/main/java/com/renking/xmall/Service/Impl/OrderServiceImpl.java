package com.renking.xmall.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.RabbitConfig;
import com.renking.xmall.Config.RedisConfig;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.OrderDto;
import com.renking.xmall.Entity.Dto.ProductDto;
import com.renking.xmall.Entity.Dto.UserDto;
import com.renking.xmall.Entity.Items;
import com.renking.xmall.Entity.Order;
import com.renking.xmall.Mapper.OrderItemMapper;
import com.renking.xmall.Mapper.OrderMapper;
import com.renking.xmall.Service.OrderService;
import com.renking.xmall.Service.ProductService;
import com.renking.xmall.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public Boolean buyProduct(OrderDto orderDto) {
        UserDto userDto = new UserDto();
        userDto.setId(orderDto.getUserId());
        userDto = userService.getInfo(userDto);
        if(userDto.getMoney().compareTo(orderDto.getTotalPrice())<0){
            log.warn("用户:{}余额不足！",userDto.getUserName());
            throw new ServiceException("用户余额不足！", StatusCode.USER_MONEY_NOT_ENOUGH);
        }
        //生成订单号
        String orderNumber = "XM" + System.currentTimeMillis() + userDto.getId() + (int)(Math.random() * 10000);
        orderDto.setOrderNumber(orderNumber);
        orderDto.getItems().forEach(items -> items.setOrderNumber(orderNumber));
        //检查库存是否充足
        orderDto.getItems().forEach(items -> {
            ProductDto productDto = productService.getProductinfo(items.getProductId());
            if(productDto.getStock()<items.getQuantity()){
                log.warn("商品:{}库存不足！",productDto.getName());
                throw new ServiceException("商品库存不足！", StatusCode.PRODUCT_STOCK_NOT_ENOUGH);
            }
        });
        //生成订单
        orderDto.setStatus(0);      //默认为未支付
        orderDto.setPaymentMethod(3);   //默认余额支付
        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);
        // TODO 在这边添加上分布式事务，确保一致性
        try {
            //扣减库存
            rabbitTemplate.convertAndSend(RabbitConfig.PRODUCT_EXCHANGE,RabbitConfig.PRODUCT_STOCK_REDUCE_ROUTING_KEY, orderDto);
            //扣减余额
            rabbitTemplate.convertAndSend(RabbitConfig.USER_EXCHANGE,RabbitConfig.USER_MONEY_REDUCE_ROUTING_KEY, orderDto);
            //保存订单
            rabbitTemplate.convertAndSend(RabbitConfig.ORDER_ITEM_SAVE_EXCHANGE,RabbitConfig.ORDER_ITEM_SAVE_ROUTING_KEY, orderDto);
        } catch (AmqpException e) {
            log.error("消息发送失败！",e);
            throw new ServiceException("消息消费失败！", StatusCode.MQ_CONSUMER_ERROR);
        }
        orderMapper.insertOrder(order);
        return Boolean.TRUE;
    }

    @Override
    public void saveOrderItem(OrderDto orderDto) {
        try {
            orderItemMapper.insertOrderItems(orderDto.getItems());
        } catch (Exception e) {
            log.error("保存订单物品失败！",e);
            throw new ServiceException("保存订单物品失败！", StatusCode.ORDER_BUY_ERROR);
        }
    }

    @Override
    public List<OrderDto> getOrderList(Integer userId, Integer page, Integer pageSize, Integer status, String orderNumber, LocalDateTime startTime, LocalDateTime endTime) {
        page = (page-1) * pageSize;
        List<Order> orders = orderMapper.selectOrderList(userId, page, pageSize, status, orderNumber, startTime, endTime);
        List<OrderDto> orderDtos = orders.stream().map(order -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);
            return orderDto;
        }).toList();
        List<String> orderNumbers = orderDtos.stream().map(OrderDto::getOrderNumber).toList();
        List<Items> items = new ArrayList<>();
        //批量查询物品表
        if(!orderNumbers.isEmpty()) {
            items = orderItemMapper.selectdOrderItemsByOrderNumbers(orderNumbers);
        }
        Map<String, List<Items>> itemsMap = items.stream().collect(Collectors.groupingBy(Items::getOrderNumber));
        //将订单号对应的物品映射到订单中
        orderDtos.forEach(orderDto -> {
            orderDto.setItems(itemsMap.getOrDefault(orderDto.getOrderNumber(), Collections.emptyList()));
        });
        return orderDtos;
    }

    @Override
    public OrderDto getOrderInfo(Integer id) {
        OrderDto orderDto = new OrderDto();
        //缓存key
        String key = RedisConfig.ORDER_INFO_KEY + ":" + id;
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        //缓存命中
        if(jsonStr!=null && !jsonStr.equals("null")){
            try {
                orderDto = objectMapper.readValue(jsonStr, OrderDto.class);
            } catch (JsonProcessingException e) {
                log.error("数据解析失败！",e);
                throw new ServiceException("数据解析失败！", StatusCode.DATA_PARSE_ERROR);
            }
            return orderDto;
        }
        //数据库查询
        Order order = null;
        try {
            order = orderMapper.selectOrderById(id);
        } catch (Exception e) {
            log.error("数据查询失败！",e);
            throw new ServiceException("数据查询失败！", StatusCode.DATA_QUERY_ERROR);
        }
        BeanUtils.copyProperties(order, orderDto);
        return orderDto;
    }
}
