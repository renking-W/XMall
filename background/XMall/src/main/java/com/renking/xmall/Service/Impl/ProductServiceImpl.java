package com.renking.xmall.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.RedisConfig;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.ProductDto;
import com.renking.xmall.Entity.Product;
import com.renking.xmall.Mapper.ProductMapper;
import com.renking.xmall.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<ProductDto> getProducts(String productName, Integer page, Integer pageSize) {
        List<Product> products = null;
        page = (page-1)*pageSize;
        try {
            log.info("开始查询，查询参数：productName:{},page:{},pageSize:{}",productName,page,pageSize);
            products = productMapper.selectProductsByName(productName, page, pageSize);
        } catch (Exception e) {
            log.error("查询失败！",e);
            throw new ServiceException("查询失败，请稍后重试！", StatusCode.QUERY_FAILED);
        }
        log.info("查询成功！结果为：{}",products);
        List<ProductDto> res = products.stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    BeanUtils.copyProperties(product, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return res;
    }

    @Override
    public List<ProductDto> getProductList(Integer page, Integer pageSize, String categoryId, BigDecimal maxPrice, BigDecimal minPrice) {
        //获取缓存key----兼容不同的条件
        String cacheKey = RedisConfig.PRODUCT_List_KEY+ ":" +page+":"+pageSize+":"+categoryId+":"+maxPrice+":"+minPrice;
        String value = stringRedisTemplate.opsForValue().get(cacheKey);
        //缓存命中
        if(value!=null && !value.equals("null")){
            // 创建 TypeReference 实例
            TypeReference<List<ProductDto>> typeRef = new TypeReference<List<ProductDto>>() {};
            try {
                List<ProductDto> productDtos = objectMapper.readValue(value, typeRef);
                return productDtos;
            } catch (JsonProcessingException e) {
                log.error("数据解析失败！",e);
                throw new ServiceException("解析数据失败！", StatusCode.DATA_PARSE_ERROR);
            }
        }
        List<Product> products = null;
        page = (page-1)*pageSize;
        try {
            log.info("开始查询，查询参数：page:{},pageSize:{},categoryId:{},maxPrice:{},minPrice:{}",page,pageSize,categoryId,maxPrice,minPrice);
            products = productMapper.selectAllProducts(page, pageSize, categoryId, maxPrice, minPrice);
        } catch (Exception e) {
            log.error("查询失败！",e);
            throw new ServiceException("查询失败，请稍后重试！", StatusCode.QUERY_FAILED);
        }
        log.info("查询成功！结果为：{}",products);
        List<ProductDto> res = products.stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    BeanUtils.copyProperties(product, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        try {
            if(!products.isEmpty()) stringRedisTemplate.opsForValue().set(cacheKey,objectMapper.writeValueAsString(products));
            else stringRedisTemplate.opsForValue().set(cacheKey,"null",RedisConfig.NULL_EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("缓存写入失败！",e);
            throw new ServiceException("缓存写入失败！", StatusCode.CACHE_WRITE_FAILED);
        }
        return res;
    }

    @Override
    public ProductDto getProductinfo(Integer id) {
        //先查询缓存
        String cacheKey = RedisConfig.PRODUCT_INFO_KEY+":"+id;
        String jsonStr = stringRedisTemplate.opsForValue().get(cacheKey);
        log.info("开始查询缓存，缓存key为：{},查询结果{}",cacheKey,jsonStr);
        if(jsonStr!=null && !jsonStr.equals("null") ){
            try {
                ProductDto productDto = new ProductDto();
                BeanUtils.copyProperties(objectMapper.readValue(jsonStr, Product.class), productDto);
                return productDto;
            } catch (JsonProcessingException e) {
                throw new ServiceException("数据解析失败！", StatusCode.DATA_PARSE_ERROR);
            }
        }
        //未命中查询数据库
        Product product = productMapper.selectById(id);
        if(product==null){
            stringRedisTemplate.opsForValue().set(cacheKey,"null",RedisConfig.NULL_EXPIRE_TIME, TimeUnit.SECONDS);
            throw new ServiceException("商品不存在！", StatusCode.PRODUCT_NOT_EXIST);
        }
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        try {
            stringRedisTemplate.opsForValue().set(cacheKey,objectMapper.writeValueAsString(product));
        } catch (JsonProcessingException e) {
            log.error("数据解析失败！",e);
            throw new ServiceException("数据解析失败！", StatusCode.DATA_PARSE_ERROR);
        }
        return productDto;
    }

    @Override
    public Boolean updateProduct(Integer id, ProductDto productDto) {
        Product product = new Product();
        productDto.setId(id);
        BeanUtils.copyProperties(productDto, product);
        Boolean flag = null;
        try {
            flag = productMapper.updateProductById(product);
        } catch (Exception e) {
            log.error("更新失败！",e);
            throw new ServiceException("更新失败！", StatusCode.PRODUCT_EDIT_EROR);
        }
        return flag;
    }

    @Override
    public Boolean addProduct(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        Boolean flag = null;
        try {
            flag = productMapper.insertProduct(product);
        } catch (Exception e) {
            log.error("添加失败！",e);
            throw new ServiceException("添加失败！", StatusCode.PRODUCT_ADD_ERROR);
        }
        return flag;
    }

    @Override
    public void reduceStock(Integer productId, Integer quantity) {
        //直接从数据库中查找
        Product product = productMapper.selectById(productId);
        if(product==null){
            log.error("商品不存在！");
            throw new ServiceException("商品不存在！", StatusCode.PRODUCT_NOT_EXIST);
        }
        if(product.getStock()<quantity){
            log.error("商品库存不足！");
            throw new ServiceException("商品库存不足！", StatusCode.PRODUCT_STOCK_NOT_ENOUGH);
        }
        product.setStock(product.getStock()-quantity);
        productMapper.updateProductStockById(product);
        //删除缓存
        String cacheKey = RedisConfig.PRODUCT_INFO_KEY+":"+productId;
        stringRedisTemplate.delete(cacheKey);
    }
}
