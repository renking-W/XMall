package com.renking.xmall.Mapper;

import com.renking.xmall.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where phone=#{phone} and password=#{password}")
    User selectByPhoneAndPassword(@Param("phone") String phone,@Param("password") String password);
}