package com.renking.xmall.Common.Consumer;

import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.RabbitConfig;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.OrderDto;
import com.renking.xmall.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserConsumer {

    private final UserService userService;

    @RabbitListener(queues = RabbitConfig.USER_REDUCE_QUEUE)
    public void reduceMoney(OrderDto orderDto){
        log.info("用户服务开始扣减用户余额");
        try {
            userService.reduceMoney(orderDto.getUserId(), orderDto.getTotalPrice());
        } catch (Exception e) {
            throw new ServiceException("用户服务扣减用户余额失败！", StatusCode.USER_REDUCE_MONEY_ERROR);
        }
        log.info("用户服务扣减用户余额成功");
    }



}
