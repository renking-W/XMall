package com.renking.xmall.Entity.Dto;

import com.renking.xmall.Entity.Items;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto implements Serializable {

    private Integer id;     // id
    private String orderNumber;  // 订单编号
    private Integer userId;  // 用户id
    private BigDecimal totalPrice;   // 总价
    private Integer status;     // 订单状态
    private Integer paymentMethod;    // 支付方式
    private Integer addressId;    // 地址id
    private String remark;      // 备注
    private LocalDateTime paidAt;  // 支付时间

    List<Items> items;      // 商品列表
}
