package com.renking.xmall.Service;

import com.renking.xmall.Entity.Dto.CartDto;
import com.renking.xmall.Entity.Dto.OrderDto;

import java.util.List;

public interface CartService {
    List<CartDto> getCartList(Integer userId);

    Boolean addCart(CartDto cartDto);

    Boolean deleteCart(List<CartDto> cartDtoList);

    void updateCart(OrderDto orderDto);
}
