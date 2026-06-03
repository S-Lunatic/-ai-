<template>
  <div class="user-center">
    <div class="user-header">
      <div class="avatar">👤</div>
      <div class="user-info">
        <div class="user-name">张三</div>
        <div class="user-role">
          当前角色：{{ userStore.roleNameMap[userStore.currentRole] }}
          <span class="switch-role" @click="showRolePanel = true">[切换身份 ▼]</span>
        </div>
      </div>
    </div>
    
    <!-- 宠物主人菜单 -->
    <div class="menu-list" v-if="userStore.currentRole === 'petOwner'">
      <div class="menu-item" @click="goTo('pets')">
        <span class="menu-icon">🐾</span>
        <span class="menu-text">我的宠物</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="goTo('orders')">
        <span class="menu-icon">📦</span>
        <span class="menu-text">我的订单</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="goTo('myTasks')">
        <span class="menu-icon">📋</span>
        <span class="menu-text">我发布的任务</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="goTo('myVideos')">
        <span class="menu-icon">🎬</span>
        <span class="menu-text">我的视频</span>
        <span class="menu-arrow">›</span>
      </div>
      
      <div class="menu-divider"></div>
      <div class="menu-item" @click="showCertificationModal = true; certType = 'caregiver'">
        <span class="menu-icon">📝</span>
        <span class="menu-text">申请成为护工</span>
        <span class="menu-badge" v-if="userStore.certificationStatus.caregiver === 'pending'">审核中</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="showCertificationModal = true; certType = 'merchant'">
        <span class="menu-icon">🏪</span>
        <span class="menu-text">申请成为商家</span>
        <span class="menu-badge" v-if="userStore.certificationStatus.merchant === 'pending'">审核中</span>
        <span class="menu-arrow">›</span>
      </div>
    </div>
    
    <!-- 护工菜单 -->
    <div class="menu-list" v-if="userStore.currentRole === 'caregiver'">
      <div class="menu-item" @click="goTo('availableTasks')">
        <span class="menu-icon">📋</span>
        <span class="menu-text">可接任务</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="goTo('myWork')">
        <span class="menu-icon">✅</span>
        <span class="menu-text">我的接单记录</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="goTo('earnings')">
        <span class="menu-icon">💰</span>
        <span class="menu-text">收益提现</span>
        <span class="menu-arrow">›</span>
      </div>
      <!-- <div class="menu-item" @click="goTo('ratings')">
        <span class="menu-icon">⭐</span>
        <span class="menu-text">评价管理</span>
        <span class="menu-arrow">›</span>
      </div> -->
    </div>
    
    <!-- 商家菜单 -->
    <div class="menu-list" v-if="userStore.currentRole === 'merchant'">
      <div class="menu-item" @click="goTo('products')">
        <span class="menu-icon">🛒</span>
        <span class="menu-text">商品管理</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="goTo('appointments')">
        <span class="menu-icon">📅</span>
        <span class="menu-text">预约处理</span>
        <span class="menu-arrow">›</span>
      </div>
      <!-- <div class="menu-item" @click="goTo('employees')">
        <span class="menu-icon">👥</span>
        <span class="menu-text">员工管理</span>
        <span class="menu-arrow">›</span>
      </div> -->
      <div class="menu-item" @click="goTo('statistics')">
        <span class="menu-icon">📊</span>
        <span class="menu-text">营业数据</span>
        <span class="menu-arrow">›</span>
      </div>
    </div>
    
    <!-- 管理员菜单 -->
    <div class="menu-list" v-if="userStore.currentRole === 'admin'">
      <div class="menu-item" @click="goTo('videoAudit')">
        <span class="menu-icon">🎬</span>
        <span class="menu-text">视频审核</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="goTo('qualificationAudit')">
        <span class="menu-icon">✅</span>
        <span class="menu-text">资质审核</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="goTo('dispute')">
        <span class="menu-icon">⚖️</span>
        <span class="menu-text">纠纷仲裁</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="goTo('feedback')">
        <span class="menu-icon">💬</span>
        <span class="menu-text">用户反馈</span>
        <span class="menu-arrow">›</span>
      </div>
    </div>
    
    <!-- 帮助反馈 & 退出登录 -->
    <div class="menu-list">
      <div class="menu-item" @click="showFeedbackModal = true">
        <span class="menu-icon">💬</span>
        <span class="menu-text">帮助与反馈</span>
        <span class="menu-arrow">›</span>
      </div>
      <div class="menu-item" @click="handleLogout">
        <span class="menu-icon">🚪</span>
        <span class="menu-text">退出登录</span>
        <span class="menu-arrow">›</span>
      </div>
    </div>
    
    <!-- 角色切换弹窗 -->
    <div v-if="showRolePanel" class="role-modal" @click.self="showRolePanel = false">
      <div class="role-modal-content">
        <div class="role-option" @click="handleSwitchRole('petOwner')">🐕 宠物主人</div>
        <div class="role-option" @click="handleSwitchRole('caregiver')">🤝 护工</div>
        <div class="role-option" @click="handleSwitchRole('merchant')">🏪 商家</div>
        <div class="role-option" @click="handleSwitchRole('admin')">🔒 管理员</div>
      </div>
    </div>
    
    <!-- 认证申请模态框 -->
    <div v-if="showCertificationModal" class="modal" @click.self="showCertificationModal = false">
      <div class="modal-content">
        <h3>{{ certType === 'caregiver' ? '申请成为护工' : '申请成为商家' }}</h3>
        <div class="cert-type-select">
          <button class="type-btn" :class="{ active: certType === 'caregiver' }" @click="certType = 'caregiver'">护工</button>
          <button class="type-btn" :class="{ active: certType === 'merchant' }" @click="certType = 'merchant'">商家</button>
        </div>
        
        <!-- 护工表单 -->
        <template v-if="certType === 'caregiver'">
          <input type="text" v-model="certForm.name" placeholder="真实姓名" class="modal-input">
          <input type="text" v-model="certForm.idCard" placeholder="身份证号" class="modal-input">
          <textarea v-model="certForm.experience" placeholder="宠物经验介绍" class="modal-textarea"></textarea>
        </template>
        
        <!-- 商家表单 -->
        <template v-else>
          <input type="text" v-model="certForm.shopName" placeholder="店铺名称" class="modal-input">
          <input type="text" v-model="certForm.license" placeholder="营业执照号" class="modal-input">
          <input type="text" v-model="certForm.address" placeholder="店铺地址" class="modal-input">
          <input type="text" v-model="certForm.phone" placeholder="联系电话" class="modal-input">
        </template>
        
        <div class="modal-actions">
          <button class="btn-cancel" @click="showCertificationModal = false">取消</button>
          <button class="btn-submit" @click="submitCertification">提交申请</button>
        </div>
      </div>
    </div>
    
    <!-- 反馈模态框 -->
    <div v-if="showFeedbackModal" class="modal" @click.self="showFeedbackModal = false">
      <div class="modal-content">
        <h3>用户反馈</h3>
        <textarea v-model="feedbackForm.content" placeholder="请描述您的问题或建议..." class="modal-textarea"></textarea>
        <div class="modal-actions">
          <button class="btn-cancel" @click="showFeedbackModal = false">取消</button>
          <button class="btn-submit" @click="submitFeedback">提交反馈</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { submitCertification as submitCertificationApi } from '../api/certification'
import { submitFeedback as submitFeedbackApi } from '../api/feedback'

const router = useRouter()
const userStore = useUserStore()
const showRolePanel = ref(false)

// 处理角色切换
const handleSwitchRole = (role) => {
  console.log('切换到角色:', role)
  userStore.switchRole(role)
  showRolePanel.value = false
}

// 认证申请相关
const showCertificationModal = ref(false)
const certType = ref('caregiver')
const certForm = ref({
  name: '', idCard: '', experience: '',
  shopName: '', license: '', address: '', phone: ''
})

// 提交认证申请
const submitCertification = async () => {
  if (certType.value === 'caregiver') {
    if (!certForm.value.name || !certForm.value.idCard) {
      alert('请填写完整信息')
      return
    }
    try {
      const res = await submitCertificationApi({
        userId: userStore.currentUser?.id,
        type: 'caregiver',
        realName: certForm.value.name,
        idCard: certForm.value.idCard,
        experience: certForm.value.experience
      })
      if (res.code === 200) {
        alert('护工认证申请已提交，等待管理员审核')
        showCertificationModal.value = false
      } else {
        alert('提交失败')
      }
    } catch (error) {
      console.error('提交失败:', error)
      alert('提交失败')
    }
  } else {
    if (!certForm.value.shopName || !certForm.value.license) {
      alert('请填写完整信息')
      return
    }
    try {
      const res = await submitCertificationApi({
        userId: userStore.currentUser?.id,
        type: 'merchant',
        shopName: certForm.value.shopName,
        shopAddress: certForm.value.address,
        shopPhone: certForm.value.phone,
        license: certForm.value.license
      })
      if (res.code === 200) {
        alert('商家入驻申请已提交，等待管理员审核')
        showCertificationModal.value = false
      } else {
        alert('提交失败')
      }
    } catch (error) {
      console.error('提交失败:', error)
      alert('提交失败')
    }
  }
  certForm.value = { name: '', idCard: '', experience: '', shopName: '', license: '', address: '', phone: '' }
}

// 反馈相关
const showFeedbackModal = ref(false)
const feedbackForm = ref({ content: '' })

// 提交反馈
const submitFeedback = async () => {
  if (!feedbackForm.value.content) {
    alert('请填写反馈内容')
    return
  }

  try {
    const res = await submitFeedbackApi({
      userId: userStore.currentUser?.id,
      content: feedbackForm.value.content
    })
    if (res.code === 200) {
      alert('感谢您的反馈，我们会认真处理！')
      showFeedbackModal.value = false
      feedbackForm.value.content = ''
    } else {
      alert('提交失败')
    }
  } catch (error) {
    console.error('提交反馈失败:', error)
    alert('提交失败')
  }
}

// 退出登录
const handleLogout = () => {
  if (confirm('确定要退出登录吗？')) {
    userStore.logout()
    router.push('/login')
  }
}

// 页面跳转
const goTo = (target) => {
  const routes = {
    pets: '/pet-profile',
    orders: '/my-orders',
    myTasks: '/my-published-tasks',
    myVideos: '/my-videos',
    favorites: '/favorites',
    availableTasks: '/service-hall?tab=task',
    myWork: '/my-work',
    earnings: '/earnings',
    ratings: '/ratings',
    products: '/shop-manage',
    appointments: '/orders',
    employees: '/employees',
    statistics: '/statistics',
    videoAudit: '/video-audit',
    qualificationAudit: '/qualification-audit',
    dispute: '/dispute',
    feedback: '/user-feedback'
  }
  if (routes[target]) {
    router.push(routes[target])
  } else {
    alert(`${target} 页面开发中`)
  }
}
</script>

<style scoped>
.user-center {
  max-width: 800px;
}

.user-header {
  background: linear-gradient(135deg, #1a3c34, #0f2a24);
  border-radius: 20px;
  padding: 28px 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 24px;
  color: white;
}

.avatar {
  width: 64px;
  height: 64px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
}

.user-name {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 6px;
}

.user-role {
  font-size: 14px;
  opacity: 0.8;
}

.switch-role {
  margin-left: 12px;
  cursor: pointer;
  text-decoration: underline;
}

.menu-list {
  background: white;
  border-radius: 16px;
  margin-bottom: 16px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.2s;
}

.menu-item:hover {
  background: #f8f9fa;
}

.menu-icon {
  font-size: 22px;
  margin-right: 14px;
}

.menu-text {
  flex: 1;
  font-size: 15px;
  color: #333;
}

.menu-arrow {
  font-size: 20px;
  color: #ccc;
}

.menu-divider {
  height: 1px;
  background: #eee;
  margin: 8px 0;
}

.menu-badge {
  background: #ff9800;
  color: white;
  font-size: 10px;
  padding: 2px 8px;
  border-radius: 20px;
  margin-right: 8px;
}

.role-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.role-modal-content {
  background: white;
  border-radius: 20px;
  padding: 8px;
  min-width: 200px;
}

.role-option {
  padding: 12px 20px;
  cursor: pointer;
  border-radius: 12px;
  text-align: center;
}

.role-option:hover {
  background: #f0f0f0;
}

/* 模态框样式 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1001;
}

.modal-content {
  background: white;
  border-radius: 20px;
  padding: 24px;
  width: 450px;
  max-width: 90%;
}

.modal-content h3 {
  margin-bottom: 20px;
  color: #1a3c34;
}

.modal-input {
  width: 100%;
  padding: 12px;
  margin-bottom: 16px;
  border: 1px solid #ddd;
  border-radius: 12px;
  font-size: 14px;
  box-sizing: border-box;
}

.modal-textarea {
  width: 100%;
  padding: 12px;
  margin-bottom: 16px;
  border: 1px solid #ddd;
  border-radius: 12px;
  font-size: 14px;
  min-height: 100px;
  resize: vertical;
  box-sizing: border-box;
  font-family: inherit;
}

.cert-type-select {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.type-btn {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 12px;
  cursor: pointer;
}

.type-btn.active {
  background: #2ecc71;
  color: white;
  border-color: #2ecc71;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.btn-cancel {
  background: #f0f0f0;
  border: none;
  padding: 10px 24px;
  border-radius: 24px;
  cursor: pointer;
}

.btn-submit {
  background: #2ecc71;
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 24px;
  cursor: pointer;
}
</style>