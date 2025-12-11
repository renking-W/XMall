package com.renking.xmall.Controller;

import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.OrderDto;
import com.renking.xmall.Entity.Order;
import com.renking.xmall.Entity.Result;
import com.renking.xmall.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 购买商品
     * @param orderDto
     * @return
     */
    @PostMapping("/buy")
    public Result<Boolean> buyProduct(@RequestBody OrderDto orderDto) {
         Boolean flag = orderService.buyProduct(orderDto);
        if(flag)  return Result.success(flag,"购买成功");
        else return Result.failure(StatusCode.ORDER_BUY_ERROR, "充值失败");
    }

    /**
     * 查询订单列表
     * @param userId
     * @return
     */
    @GetMapping("/list/{userId}")
    public Result<List<OrderDto>> getOrderList(@PathVariable Integer userId,
                                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                                            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "status",required = false) Integer status,
                                            @RequestParam(value = "orderNumber",required = false) String orderNumber,
                                            @RequestParam(value = "startTime",required = false) LocalDateTime startTime,
                                            @RequestParam(value = "endTime",required = false) LocalDateTime endTime
    ) {
        List<OrderDto> orders = orderService.getOrderList(userId, page, pageSize, status, orderNumber, startTime, endTime);
        return Result.success(orders,"查询成功");
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<OrderDto> getOrderInfo(@PathVariable Integer id) {
        OrderDto orderDto = orderService.getOrderInfo(id);
        return Result.success(orderDto,"查询成功");
    }
}
