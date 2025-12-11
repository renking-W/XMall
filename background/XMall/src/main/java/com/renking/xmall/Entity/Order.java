package com.renking.xmall.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order extends BaseEntity implements Serializable {

    private Integer id;     // id
    private String orderNumber;  // 订单编号
    private Integer userId;  // 用户id
    private BigDecimal totalPrice;   // 总价
    private Integer status;     // 订单状态
    private Integer paymentMethod;    // 支付方式
    private Integer addressId;    // 地址id
    private String remark;      // 备注
    private String paidAt;  // 支付时间
}
