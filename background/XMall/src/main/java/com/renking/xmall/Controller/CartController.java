package com.renking.xmall.Controller;

import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.CartDto;
import com.renking.xmall.Entity.Result;
import com.renking.xmall.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 获取购物车列表
     * 前端传参：userId
     * @param userId
     * @return
     */
    @GetMapping("/list/{userId}")
    public Result<List<CartDto>> getCartList(@PathVariable Integer userId) {
        List<CartDto> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }

    /**
     * 添加商品到购物车
     * 前端传参： userId, productId, quantity
     * @param cartDto
     * @return
     */
    @PostMapping("/add")
    public Result<Boolean> addCart(@RequestBody CartDto cartDto) {
        Boolean result = cartService.addCart(cartDto);
        if(result.equals(Boolean.TRUE)) return Result.success(result);
        else return Result.failure(StatusCode.CART_ADD_ERROR, "添加购物车失败！");
    }

    /**
     * 删除购物车
     * 前端传参： userId, productId
     * @param cartDtoList
     * @return
     */
    @DeleteMapping("/delete")
    public Result<Boolean> deleteCart(@RequestBody List<CartDto> cartDtoList) {
        if(cartDtoList.size()==0) return Result.success( true);
        Boolean result = cartService.deleteCart(cartDtoList);
        if(result.equals(Boolean.TRUE)) return Result.success(result);
        else return Result.failure(StatusCode.CART_DELETE_ERROR, "删除购物车失败！");
    }

}
