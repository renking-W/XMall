package com.renking.xmall.Common.Consumer;

import com.renking.xmall.Config.RabbitConfig;
import com.renking.xmall.Entity.Dto.OrderDto;
import com.renking.xmall.Service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CartConsumer {

    private final CartService cartService;

    @RabbitListener(queues = RabbitConfig.CART_DELETE_QUEUE)
    public void deleteCart(OrderDto orderDto) {
        log.info("更新购物车队列开始消费");
        try {
            cartService.updateCart(orderDto);
        } catch (Exception e) {
            log.error("更新购物车失败！",e);
        }
    }
}
