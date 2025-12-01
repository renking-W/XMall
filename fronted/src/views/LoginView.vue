<script setup>
import { ref, reactive } from 'vue'
import PasswordLogin from '../components/login/PasswordLogin.vue'
import SmsLogin from '../components/login/SmsLogin.vue'
import SetPassword from '../components/login/SetPassword.vue'

// 登录方式切换：'sms' 为短信验证码登录，'password' 为账号密码登录
const loginType = ref('sms')

// 控制是否显示设置密码界面
const showSetPassword = ref(false)

// 用户数据（模拟）
const userData = reactive({
  phone: '',
  isFirstLogin: false
})

// 切换登录方式
const switchLoginType = (type) => {
  loginType.value = type
}

// 处理登录成功事件
const handleLoginSuccess = (data) => {
  userData.phone = data.phone
  userData.isFirstLogin = data.isFirstLogin
  
  // 如果是首次登录，显示设置密码界面
  if (data.isFirstLogin) {
    showSetPassword.value = true
  } else {
    // 正常登录流程
    console.log('登录成功:', data)
    // 这里可以跳转到首页或其他页面
  }
}

// 处理设置密码完成事件
const handleSetPasswordComplete = () => {
  showSetPassword.value = false
  // 密码设置完成，可以跳转到首页
  console.log('密码设置完成，跳转到首页')
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>欢迎登录 XMall</h1>
        <p>请选择登录方式</p>
      </div>
      
      <!-- 登录方式切换 tabs -->
      <div class="login-tabs">
        <button 
          :class="{ active: loginType === 'sms' }" 
          @click="switchLoginType('sms')"
        >
          短信验证码登录
        </button>
        <button 
          :class="{ active: loginType === 'password' }" 
          @click="switchLoginType('password')"
        >
          账号密码登录
        </button>
      </div>
      
      <!-- 登录表单 -->
      <div class="login-form">
        <SmsLogin 
          v-if="loginType === 'sms'" 
          @login-success="handleLoginSuccess"
        />
        <PasswordLogin 
          v-if="loginType === 'password'" 
          @login-success="handleLoginSuccess"
        />
      </div>
      
      <!-- 设置密码界面（首次登录时显示） -->
      <SetPassword 
        v-if="showSetPassword" 
        :phone="userData.phone"
        @complete="handleSetPasswordComplete"
      />
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.login-box {
  width: 100%;
  max-width: 400px;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.login-header p {
  color: #666;
}

.login-tabs {
  display: flex;
  border-bottom: 1px solid #eee;
  margin-bottom: 30px;
}

.login-tabs button {
  flex: 1;
  padding: 12px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  position: relative;
}

.login-tabs button.active {
  color: #409eff;
  font-weight: 500;
}

.login-tabs button.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: #409eff;
}

.login-form {
  min-height: 250px;
}
</style>