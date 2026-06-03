import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, register as registerApi } from '../api/user'

export const useUserStore = defineStore('user', () => {
  // ========== 登录/注册相关 ==========
  const isLoggedIn = ref(false)
  const currentUser = ref(null)
  const currentRole = ref('petOwner')
  
  // ========== 认证申请状态 ==========
  const certificationStatus = ref({
    caregiver: 'none',  // none: 未申请, pending: 审核中, approved: 已通过, rejected: 已驳回
    merchant: 'none'
  })
  
  const roleNameMap = {
    petOwner: '宠物主人',
    caregiver: '护工',
    merchant: '商家',
    admin: '管理员'
  }
  
  // ========== 初始化：从 localStorage 读取登录状态 ==========
  const initAuth = () => {
    const token = localStorage.getItem('token')
    const user = localStorage.getItem('currentUser')
    if (token && user) {
      isLoggedIn.value = true
      currentUser.value = JSON.parse(user)
      currentRole.value = currentUser.value?.role || 'petOwner'
    }
  }
  
  // ========== 登录 ==========
  const login = async (username, password) => {
    try {
      const res = await loginApi({ username, password })
      if (res.code === 200) {
        const user = res.data
        isLoggedIn.value = true
        currentUser.value = user
        currentRole.value = user.role
        
        localStorage.setItem('token', 'mock-token-' + Date.now())
        localStorage.setItem('currentUser', JSON.stringify(user))
        return true
      } else {
        return false
      }
    } catch (error) {
      console.error('登录失败:', error)
      return false
    }
  }
  
  // ========== 注册 ==========
  const register = async (userData) => {
    try {
      const res = await registerApi({
        username: userData.username,
        password: userData.password
      })
      if (res.code === 200) {
        return true
      } else {
        return false
      }
    } catch (error) {
      console.error('注册失败:', error)
      return false
    }
  }
  
  // ========== 登出 ==========
  const logout = () => {
    isLoggedIn.value = false
    currentUser.value = null
    currentRole.value = 'petOwner'
    localStorage.removeItem('token')
    localStorage.removeItem('currentUser')
  }
  
  // ========== 切换角色 ==========
  const switchRole = (role) => {
    console.log('store.switchRole 被调用:', role)
    currentRole.value = role
    localStorage.setItem('userRole', role)
  }
  
  // ========== 初始化角色（兼容旧逻辑） ==========
  const initRole = () => {
    const savedRole = localStorage.getItem('userRole')
    if (savedRole && roleNameMap[savedRole]) {
      currentRole.value = savedRole
    }
  }
  
  // ========== 认证申请相关 ==========
  const submitCertification = (type, data) => {
    certificationStatus.value[type] = 'pending'
    localStorage.setItem(`cert_${type}`, JSON.stringify(data))
    localStorage.setItem(`cert_${type}_status`, 'pending')
  }
  
  const loadCertificationStatus = () => {
    const caregiverStatus = localStorage.getItem('cert_caregiver_status')
    const merchantStatus = localStorage.getItem('cert_merchant_status')
    if (caregiverStatus) certificationStatus.value.caregiver = caregiverStatus
    if (merchantStatus) certificationStatus.value.merchant = merchantStatus
  }
  
  const getCertificationData = (type) => {
    const data = localStorage.getItem(`cert_${type}`)
    return data ? JSON.parse(data) : null
  }
  
  return { 
    // 登录/注册
    isLoggedIn,
    currentUser,
    initAuth,
    login,
    register,
    logout,
    // 角色
    currentRole, 
    roleNameMap, 
    switchRole, 
    initRole,
    // 认证申请
    certificationStatus,
    submitCertification,
    loadCertificationStatus,
    getCertificationData
  }
})