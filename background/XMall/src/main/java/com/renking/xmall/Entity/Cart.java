package com.renking.xmall.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseEntity implements Serializable {

    private Integer id;             // id
    private Integer userId;         // 用户id
    private Integer productId;      // 商品id
    private Integer quantity;       // 数量

}
