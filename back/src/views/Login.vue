<template>
  <div class="login-container">
    <div class="login-card">
      <div class="logo">
        <span class="logo-icon">🐾</span>
        <span class="logo-text">宠物万事通</span>
      </div>
      <h2>欢迎回来</h2>
      <p class="subtitle">请登录您的账号</p>
      
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <span class="input-icon">👤</span>
          <input 
            type="text" 
            v-model="loginForm.username" 
            placeholder="用户名 / 手机号"
            required
          >
        </div>
        
        <div class="input-group">
          <span class="input-icon">🔒</span>
          <input 
            :type="showPassword ? 'text' : 'password'" 
            v-model="loginForm.password" 
            placeholder="密码"
            required
          >
          <span class="password-toggle" @click="showPassword = !showPassword">
            {{ showPassword ? '🙈' : '👁️' }}
          </span>
        </div>
        
        <button type="submit" class="login-btn" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>
      
      <div class="register-link">
        还没有账号？ <router-link to="/register">立即注册</router-link>
      </div>
      
      <!-- 演示账号提示 -->
      <div class="demo-tip">
        <p>演示账号：</p>
        <div class="demo-accounts">
          <span @click="fillDemo('petOwner')">🐕 宠物主人</span>
          <span @click="fillDemo('caregiver')">🤝 护工</span>
          <span @click="fillDemo('merchant')">🏪 商家</span>
          <span @click="fillDemo('admin')">🔒 管理员</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const loginForm = ref({
  username: '',
  password: ''
})
const loading = ref(false)
const showPassword = ref(false)

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    alert('请填写用户名和密码')
    return
  }
  
  loading.value = true
  
  try {
    const success = await userStore.login(loginForm.value.username, loginForm.value.password)
    if (success) {
      router.push('/')
    } else {
      alert('用户名或密码错误')
    }
  } catch (error) {
    alert('登录失败，请重试')
  } finally {
    loading.value = false
  }
}

// 填充演示账号
const fillDemo = (role) => {
  const demoAccounts = {
    petOwner: { username: 'petOwner', password: '123456' },
    caregiver: { username: 'caregiver', password: '123456' },
    merchant: { username: 'merchant', password: '123456' },
    admin: { username: 'admin', password: '123456' }
  }
  loginForm.value = demoAccounts[role]
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a3c34 0%, #0f2a24 100%);
}

.login-card {
  background: white;
  border-radius: 32px;
  padding: 48px 40px;
  width: 420px;
  max-width: 90%;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
}

.logo {
  text-align: center;
  margin-bottom: 32px;
}

.logo-icon {
  font-size: 48px;
}

.logo-text {
  font-size: 24px;
  font-weight: 600;
  color: #1a3c34;
  margin-left: 8px;
}

h2 {
  font-size: 28px;
  color: #1a3c34;
  margin-bottom: 8px;
  text-align: center;
}

.subtitle {
  color: #666;
  text-align: center;
  margin-bottom: 32px;
}

.input-group {
  position: relative;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 0 16px;
  transition: all 0.2s;
}

.input-group:focus-within {
  border-color: #2ecc71;
  box-shadow: 0 0 0 2px rgba(46, 204, 113, 0.1);
}

.input-icon {
  font-size: 18px;
  color: #999;
  margin-right: 12px;
}

.input-group input {
  flex: 1;
  padding: 14px 0;
  border: none;
  outline: none;
  font-size: 15px;
}

.password-toggle {
  cursor: pointer;
  font-size: 18px;
  color: #999;
}

.login-btn {
  width: 100%;
  padding: 14px;
  background: #2ecc71;
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 8px;
  transition: all 0.2s;
}

.login-btn:hover {
  background: #27ae60;
  transform: translateY(-2px);
}

.login-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
}

.register-link {
  text-align: center;
  margin-top: 24px;
  color: #666;
}

.register-link a {
  color: #2ecc71;
  text-decoration: none;
  font-weight: 500;
}

.demo-tip {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.demo-tip p {
  font-size: 13px;
  color: #999;
  margin-bottom: 12px;
}

.demo-accounts {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.demo-accounts span {
  padding: 6px 16px;
  background: #f0f0f0;
  border-radius: 20px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.demo-accounts span:hover {
  background: #2ecc71;
  color: white;
}
</style>