<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getUserInfo, updateUserInfo } from '../services/api'

const router = useRouter()

// 用户信息
const user = ref({
  id: '',
  userName: '',
  phone: '',
  password: '',
  oldPassword: ''
})

// 标记用户是否有密码（新用户没有密码）
const hasPassword = ref(true)

// 编辑状态
const isEditing = ref(false)
const editForm = ref({
  userName: '',
  phone: '',
  oldPassword: '',
  password: '',
  confirmPassword: ''
})

// 消息提示
const message = ref({
  type: '',
  text: '',
  show: false
})

// 加载状态
const loading = ref(true)

// 页面挂载时获取用户信息
onMounted(async () => {
  await loadUserInfo()
})

// 获取用户信息
const loadUserInfo = async () => {
  try {
    loading.value = true
    const token = localStorage.getItem('token')
    const userName = localStorage.getItem('userName')
    const phone = localStorage.getItem('phone')
    const isNewUser = localStorage.getItem('isNewUser') === 'true'
    
    if (!token || !userName) {
      showMessage('error', '未登录或信息缺失')
      setTimeout(() => {
        router.push('/login')
      }, 2000)
      return
    }
    
    // 根据是否是新用户来设置 hasPassword
    hasPassword.value = !isNewUser
    
    const response = await getUserInfo({
      userName: userName,
      phone: phone
    })
    
    if (response.code === 20000) {
      user.value = response.data
      loading.value = false
    } else {
      showMessage('error', response.message || '获取用户信息失败')
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    showMessage('error', '获取用户信息失败，请检查网络连接')
    setTimeout(() => {
      router.push('/login')
    }, 2000)
  }
}

// 开始编辑
const startEdit = () => {
  editForm.value = {
    userName: user.value.userName,
    phone: user.value.phone,
    oldPassword: '',
    password: '',
    confirmPassword: ''
  }
  isEditing.value = true
}

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false
  editForm.value = {
    userName: '',
    phone: '',
    oldPassword: '',
    password: '',
    confirmPassword: ''
  }
}

// 验证编辑表单
const validateEditForm = () => {
  // 如果用户有密码，则需要验证旧密码
  if (hasPassword.value) {
    if (!editForm.value.oldPassword) {
      showMessage('error', '请输入旧密码')
      return false
    }
  }
  
  // 如果输入了新密码，需要验证
  if (editForm.value.password) {
    if (editForm.value.password.length < 6) {
      showMessage('error', '新密码长度不能少于6位')
      return false
    }
    
    if (!editForm.value.confirmPassword) {
      showMessage('error', '请确认新密码')
      return false
    }
    
    if (editForm.value.password !== editForm.value.confirmPassword) {
      showMessage('error', '两次输入的密码不一致')
      return false
    }
  }
  
  return true
}

// 提交编辑
const submitEdit = async () => {
  if (!validateEditForm()) {
    return
  }
  
  try {
    loading.value = true
    
    const updateData = {
      userName: editForm.value.userName,
      phone: editForm.value.phone,
      oldPassword: editForm.value.oldPassword
    }
    
    // 只有当输入了新密码时才更新
    if (editForm.value.password) {
      updateData.password = editForm.value.password
    }
    
    const response = await updateUserInfo(updateData)
    
    if (response.code === 20000) {
      user.value = {
        ...user.value,
        userName: editForm.value.userName
      }
      isEditing.value = false
      showMessage('success', '用户信息更新成功')
      loading.value = false
    } else {
      showMessage('error', response.message || '更新失败')
      loading.value = false
    }
  } catch (error) {
    console.error('更新用户信息失败:', error)
    showMessage('error', '更新失败，请检查网络连接')
    loading.value = false
  }
}

// 退出登录
const logout = () => {
  if (confirm('确定要退出登录吗？')) {
    localStorage.removeItem('token')
    localStorage.removeItem('userName')
    localStorage.removeItem('phone')
    router.push('/login')
  }
}

// 显示消息
const showMessage = (type, text) => {
  message.value = {
    type: type,
    text: text,
    show: true
  }
  
  setTimeout(() => {
    message.value.show = false
  }, 3000)
}
</script>

<template>
  <div class="user-detail-container">
    <!-- 顶部导航栏 -->
    <div class="navbar">
      <h1>用户详情</h1>
      <button class="logout-btn" @click="logout">退出登录</button>
    </div>
    
    <!-- 消息提示 -->
    <div v-if="message.show" :class="['message', message.type]">
      {{ message.text }}
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">加载中...</div>
    
    <!-- 用户信息展示和编辑 -->
    <div v-else class="user-detail-box">
      <!-- 显示模式 -->
      <div v-if="!isEditing" class="view-mode">
        <div class="user-info-item">
          <span class="label">用户名:</span>
          <span class="value">{{ user.userName }}</span>
        </div>
        
        <div class="user-info-item">
          <span class="label">手机号:</span>
          <span class="value">{{ user.phone }}</span>
        </div>
        
        <div class="user-info-item">
          <span class="label">账户状态:</span>
          <span class="value success">已激活</span>
        </div>
        
        <button class="edit-btn" @click="startEdit">编辑信息</button>
      </div>
      
      <!-- 编辑模式 -->
      <div v-else class="edit-mode">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="editForm.userName" type="text" placeholder="请输入用户名" />
        </div>
        
        <div class="form-group">
          <label>手机号</label>
          <input v-model="editForm.phone" type="tel" placeholder="请输入手机号" disabled />
        </div>
        
        <div class="form-group">
          <label>旧密码 <span class="required">*</span> <span v-if="!hasPassword" class="hint">（首次设置密码，不需要输入）</span></label>
          <input 
            v-if="hasPassword"
            v-model="editForm.oldPassword" 
            type="password" 
            placeholder="请输入旧密码"
          />
          <div v-else class="no-password-hint">首次设置密码，无需验证旧密码</div>
        </div>
        
        <div class="form-group">
          <label>新密码 <span class="hint">（不修改可留空）</span></label>
          <input 
            v-model="editForm.password" 
            type="password" 
            placeholder="请输入新密码（至少6位）"
          />
        </div>
        
        <div class="form-group">
          <label>确认新密码</label>
          <input 
            v-model="editForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入新密码"
          />
        </div>
        
        <div class="button-group">
          <button class="submit-btn" @click="submitEdit">提交修改</button>
          <button class="cancel-btn" @click="cancelEdit">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.user-detail-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 600px;
  margin: 0 auto 30px;
  color: white;
}

.navbar h1 {
  font-size: 28px;
  margin: 0;
}

.logout-btn {
  padding: 8px 20px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  border-color: white;
}

.message {
  max-width: 600px;
  margin: 0 auto 20px;
  padding: 15px;
  border-radius: 4px;
  text-align: center;
  font-size: 14px;
  animation: slideDown 0.3s ease-out;
}

.message.success {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.message.error {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

@keyframes slideDown {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.loading {
  text-align: center;
  color: white;
  font-size: 18px;
  padding: 50px;
}

.user-detail-box {
  max-width: 600px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  padding: 40px;
}

.view-mode {
  animation: fadeIn 0.3s ease-out;
}

.user-info-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.user-info-item:last-of-type {
  border-bottom: none;
}

.user-info-item .label {
  flex: 0 0 120px;
  font-weight: 500;
  color: #333;
}

.user-info-item .value {
  flex: 1;
  color: #666;
  word-break: break-all;
}

.user-info-item .value.success {
  color: #28a745;
  font-weight: 500;
}

.edit-btn {
  width: 100%;
  margin-top: 30px;
  padding: 12px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}

.edit-btn:hover {
  background: #66b1ff;
}

.edit-mode {
  animation: fadeIn 0.3s ease-out;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.form-group .required {
  color: #f56565;
}

.form-group .hint {
  color: #999;
  font-size: 12px;
  font-weight: normal;
}

.form-group input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
  transition: border-color 0.3s;
}

.form-group input:focus {
  border-color: #409eff;
  outline: none;
}

.form-group input:disabled {
  background: #f5f5f5;
  color: #999;
  cursor: not-allowed;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-top: 30px;
}

.submit-btn,
.cancel-btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.submit-btn {
  background: #409eff;
  color: white;
}

.submit-btn:hover {
  background: #66b1ff;
}

.submit-btn:active {
  background: #337ecc;
}

.cancel-btn {
  background: #f5f5f5;
  color: #666;
  border: 1px solid #ddd;
}

.cancel-btn:hover {
  background: #e8e8e8;
}

.no-password-hint {
  padding: 10px 12px;
  background: #f0f9ff;
  border: 1px solid #b3d8ff;
  border-radius: 4px;
  color: #0050b3;
  font-size: 14px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
