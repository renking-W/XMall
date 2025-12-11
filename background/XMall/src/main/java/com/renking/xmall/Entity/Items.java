package com.renking.xmall.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Items extends BaseEntity implements Serializable {

    private Integer id;         // id
    private String orderNumber;    // 订单编号
    private Integer productId;  // 商品id
    private String productName;         // 商品名称
    private BigDecimal productPrice;    // 商品价格
    private Integer quantity;           // 商品数量
    private BigDecimal totalPrice;      // 商品总价

}
