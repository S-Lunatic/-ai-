<template>
  <div class="my-published-tasks">
    <h1 class="page-title">📋 我发布的任务</h1>
    
    <div class="tabs">
      <div class="tab" :class="{ active: activeTab === 'pending' }" @click="activeTab = 'pending'">
        待接单 ({{ pendingTasks.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'processing' }" @click="activeTab = 'processing'">
        进行中 ({{ processingTasks.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'completed' }" @click="activeTab = 'completed'">
        已完成 ({{ completedTasks.length }})
      </div>
    </div>
    
    <!-- 待接单任务 -->
    <div v-if="activeTab === 'pending'" class="task-list">
      <div class="task-card" v-for="task in pendingTasks" :key="task.id">
        <div class="task-header">
          <span class="task-icon">{{ getTaskIcon(task.type) }}</span>
          <span class="task-title">{{ task.title }}</span>
          <span class="task-status status-pending">待接单</span>
        </div>
        <div class="task-detail">💰 报酬：{{ task.reward }}元/小时</div>
        <div class="task-detail">📅 时间：{{ formatTime(task.taskTime) }}</div>
        <div class="task-detail">📍 地点：{{ task.location }}</div>
        <div class="task-detail">📝 要求：{{ task.requirement || '无' }}</div>
        <div class="task-actions">
          <button class="btn-cancel" @click="cancelTask(task)">取消任务</button>
        </div>
      </div>
      <div v-if="pendingTasks.length === 0" class="empty-state">暂无待接单任务</div>
    </div>
    
    <!-- 进行中任务 -->
    <div v-if="activeTab === 'processing'" class="task-list">
      <div class="task-card" v-for="task in processingTasks" :key="task.id">
        <div class="task-header">
          <span class="task-icon">{{ getTaskIcon(task.type) }}</span>
          <span class="task-title">{{ task.title }}</span>
          <span class="task-status status-processing">进行中</span>
        </div>
        <div class="task-detail">💰 报酬：{{ task.reward }}元/小时</div>
        <div class="task-detail">📅 时间：{{ formatTime(task.taskTime) }}</div>
        <div class="task-detail">📍 地点：{{ task.location }}</div>
        <div class="task-detail">👤 接单护工：{{ task.acceptedBy || '待分配' }}</div>
      </div>
      <div v-if="processingTasks.length === 0" class="empty-state">暂无进行中的任务</div>
    </div>
    
    <!-- 已完成任务 -->
    <div v-if="activeTab === 'completed'" class="task-list">
      <div class="task-card completed" v-for="task in completedTasks" :key="task.id">
        <div class="task-header">
          <span class="task-icon">{{ getTaskIcon(task.type) }}</span>
          <span class="task-title">{{ task.title }}</span>
          <span class="task-status status-completed">已完成</span>
        </div>
        <div class="task-detail">💰 报酬：{{ task.reward }}元/小时</div>
        <div class="task-detail">📅 时间：{{ formatTime(task.taskTime) }}</div>
        <div class="task-detail">📍 地点：{{ task.location }}</div>
        <div class="task-detail">👤 护工：{{ task.acceptedBy || '未知' }}</div>
      </div>
      <div v-if="completedTasks.length === 0" class="empty-state">暂无已完成的任务</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { getMyPublishedTasks, cancelTask as cancelTaskApi } from '../api/task'

const userStore = useUserStore()
const activeTab = ref('pending')
const pendingTasks = ref([])
const processingTasks = ref([])
const completedTasks = ref([])

// 格式化时间
const formatTime = (time) => {
  if (!time) return '未知'
  return new Date(time).toLocaleString()
}

// 获取任务图标
const getTaskIcon = (type) => {
  const icons = {
    walking: '🐕',
    feeding: '🐈',
    playing: '🐕'
  }
  return icons[type] || '📝'
}

// 加载任务
const loadTasks = async () => {
  const userId = userStore.currentUser?.id
  if (!userId) return
  
  try {
    const res = await getMyPublishedTasks(userId)
    if (res.code === 200) {
      const allTasks = res.data
      pendingTasks.value = allTasks.filter(t => t.status === 'pending')
      processingTasks.value = allTasks.filter(t => t.status === 'processing')
      completedTasks.value = allTasks.filter(t => t.status === 'completed')
    }
  } catch (error) {
    console.error('加载任务失败:', error)
  }
}

// 取消任务
const cancelTask = async (task) => {
  if (!confirm(`确定要取消任务「${task.title}」吗？`)) return
  
  try {
    const res = await cancelTaskApi(task.id, userStore.currentUser?.id)
    if (res.code === 200) {
      alert('任务已取消')
      loadTasks()
    } else {
      alert('取消失败，只能取消待接单状态的任务')
    }
  } catch (error) {
    console.error('取消任务失败:', error)
    alert('取消失败')
  }
}

onMounted(() => {
  loadTasks()
})
</script>

<style scoped>
.my-published-tasks {
  max-width: 800px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 24px;
  color: #1a3c34;
}

.tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: white;
  padding: 6px;
  border-radius: 48px;
  width: fit-content;
}

.tab {
  padding: 8px 24px;
  border-radius: 40px;
  cursor: pointer;
  font-size: 14px;
}

.tab.active {
  background: #2ecc71;
  color: white;
}

.task-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.task-card.completed {
  background: #f8f9fa;
}

.task-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.task-icon {
  font-size: 24px;
}

.task-title {
  font-size: 18px;
  font-weight: 600;
  flex: 1;
}

.task-status {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
}

.status-pending {
  background: #e3f2fd;
  color: #2196f3;
}

.status-processing {
  background: #fff3e0;
  color: #ff9800;
}

.status-completed {
  background: #e8f5e9;
  color: #2ecc71;
}

.task-detail {
  font-size: 14px;
  color: #555;
  margin-bottom: 8px;
}

.task-actions {
  margin-top: 12px;
}

.btn-cancel {
  background: #f0f0f0;
  border: none;
  padding: 8px 20px;
  border-radius: 24px;
  cursor: pointer;
  color: #ff4757;
}

.empty-state {
  text-align: center;
  padding: 60px;
  color: #999;
  background: white;
  border-radius: 16px;
}
</style>