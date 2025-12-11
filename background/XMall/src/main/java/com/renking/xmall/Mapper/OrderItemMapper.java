package com.renking.xmall.Mapper;

import com.renking.xmall.Entity.Items;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    void insertOrderItems(List<Items> items);

    List<Items> selectOrderItemsByOrderNumber(String orderNumber);

    List<Items> selectdOrderItemsByOrderNumbers(List<String> orderNumbers);
}
