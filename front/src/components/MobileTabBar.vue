<template>
  <div class="mobile-tab-bar">
    <div 
      v-for="item in menuItems" 
      :key="item.path"
      class="tab-item"
      :class="{ active: currentPath === item.path }"
      @click="navigateTo(item.path)"
    >
      <span class="tab-icon">{{ item.icon }}</span>
      <span class="tab-name">{{ item.name }}</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const currentPath = computed(() => route.path)

// 根据角色显示不同的底部菜单
const menuItems = computed(() => {
  const role = userStore.currentRole
  
  // 宠物主人菜单
  if (role === 'petOwner') {
    return [
      { name: '首页', icon: '🏠', path: '/' },
      { name: '宠物', icon: '🐾', path: '/pet-profile' },
      { name: '健康', icon: '📊', path: '/health-manage' },
      { name: '服务', icon: '🛎️', path: '/service-hall' },
      { name: '我的', icon: '👤', path: '/user-center' }
    ]
  }
  
  // 护工菜单
  if (role === 'caregiver') {
    return [
      { name: '首页', icon: '🏠', path: '/' },
      { name: '任务', icon: '📋', path: '/service-hall?tab=task' },
      { name: '工作', icon: '✅', path: '/my-work' },
      { name: '收益', icon: '💰', path: '/earnings' },
      { name: '我的', icon: '👤', path: '/user-center' }
    ]
  }
  
  // 商家菜单（手机端简版）
  if (role === 'merchant') {
    return [
      { name: '首页', icon: '🏠', path: '/' },
      { name: '店铺', icon: '🏪', path: '/shop-manage' },
      { name: '订单', icon: '📦', path: '/orders' },
      { name: '我的', icon: '👤', path: '/user-center' }
    ]
  }
  
  // 管理员菜单（手机端简版）
  return [
    { name: '审核', icon: '🎬', path: '/video-audit' },
    { name: '资质', icon: '✅', path: '/qualification-audit' },
    { name: '纠纷', icon: '⚖️', path: '/dispute' },
    { name: '我的', icon: '👤', path: '/user-center' }
  ]
})

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.mobile-tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  background: white;
  border-top: 1px solid #e0e0e0;
  padding: 8px 0;
  z-index: 100;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 6px 0;
  cursor: pointer;
  transition: all 0.2s;
}

.tab-icon {
  font-size: 22px;
}

.tab-name {
  font-size: 11px;
  color: #666;
}

.tab-item.active .tab-icon,
.tab-item.active .tab-name {
  color: #2ecc71;
}

.tab-item.active .tab-icon {
  transform: scale(1.05);
}
</style>