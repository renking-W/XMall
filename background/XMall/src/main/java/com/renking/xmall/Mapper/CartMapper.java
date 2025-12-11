package com.renking.xmall.Mapper;

import com.renking.xmall.Entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    List<Cart> selectCartsByUserId(Integer userId);

    Boolean insertCart(Cart cart);

    Boolean deleteCarts(@Param("cartList") List<Cart> cartList,@Param("userId") Integer userId);
}
