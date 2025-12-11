package com.renking.xmall.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.RedisConfig;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.UserDto;
import com.renking.xmall.Entity.User;
import com.renking.xmall.Mapper.UserMapper;
import com.renking.xmall.Service.UserService;
import com.renking.xmall.Utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {


    private final StringRedisTemplate stringRedisTemplate;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    @Override
    public UserDto login(String phone, String password) {
        User user = userMapper.selectByPhoneAndPassword(phone, password);
        //用户不存在抛出异常
        if(user==null) throw new ServiceException("用户不存在或密码错误！", StatusCode.USER_NOT_EXIST);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);
        //生成token
        String token = TokenUtil.getToken(user);
        //存入redis 过期时间8小时
        stringRedisTemplate.opsForValue().set(RedisConfig.USER_TOKEN_KEY+":"+phone, token,RedisConfig.USER_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        userDto.setToken(token);
        log.info("{}:token生成成功,token:{}",phone,token);
        return userDto;
    }

    @Override
    public String getCode(String phone) {
        //生成6位随机数
        String code = String.valueOf(100000 + new Random().nextInt(900000));
        //存入redis
        stringRedisTemplate.opsForValue().set(RedisConfig.USER_CODE_KEY+":"+phone, code,RedisConfig.USER_CODE_EXPIRE_TIME,TimeUnit.SECONDS);
        log.info("{}:验证码生成成功,验证码:{}",phone,code);
        return code;
    }

    @Override
    public UserDto register(UserDto userDto) {
        //获取验证码
        String code = stringRedisTemplate.opsForValue().get(RedisConfig.USER_CODE_KEY + ":" + userDto.getPhone());
        log.info("{}:验证码:{}",userDto.getPhone(),code);
        if(code==null||!code.equals(userDto.getCode())) throw new ServiceException("无效的验证码", StatusCode.CODE_UNVALID);
        //插入数据
        userDto.setUserName(userDto.getPhone());
        try {
            User user = new User();
            BeanUtils.copyProperties(userDto,user);
            userMapper.insertUserIfNotExist(user);
        } catch (Exception e) {
            log.info("{}:插入数据失败!",userDto.getUserName(),e);
            throw new ServiceException("插入数据失败！");
        }
        //生成token
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        String token = TokenUtil.getToken(user);
        userDto.setToken(token);
        //存入redis
        stringRedisTemplate.opsForValue().set(RedisConfig.USER_TOKEN_KEY+":"+userDto.getPhone(), token,RedisConfig.USER_TOKEN_EXPIRE_TIME,TimeUnit.SECONDS);
        log.info("{}:注册成功,token:{}",userDto.getPhone(),token);
        return userDto;
    }

    @Override
    public UserDto getInfo(UserDto userInfo) {
        UserDto userDto = new UserDto();
        String tar = stringRedisTemplate.opsForValue().get(RedisConfig.USER_INFO_KEY + ":" + userInfo.getId());
        //未命中查询数据库
        if(tar== null){
            User user = userMapper.selectById(userInfo.getId());
            BeanUtils.copyProperties(user,userDto);
            //存入缓存
            try {

                stringRedisTemplate.opsForValue().set(RedisConfig.USER_INFO_KEY+":"+user.getId(), objectMapper.writeValueAsString(userDto),RedisConfig.USER_INFO_EXPIRE_TIME,TimeUnit.SECONDS);
            } catch (JsonProcessingException e) {
                log.error("{}:数据解析失败!",userInfo.getPhone(),e);
                throw new ServiceException("数据解析失败！", StatusCode.DATA_PARSE_ERROR);
            }
        }
        else{
            //查询缓存
            try {
                userDto = objectMapper.readValue(tar, UserDto.class);
            } catch (JsonProcessingException e) {
                log.error("{}:数据解析失败!",userInfo.getUserName(),e);
                throw new ServiceException("数据解析失败！", StatusCode.DATA_PARSE_ERROR);
            }
        }
        //命中直接返回
        return userDto;
    }


    @Override
    public Boolean updateUserInfo(UserDto userDto) {
        UserDto info = getInfo(userDto);
        //验证新旧密码是否相同
        String oldPassword = info.getPassword();
        userDto.setId(info.getId());
        if( oldPassword!=null && !oldPassword.equals(userDto.getOldPassword())){
            throw new ServiceException("密码不一致或密码错误！", StatusCode.USER_PASSWORD_ERROR);
        }else{
            try {
                User user = new User();
                BeanUtils.copyProperties(userDto,user);
                userMapper.updateUserInfoAll(user);
                //更新缓存
                stringRedisTemplate.delete(RedisConfig.USER_INFO_KEY+":"+userDto.getId());
            } catch (Exception e) {
                log.error("{}:更新数据失败!",userDto.getUserName(),e);
                throw new ServiceException("更新数据失败！");
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean recharge(Integer id, BigDecimal mon) {
        //获取缓存key
        String key = RedisConfig.USER_INFO_KEY + ":" + id;
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        Boolean flag = userMapper.addMoneyById(id,mon);
        if(flag.equals(Boolean.FALSE)) throw new ServiceException("充值失败！", StatusCode.USER_RECHARGE_ERROR);
        if(jsonStr!=null && !jsonStr.equals("null")){
            stringRedisTemplate.delete(key);
        }
        return flag;
    }

    @Override
    public void reduceMoney(Integer userId, BigDecimal tar) {
        userMapper.reduceMoneyById(userId,tar);
    }
}
