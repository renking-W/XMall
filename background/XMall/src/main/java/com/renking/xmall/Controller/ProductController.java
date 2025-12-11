package com.renking.xmall.Controller;

import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.ProductDto;
import com.renking.xmall.Entity.Result;
import com.renking.xmall.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 获取商品列表
     * 对每种筛选结果都做了缓存
     * 前端传参： page, pageSize, categoryId, maxPrice, minPrice
     * @return
     */
    @GetMapping("/list")
    public Result<List<ProductDto>> getProductList(@RequestParam(value = "page",defaultValue = "1") Integer page, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                                   @RequestParam(value = "categoryId", required = false) String categoryId,
                                                   @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                                                   @RequestParam(value = "minPrice", required = false) BigDecimal minPrice) {
        List<ProductDto> products = productService.getProductList(page, pageSize, categoryId, maxPrice, minPrice);
        return Result.success(products,"获取成功！");
    }

    /**
     * 搜索商品
     * 这里不做缓存
     * 前端传参： keyword, page, pageSize
     */
    @GetMapping("/search")
    public Result<List<ProductDto>> searchProduct(@RequestParam(value = "keyword", required = true) String keyword,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<ProductDto> products = productService.getProducts(keyword,page,pageSize);
        return Result.success(products,"搜索成功！");
    }


    /**
     * 获取商品信息
     * 前端传参： id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<ProductDto> getProductInfo(@PathVariable("id") Integer id) {
        ProductDto product = productService.getProductinfo(id);
        return Result.success(product,"获取成功！");
    }


    /**
     * 修改商品信息
     * 前端传参： id, product{ name,categoryId,price,stock,coverImg,status }
     * @param id
     * @param product
     * @return
     */
    @PutMapping("/update/{id}")
    public Result<Boolean> updateProduct(@PathVariable("id") Integer id, @RequestBody ProductDto product) {
        Boolean flag = productService.updateProduct(id, product);
        if(Boolean.TRUE.equals(flag)) return Result.success(flag,"修改成功！");
        else return Result.failure(StatusCode.PRODUCT_EDIT_EROR, "修改失败！");
    }


    /**
     * 添加商品
     * 前端传参： product{ name,categoryId,price,stock,coverImg,status }
     * @param product
     * @return
     */
    @PostMapping("/add")
    public Result<Boolean> addProduct(@RequestBody ProductDto product) {
        Boolean flag = productService.addProduct(product);
        if(Boolean.TRUE.equals(flag)) return Result.success(flag,"添加成功！");
        else return Result.failure(StatusCode.PRODUCT_ADD_ERROR, "添加失败！");
    }



}
