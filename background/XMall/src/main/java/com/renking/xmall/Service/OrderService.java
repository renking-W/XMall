package com.renking.xmall.Service;

import com.renking.xmall.Entity.Dto.OrderDto;
import com.renking.xmall.Entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Boolean buyProduct(OrderDto orderDto);

    void saveOrderItem(OrderDto orderDto);

    List<OrderDto> getOrderList(Integer userId, Integer page, Integer pageSize, Integer status, String orderNumber, LocalDateTime startTime, LocalDateTime endTime);

    OrderDto getOrderInfo(Integer id);
}
