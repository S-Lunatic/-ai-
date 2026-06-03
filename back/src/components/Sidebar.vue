<template>
  <div class="sidebar">
    <div class="logo">
      <span class="logo-icon">🐾</span>
      <span class="logo-text">宠物万事通</span>
    </div>
    
    <div class="nav-menu">
      <!-- 宠物主人菜单 -->
      <template v-if="currentRole === 'petOwner'">
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/' }"
          @click="navigateTo('/')"
        >
          <span class="nav-icon">🏠</span>
          <span class="nav-text">首页</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/pet-profile' }"
          @click="navigateTo('/pet-profile')"
        >
          <span class="nav-icon">📚</span>
          <span class="nav-text">宠物档案</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/health-manage' }"
          @click="navigateTo('/health-manage')"
        >
          <span class="nav-icon">📊</span>
          <span class="nav-text">健康管理</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/daily-record' }"
          @click="navigateTo('/daily-record')"
        >
          <span class="nav-icon">📝</span>
          <span class="nav-text">日常记录</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/service-hall' }"
          @click="navigateTo('/service-hall')"
        >
          <span class="nav-icon">🛎️</span>
          <span class="nav-text">服务大厅</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/user-center' }"
          @click="navigateTo('/user-center')"
        >
          <span class="nav-icon">👤</span>
          <span class="nav-text">我的</span>
        </div>
      </template>
      
      <!-- 护工菜单 -->
      <template v-if="currentRole === 'caregiver'">
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/' }"
          @click="navigateTo('/')"
        >
          <span class="nav-icon">🏠</span>
          <span class="nav-text">首页</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/service-hall' }"
          @click="navigateTo('/service-hall')"
        >
          <span class="nav-icon">📋</span>
          <span class="nav-text">任务大厅</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/my-work' }"
          @click="navigateTo('/my-work')"
        >
          <span class="nav-icon">✅</span>
          <span class="nav-text">我的工作</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/earnings' }"
          @click="navigateTo('/earnings')"
        >
          <span class="nav-icon">💰</span>
          <span class="nav-text">收益</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/user-center' }"
          @click="navigateTo('/user-center')"
        >
          <span class="nav-icon">👤</span>
          <span class="nav-text">我的</span>
        </div>
      </template>
      
      <!-- 商家菜单 -->
      <template v-if="currentRole === 'merchant'">
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/' }"
          @click="navigateTo('/')"
        >
          <span class="nav-icon">🏠</span>
          <span class="nav-text">首页</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/shop-manage' }"
          @click="navigateTo('/shop-manage')"
        >
          <span class="nav-icon">🏪</span>
          <span class="nav-text">店铺管理</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/orders' }"
          @click="navigateTo('/orders')"
        >
          <span class="nav-icon">📦</span>
          <span class="nav-text">订单处理</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/statistics' }"
          @click="navigateTo('/statistics')"
        >
          <span class="nav-icon">📊</span>
          <span class="nav-text">数据统计</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/user-center' }"
          @click="navigateTo('/user-center')"
        >
          <span class="nav-icon">👤</span>
          <span class="nav-text">我的</span>
        </div>
      </template>
      
      <!-- 管理员菜单 -->
      <template v-if="currentRole === 'admin'">
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/' }"
          @click="navigateTo('/')"
        >
          <span class="nav-icon">🏠</span>
          <span class="nav-text">首页</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/video-audit' }"
          @click="navigateTo('/video-audit')"
        >
          <span class="nav-icon">🎬</span>
          <span class="nav-text">视频审核</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/qualification-audit' }"
          @click="navigateTo('/qualification-audit')"
        >
          <span class="nav-icon">✅</span>
          <span class="nav-text">资质审核</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/dispute' }"
          @click="navigateTo('/dispute')"
        >
          <span class="nav-icon">⚖️</span>
          <span class="nav-text">纠纷仲裁</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/user-feedback' }"
          @click="navigateTo('/user-feedback')"
        >
          <span class="nav-icon">💬</span>
          <span class="nav-text">用户反馈</span>
        </div>
        <div 
          class="nav-item"
          :class="{ active: currentPath === '/user-center' }"
          @click="navigateTo('/user-center')"
        >
          <span class="nav-icon">👤</span>
          <span class="nav-text">我的</span>
        </div>
      </template>
    </div>
    
    <div class="user-info">
      <div class="avatar">👤</div>
      <div class="user-details">
        <div class="user-name">测试用户</div>
        <div class="user-role">{{ roleName }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { storeToRefs } from 'pinia'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 关键：使用 storeToRefs 确保响应式
const { currentRole } = storeToRefs(userStore)
const { roleNameMap } = userStore

const currentPath = computed(() => route.path)
const roleName = computed(() => roleNameMap[currentRole.value])

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.sidebar {
  width: 260px;
  background: linear-gradient(180deg, #1a3c34 0%, #0f2a24 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.1);
}

.logo {
  padding: 24px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 28px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
}

.nav-menu {
  flex: 1;
  padding: 24px 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: rgba(255, 255, 255, 0.7);
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.nav-item.active {
  background-color: #2ecc71;
  color: #fff;
  box-shadow: 0 4px 12px rgba(46, 204, 113, 0.3);
}

.nav-icon {
  font-size: 20px;
  width: 28px;
}

.nav-text {
  font-size: 15px;
  font-weight: 500;
}

.user-info {
  padding: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
}

.user-role {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}
</style>