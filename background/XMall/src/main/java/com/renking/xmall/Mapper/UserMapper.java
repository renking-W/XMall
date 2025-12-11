package com.renking.xmall.Mapper;

import com.renking.xmall.Entity.Dto.UserDto;
import com.renking.xmall.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface UserMapper {

    User selectByPhoneAndPassword(@Param("phone") String phone,@Param("password") String password);

    int insertUserIfNotExist(User user);

    User selectByUserName(String userName);

    void updateUserInfoAll(User user);

    Boolean updateMoneyByPhone(@Param("phone") String phone,@Param("mon") BigDecimal mon);

    @Select("select id, user_name, phone, password, avatar, gender, money from user where id = #{id}")
    User selectById(Integer id);

    Boolean addMoneyById(@Param("id") Integer id,@Param("mon") BigDecimal mon);

    Boolean reduceMoneyById(@Param("id") Integer id,@Param("mon") BigDecimal mon);
}