package com.renking.xmall.Common.Consumer;

import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.RabbitConfig;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.OrderDto;
import com.renking.xmall.Service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    //保存订单物品
    @RabbitListener(queues = RabbitConfig.ORDER_ITEM_SAVE_QUEUE)
    public void saveOrderItem(OrderDto orderDto) {
        log.info("保存订单物品队列开始消费");
        try {
            orderService.saveOrderItem(orderDto);
        } catch (Exception e) {
            log.error("保存订单物品失败！",e);
            throw new ServiceException("保存订单物品失败！", StatusCode.ORDER_BUY_ERROR);
        }
    }

}
