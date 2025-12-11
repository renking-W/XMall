package com.renking.xmall.Mapper;

import com.renking.xmall.Entity.Dto.ProductDto;
import com.renking.xmall.Entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> selectAllProducts(@Param("page") Integer page, @Param("pageSize") Integer pageSize, @Param("categoryId") String categoryId,
                                    @Param("maxPrice") BigDecimal maxPrice, @Param("minPrice") BigDecimal minPrice);

    List<Product> selectProductsByName(@Param("productName") String productName,@Param("page") Integer page,@Param("pageSize") Integer pageSize);

    @Select("select id, name, description, category_id, price, stock, cover_img, status, sales_count, rating, created_at, updated_at from product where id = #{id}")
    Product selectById(Integer id);

    Boolean updateProductById(Product product);

    Boolean insertProduct(Product product);

    void updateProductStockById(Product product);
}
