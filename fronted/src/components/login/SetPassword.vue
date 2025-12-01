<script setup>
import { ref, reactive } from 'vue'

// 定义props
const props = defineProps({
  phone: {
    type: String,
    required: true
  }
})

// 定义事件发射
const emit = defineEmits(['complete'])

// 表单数据
const form = reactive({
  password: '',
  confirmPassword: ''
})

// 表单验证错误信息
const errors = reactive({
  password: '',
  confirmPassword: ''
})

// 表单验证
const validateForm = () => {
  let isValid = true
  
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
  
  // 验证确认密码
  if (!form.confirmPassword) {
    errors.confirmPassword = '请再次输入密码'
    isValid = false
  } else if (form.confirmPassword !== form.password) {
    errors.confirmPassword = '两次输入的密码不一致'
    isValid = false
  } else {
    errors.confirmPassword = ''
  }
  
  return isValid
}

// 设置密码处理
const handleSetPassword = () => {
  if (!validateForm()) {
    return
  }
  
  // 模拟设置密码请求
  console.log('设置密码:', {
    phone: props.phone,
    password: form.password
  })
  
  // 发射完成事件
  emit('complete')
}
</script>

<template>
  <div class="set-password-overlay">
    <div class="set-password-modal">
      <div class="modal-header">
        <h2>设置密码</h2>
        <p>首次登录，请设置您的登录密码</p>
      </div>
      
      <div class="form-group">
        <input 
          v-model="form.password"
          type="password"
          placeholder="请输入密码（至少6位）"
          class="form-input"
          :class="{ error: errors.password }"
        />
        <div v-if="errors.password" class="error-message">{{ errors.password }}</div>
      </div>
      
      <div class="form-group">
        <input 
          v-model="form.confirmPassword"
          type="password"
          placeholder="请再次输入密码"
          class="form-input"
          :class="{ error: errors.confirmPassword }"
        />
        <div v-if="errors.confirmPassword" class="error-message">{{ errors.confirmPassword }}</div>
      </div>
      
      <button class="set-password-button" @click="handleSetPassword">
        确认设置
      </button>
    </div>
  </div>
</template>

<style scoped>
.set-password-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.set-password-modal {
  width: 100%;
  max-width: 400px;
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.modal-header {
  text-align: center;
  margin-bottom: 30px;
}

.modal-header h2 {
  font-size: 20px;
  color: #333;
  margin-bottom: 10px;
}

.modal-header p {
  color: #666;
  font-size: 14px;
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

.set-password-button {
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

.set-password-button:hover {
  background: #66b1ff;
}

.set-password-button:active {
  background: #337ecc;
}
</style>