<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { getVerificationCode, loginWithCode } from '../../services/api'

const router = useRouter()
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

// 加载状态
const loading = ref(false)

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
const getSmsCode = async () => {
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
  
  try {
    smsButton.disabled = true
    const response = await getVerificationCode(form.phone)
    
    if (response.code === 20000) {
      // 验证码发送成功，启动倒计时
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
      
      // 开发环境：提示验证码
      console.log('验证码：' + response.message)
    } else {
      smsButton.disabled = false
      errors.phone = response.message || '获取验证码失败'
    }
  } catch (error) {
    console.error('获取验证码失败:', error)
    smsButton.disabled = false
    errors.phone = '获取验证码失败，请检查网络连接'
  }
}

// 登录处理
const handleLogin = async () => {
  if (!validateForm()) {
    return
  }
  
  try {
    loading.value = true
    const response = await loginWithCode(form.phone, form.smsCode)
    
    if (response.code === 20000) {
      // 保存登录信息到本地存储
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('userName', response.data.userName)
      localStorage.setItem('phone', response.data.phone)
      localStorage.setItem('userId', response.data.id)
      // 短信验证码登录是新用户登录（没有密码）
      localStorage.setItem('isNewUser', 'true')
      
      // 发射登录成功事件
      emit('loginSuccess', {
        phone: form.phone,
        isFirstLogin: false
      })
      
      // 跳转到用户详情页
      setTimeout(() => {
        router.push('/user-detail')
      }, 500)
    } else {
      errors.smsCode = response.message || '登录失败'
    }
  } catch (error) {
    console.error('登录失败:', error)
    errors.smsCode = '登录失败，请检查验证码是否正确'
  } finally {
    loading.value = false
  }
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
    
    <button class="login-button" @click="handleLogin" :disabled="loading">
      {{ loading ? '登录中...' : '登录' }}
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