<script setup>
import { ref, reactive } from 'vue'

// 定义事件发射
const emit = defineEmits(['loginSuccess'])

// 表单数据
const form = reactive({
  phone: '',
  smsCode: ''
})

// 表单验证错误信息
const errors = reactive({
  phone: '',
  smsCode: ''
})

// 验证码按钮状态
const smsButton = reactive({
  text: '获取验证码',
  disabled: false,
  countdown: 60
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
  
  // 验证验证码
  if (!form.smsCode) {
    errors.smsCode = '请输入验证码'
    isValid = false
  } else if (!/^\d{6}$/.test(form.smsCode)) {
    errors.smsCode = '验证码为6位数字'
    isValid = false
  } else {
    errors.smsCode = ''
  }
  
  return isValid
}

// 获取验证码
const getSmsCode = () => {
  // 验证手机号
  if (!form.phone) {
    errors.phone = '请输入手机号'
    return
  }
  
  if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    errors.phone = '手机号格式不正确'
    return
  }
  
  errors.phone = ''
  
  // 模拟发送验证码
  console.log('发送验证码到:', form.phone)
  
  // 启动倒计时
  smsButton.disabled = true
  smsButton.countdown = 60
  smsButton.text = `${smsButton.countdown}秒后重新获取`
  
  const timer = setInterval(() => {
    smsButton.countdown--
    smsButton.text = `${smsButton.countdown}秒后重新获取`
    
    if (smsButton.countdown <= 0) {
      clearInterval(timer)
      smsButton.disabled = false
      smsButton.text = '获取验证码'
    }
  }, 1000)
}

// 登录处理
const handleLogin = () => {
  if (!validateForm()) {
    return
  }
  
  // 模拟登录请求
  console.log('短信验证码登录:', form)
  
  // 模拟后端响应
  // 这里模拟两种情况：
  // 1. 首次登录用户
  // 2. 已有账户用户
  const isFirstLogin = Math.random() > 0.5 // 随机模拟是否首次登录
  
  // 发射登录成功事件
  emit('loginSuccess', {
    phone: form.phone,
    isFirstLogin: isFirstLogin
  })
}
</script>

<template>
  <div class="sms-login">
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
    
    <div class="form-group sms-group">
      <input 
        v-model="form.smsCode"
        type="text"
        placeholder="请输入验证码"
        class="form-input sms-input"
        :class="{ error: errors.smsCode }"
        maxlength="6"
      />
      <button 
        class="sms-button"
        :class="{ disabled: smsButton.disabled }"
        @click="getSmsCode"
        :disabled="smsButton.disabled"
      >
        {{ smsButton.text }}
      </button>
      <div v-if="errors.smsCode" class="error-message">{{ errors.smsCode }}</div>
    </div>
    
    <button class="login-button" @click="handleLogin">
      登录
    </button>
  </div>
</template>

<style scoped>
.sms-login {
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

.sms-group {
  position: relative;
}

.sms-input {
  padding-right: 120px;
}

.sms-button {
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  padding: 0 15px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  font-size: 12px;
}

.sms-button.disabled {
  background: #a0cfff;
  cursor: not-allowed;
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
}

.login-button:hover {
  background: #66b1ff;
}

.login-button:active {
  background: #337ecc;
}
</style>