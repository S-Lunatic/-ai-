<template>
  <!-- 已登录：显示完整布局 -->
  <div class="app-container" v-if="userStore.isLoggedIn">
    <!-- 电脑端：左侧导航栏 -->
    <Sidebar v-if="!isMobile" />
    <!-- 手机端：底部导航栏 -->
    <div class="mobile-layout" v-else>
      <div class="mobile-content">
        <router-view />
      </div>
      <MobileTabBar />
    </div>
    <!-- 电脑端内容区 -->
    <div class="main-content" v-if="!isMobile">
      <router-view />
    </div>
  </div>
  <!-- 未登录：只显示路由内容 -->
  <router-view v-else />
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useUserStore } from './stores/user'
import Sidebar from './components/Sidebar.vue'
import MobileTabBar from './components/MobileTabBar.vue'

const userStore = useUserStore()
const isMobile = ref(window.innerWidth < 768)

const handleResize = () => {
  isMobile.value = window.innerWidth < 768
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #f5f7fa;
}

.app-container {
  display: flex;
  height: 100vh;
  width: 100%;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  padding: 24px 32px;
  background-color: #f5f7fa;
}

/* 手机端布局 */
.mobile-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100%;
}

.mobile-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  padding-bottom: 70px;
  background-color: #f5f7fa;
}

/* ========== 响应式适配 ========== */
@media (max-width: 768px) {
  .main-content {
    padding: 16px;
  }
  
  /* 卡片内边距减小 */
  .info-card, .section, .task-card, .order-card {
    padding: 16px !important;
  }
  
  /* 快捷入口网格 */
  .shortcuts {
    grid-template-columns: repeat(4, 1fr) !important;
    gap: 10px !important;
  }
  
  /* 两列布局改单列 */
  .two-columns {
    grid-template-columns: 1fr !important;
  }
  
  /* 视频网格改单列 */
  .video-grid {
    flex-direction: column !important;
  }
  
  /* 宠物切换栏横向滚动 */
  .pet-tabs {
    overflow-x: auto;
    flex-wrap: nowrap;
  }
}
</style>