package com.renking.xmall.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product extends BaseEntity implements Serializable {
    // id
    private Integer id;
    //商品名称
    private String name;
    //描述
    private String description;
    //分类id
    private Integer categoryId;
    //价格
    private BigDecimal price;
    //库存
    private Integer stock;
    //封面url
    private String coverImg;
    //状态
    private Integer status;
    //销售数量
    private Integer salesCount;
    //平均评分
    private BigDecimal rating;
}
