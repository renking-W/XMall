// API 配置
const API_BASE_URL = 'http://127.0.0.1:8080'

// \u901a\u7528\u8bf7\u6c42\u65b9\u6cd5
async function request(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers
  }
  
  // \u6dfb\u52a0 token \u5230\u8bf7\u6c42\u5934
  const token = localStorage.getItem('token')
  if (token) {
    headers['token'] = token
  }
  
  try {
    const response = await fetch(url, {
      method: options.method || 'GET',
      headers: headers,
      body: options.body ? JSON.stringify(options.body) : undefined
    })
    
    // 检查是否成功
    if (!response.ok) {
      const errorData = await response.json()
      throw new Error(errorData.message || `HTTP ${response.status}`)
    }
    
    return await response.json()
  } catch (error) {
    console.error(`API Error: ${endpoint}`, error)
    throw error
  }
}

// 密码登录接口
export async function loginWithPassword(phone, password) {
  return request('/user/login/password', {
    method: 'POST',
    body: {
      phone: phone,
      password: password
    }
  })
}

// 获取验证码接口
export async function getVerificationCode(phone) {
  return request(`/user/code/${phone}`, {
    method: 'GET'
  })
}

// 验证码登录接口
export async function loginWithCode(phone, code) {
  return request('/user/login/code', {
    method: 'POST',
    body: {
      phone: phone,
      code: code
    }
  })
}

// 获取用户信息接口
export async function getUserInfo(userData) {
  return request('/user/info', {
    method: 'POST',
    body: userData
  })
}

// 更新用户信息接口
export async function updateUserInfo(userData) {
  return request('/user/update', {
    method: 'POST',
    body: userData
  })
}
