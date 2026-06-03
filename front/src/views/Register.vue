<template>
  <div class="register-container">
    <div class="register-card">
      <div class="logo">
        <span class="logo-icon">🐾</span>
        <span class="logo-text">宠物万事通</span>
      </div>
      <h2>创建账号</h2>
      <p class="subtitle">注册成为宠物主人</p>
      
      <form @submit.prevent="handleRegister">
        <!-- 角色选择（只读，只能选宠物主人） -->
        <div class="role-select">
          <div class="role-option active">
            <span class="role-icon">🐕</span>
            <span class="role-name">宠物主人</span>
          </div>
          <div class="role-option disabled">
            <span class="role-icon">🤝</span>
            <span class="role-name">护工</span>
            <span class="role-badge">需申请</span>
          </div>
          <div class="role-option disabled">
            <span class="role-icon">🏪</span>
            <span class="role-name">商家</span>
            <span class="role-badge">需申请</span>
          </div>
        </div>
        <p class="role-hint">
          💡 注册后可在“我的”页面申请成为护工或商家
        </p>
        
        <div class="input-group">
          <span class="input-icon">👤</span>
          <input 
            type="text" 
            v-model="registerForm.username" 
            placeholder="用户名"
            required
          >
        </div>
        
        <div class="input-group">
          <span class="input-icon">🔒</span>
          <input 
            :type="showPassword ? 'text' : 'password'" 
            v-model="registerForm.password" 
            placeholder="密码"
            required
          >
          <span class="password-toggle" @click="showPassword = !showPassword">
            {{ showPassword ? '🙈' : '👁️' }}
          </span>
        </div>
        
        <div class="input-group">
          <span class="input-icon">🔒</span>
          <input 
            :type="showConfirmPassword ? 'text' : 'password'" 
            v-model="registerForm.confirmPassword" 
            placeholder="确认密码"
            required
          >
          <span class="password-toggle" @click="showConfirmPassword = !showConfirmPassword">
            {{ showConfirmPassword ? '🙈' : '👁️' }}
          </span>
        </div>
        
        <button type="submit" class="register-btn" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>
      
      <div class="login-link">
        已有账号？ <router-link to="/login">立即登录</router-link>
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

// 角色固定为宠物主人
const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: ''
})
const loading = ref(false)
const showPassword = ref(false)
const showConfirmPassword = ref(false)

const handleRegister = async () => {
  if (!registerForm.value.username) {
    alert('请填写用户名')
    return
  }
  if (!registerForm.value.password) {
    alert('请填写密码')
    return
  }
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }
  if (registerForm.value.password.length < 6) {
    alert('密码长度不能小于6位')
    return
  }
  
  loading.value = true
  
  try {
    // 角色固定为宠物主人
    const success = await userStore.register({
      username: registerForm.value.username,
      password: registerForm.value.password,
      role: 'petOwner',  // 固定为宠物主人
      shopName: '',
      phone: ''
    })
    
    if (success) {
      alert('注册成功，请登录')
      router.push('/login')
    } else {
      alert('用户名已存在')
    }
  } catch (error) {
    alert('注册失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a3c34 0%, #0f2a24 100%);
}

.register-card {
  background: white;
  border-radius: 32px;
  padding: 40px;
  width: 480px;
  max-width: 90%;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
}

.logo {
  text-align: center;
  margin-bottom: 24px;
}

.logo-icon {
  font-size: 40px;
}

.logo-text {
  font-size: 22px;
  font-weight: 600;
  color: #1a3c34;
  margin-left: 8px;
}

h2 {
  font-size: 26px;
  color: #1a3c34;
  margin-bottom: 8px;
  text-align: center;
}

.subtitle {
  color: #666;
  text-align: center;
  margin-bottom: 24px;
}

.role-select {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.role-option {
  flex: 1;
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 16px;
  text-align: center;
  position: relative;
}

.role-option.active {
  border-color: #2ecc71;
  background: #e8f5e9;
}

.role-option.disabled {
  background: #f5f5f5;
  cursor: not-allowed;
  opacity: 0.7;
}

.role-icon {
  font-size: 28px;
  display: block;
  margin-bottom: 6px;
}

.role-name {
  font-size: 13px;
  font-weight: 500;
  color: #333;
}

.role-badge {
  display: inline-block;
  background: #ff9800;
  color: white;
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 10px;
  margin-left: 4px;
}

.role-hint {
  font-size: 12px;
  color: #999;
  text-align: center;
  margin-bottom: 24px;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 8px;
}

.input-group {
  position: relative;
  margin-bottom: 16px;
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
  font-size: 14px;
}

.password-toggle {
  cursor: pointer;
  font-size: 18px;
  color: #999;
}

.register-btn {
  width: 100%;
  padding: 14px;
  background: #2ecc71;
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 16px;
  transition: all 0.2s;
}

.register-btn:hover {
  background: #27ae60;
  transform: translateY(-2px);
}

.register-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
}

.login-link {
  text-align: center;
  margin-top: 24px;
  color: #666;
}

.login-link a {
  color: #2ecc71;
  text-decoration: none;
  font-weight: 500;
}
</style>