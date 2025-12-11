package com.renking.xmall.Service;

import com.renking.xmall.Entity.Dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<ProductDto> getProducts(String productName, Integer page, Integer pageSize);

    List<ProductDto> getProductList(Integer page, Integer pageSize, String categoryId, BigDecimal maxPrice, BigDecimal minPrice);

    ProductDto getProductinfo(Integer id);

    Boolean updateProduct(Integer id, ProductDto productDto);

    Boolean addProduct(ProductDto productDto);

    void reduceStock(Integer productId, Integer quantity);
}
