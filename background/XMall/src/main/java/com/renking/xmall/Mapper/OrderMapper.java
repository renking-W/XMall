package com.renking.xmall.Mapper;

import com.renking.xmall.Entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {


    void insertOrder(Order order);

    List<Order> selectOrderList(@Param("userId") Integer userId,@Param("page") Integer page,@Param("pageSize") Integer pageSize,
                                @Param("status")Integer status,@Param("orderNumber") String orderNumber,@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime);

    Order selectOrderById(Integer id);
}
