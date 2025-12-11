package com.renking.xmall.Common.Consumer;

import com.renking.xmall.Config.RabbitConfig;
import com.renking.xmall.Entity.Dto.OrderDto;
import com.renking.xmall.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductConsumer {

    private final ProductService productService;

    @RabbitListener(queues = RabbitConfig.PRODUCT_REDUCE_QUEUE)
    public void reduceStock(OrderDto orderDto){
        log.info("商品服务开始扣减库存");
        orderDto.getItems().forEach(items -> {
            productService.reduceStock(items.getProductId(),items.getQuantity());
        });
        log.info("商品服务扣减库存成功");
    }

}
