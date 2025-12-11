package com.renking.xmall.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.RedisConfig;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Cart;
import com.renking.xmall.Entity.Dto.CartDto;
import com.renking.xmall.Entity.Dto.OrderDto;
import com.renking.xmall.Entity.Items;
import com.renking.xmall.Mapper.CartMapper;
import com.renking.xmall.Service.CartService;
import com.renking.xmall.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final ProductService productService;

    @Override
    public List<CartDto> getCartList(Integer userId) {
        //获取缓存
        String cacheKey = RedisConfig.CART_INFO_KEY + ":" + userId;
        String jsonStr = stringRedisTemplate.opsForValue().get(cacheKey);
        List<CartDto> cartList = null;
        //缓存命中
        if(jsonStr!=null && !jsonStr.equals("null")){
            try {
                cartList = objectMapper.readValue(jsonStr, new TypeReference<List<CartDto>>() {});
            } catch (JsonProcessingException e) {
                log.error("数据解析失败！", e);
                throw new ServiceException("数据解析失败！", StatusCode.DATA_PARSE_ERROR);
            }
            return cartList;
        }
        //未命中，查询数据库
        List<Cart> carts = cartMapper.selectCartsByUserId(userId);
        cartList = carts.stream().map(cart -> {
            CartDto cartDto = new CartDto();
            BeanUtils.copyProperties(cart, cartDto);
            return cartDto;
        }).toList();
        cartList.forEach(cartDto -> {
            cartDto.setProductDto(productService.getProductinfo(cartDto.getProductId() )  );
        });
        try {
            jsonStr = objectMapper.writeValueAsString(cartList);
        } catch (JsonProcessingException e) {
            log.error("数据解析失败！", e);
            throw new ServiceException("数据解析失败！", StatusCode.DATA_PARSE_ERROR);
        }
        //存入缓存
        stringRedisTemplate.opsForValue().set(cacheKey,jsonStr,RedisConfig.CART_INFO_EXPIRE_TIME, TimeUnit.SECONDS);
        return cartList;
    }

    @Override
    public Boolean addCart(CartDto cartDto) {
        Cart cart = new Cart();
        BeanUtils.copyProperties(cartDto, cart);
        Boolean flag = cartMapper.insertCart(cart);
        //更新缓存
        String cacheKey = RedisConfig.CART_INFO_KEY + ":" + cartDto.getUserId();
        stringRedisTemplate.delete(cacheKey);
        return flag;
    }

    @Override
    public Boolean deleteCart(List<CartDto> cartDtoList) {
        List<Cart> cartList = cartDtoList.stream().map(cartDto -> {
            Cart cart = new Cart();
            BeanUtils.copyProperties(cartDto, cart);
            return cart;
        }).toList();
        Integer userId = cartList.get(0).getUserId();
        Boolean flag = cartMapper.deleteCarts(cartList,userId);
        //更新缓存
        String cacheKey = RedisConfig.CART_INFO_KEY + ":" + userId;
        stringRedisTemplate.delete(cacheKey);
        return flag;
    }

    @Override
    public void updateCart(OrderDto orderDto) {
        List<Items> items = orderDto.getItems();
        List<CartDto> cartDtoList = items.stream().map(item -> {
            Cart cart = new Cart();
            cart.setUserId(orderDto.getUserId());
            cart.setProductId(item.getProductId());
            cart.setQuantity(item.getQuantity());
            CartDto cartDto = new CartDto();
            BeanUtils.copyProperties(cart, cartDto);
            return cartDto;
        }).toList();
        deleteCart(cartDtoList);
    }

}
