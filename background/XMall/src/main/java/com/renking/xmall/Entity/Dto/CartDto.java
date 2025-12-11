package com.renking.xmall.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto implements Serializable {
    private Integer id;             // id
    private Integer userId;         // 用户id
    private Integer productId;      // 商品id
    private ProductDto productDto;  // 商品信息
    private Integer quantity;       // 数量
}
