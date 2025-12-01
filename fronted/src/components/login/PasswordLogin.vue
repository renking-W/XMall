<script setup>
import { ref, reactive } from 'vue'

// 定义事件发射
const emit = defineEmits(['loginSuccess'])

// 表单数据
const form = reactive({
  phone: '',
  password: ''
})

// 表单验证错误信息
const errors = reactive({
  phone: '',
  password: ''
})

// 表单验证
const validateForm = () => {
  let isValid = true
  
  // 验证手机号
  if (!form.phone) {
    errors.phone = '请输入手机号'
    isValid = false
  } else if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    errors.phone = '手机号格式不正确'
    isValid = false
  } else {
    errors.phone = ''
  }
  
  // 验证密码
  if (!form.password) {
    errors.password = '请输入密码'
    isValid = false
  } else if (form.password.length < 6) {
    errors.password = '密码长度不能少于6位'
    isValid = false
  } else {
    errors.password = ''
  }
  
  return isValid
}

// 登录处理
const handleLogin = () => {
  if (!validateForm()) {
    return
  }
  
  // 模拟登录请求
  console.log('账号密码登录:', form)
  
  // 模拟后端响应
  // 这里模拟两种情况：
  // 1. 首次登录用户（没有设置密码）
  // 2. 已有账户用户
  // 对于账号密码登录，我们假设用户已经设置过密码
  const isFirstLogin = false
  
  // 发射登录成功事件
  emit('loginSuccess', {
    phone: form.phone,
    isFirstLogin: isFirstLogin
  })
}
</script>

<template>
  <div class="password-login">
    <div class="form-group">
      <input 
        v-model="form.phone"
        type="tel"
        placeholder="请输入手机号"
        class="form-input"
        :class="{ error: errors.phone }"
      />
      <div v-if="errors.phone" class="error-message">{{ errors.phone }}</div>
    </div>
    
    <div class="form-group">
      <input 
        v-model="form.password"
        type="password"
        placeholder="请输入密码"
        class="form-input"
        :class="{ error: errors.password }"
      />
      <div v-if="errors.password" class="error-message">{{ errors.password }}</div>
    </div>
    
    <button class="login-button" @click="handleLogin">
      登录
    </button>
    
    <div class="login-footer">
      <p>首次登录请使用短信验证码登录</p>
    </div>
  </div>
</template>

<style scoped>
.password-login {
  padding: 20px 0;
}

.form-group {
  margin-bottom: 20px;
}

.form-input {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
  transition: border-color 0.3s;
}

.form-input:focus {
  border-color: #409eff;
  outline: none;
}

.form-input.error {
  border-color: #f56565;
}

.error-message {
  color: #f56565;
  font-size: 12px;
  margin-top: 5px;
}

.login-button {
  width: 100%;
  padding: 12px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
  margin-bottom: 20px;
}

.login-button:hover {
  background: #66b1ff;
}

.login-button:active {
  background: #337ecc;
}

.login-footer {
  text-align: center;
}

.login-footer p {
  color: #999;
  font-size: 12px;
  margin: 0;
}
</style>