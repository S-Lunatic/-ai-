<template>
  <div class="my-videos">
    <h1 class="page-title">🎬 我的视频</h1>
    
    <div class="tabs">
      <div class="tab" :class="{ active: activeTab === 'all' }" @click="activeTab = 'all'">
        全部 ({{ allVideos.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'pending' }" @click="activeTab = 'pending'">
        待审核 ({{ pendingVideos.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'approved' }" @click="activeTab = 'approved'">
        已通过 ({{ approvedVideos.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'rejected' }" @click="activeTab = 'rejected'">
        已驳回 ({{ rejectedVideos.length }})
      </div>
    </div>
    
    <div class="video-list">
      <div class="video-card" v-for="video in filteredVideos" :key="video.id">
        <div class="video-cover">{{ video.cover || '🎬' }}</div>
        <div class="video-info">
          <div class="video-title">{{ video.title }}</div>
          <div class="video-desc">{{ video.description }}</div>
          <div class="video-time">提交时间：{{ formatTime(video.createdAt) }}</div>
          <div class="video-status" :class="getStatusClass(video.status)">
            {{ getStatusText(video.status) }}
          </div>
          <div v-if="video.status === 'rejected' && video.rejectReason" class="video-reject">
            ❌ 驳回原因：{{ video.rejectReason }}
          </div>
          <div v-if="video.status === 'rejected'" class="video-actions">
            <button class="btn-resubmit" @click="resubmitVideo(video)">重新提交</button>
            <button class="btn-delete" @click="deleteVideo(video)">删除</button>
          </div>
        </div>
      </div>
    </div>
    
    <div v-if="filteredVideos.length === 0" class="empty-state">
      <span>📹</span>
      <p>暂无视频</p>
      <button class="btn-upload" @click="goToUpload">去发布视频</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { getMyVideos } from '../api/video'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('all')
const allVideos = ref([])

// 按状态分类
const pendingVideos = computed(() => allVideos.value.filter(v => v.status === 'pending'))
const approvedVideos = computed(() => allVideos.value.filter(v => v.status === 'approved'))
const rejectedVideos = computed(() => allVideos.value.filter(v => v.status === 'rejected'))

const filteredVideos = computed(() => {
  if (activeTab.value === 'all') return allVideos.value
  if (activeTab.value === 'pending') return pendingVideos.value
  if (activeTab.value === 'approved') return approvedVideos.value
  if (activeTab.value === 'rejected') return rejectedVideos.value
  return allVideos.value
})

// 格式化时间
const formatTime = (time) => {
  if (!time) return '未知'
  return new Date(time).toLocaleString()
}

// 获取状态样式
const getStatusClass = (status) => {
  switch (status) {
    case 'pending': return 'status-pending'
    case 'approved': return 'status-approved'
    case 'rejected': return 'status-rejected'
    default: return ''
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 'pending': return '⏳ 待审核'
    case 'approved': return '✅ 已通过'
    case 'rejected': return '❌ 已驳回'
    default: return '未知'
  }
}

// 加载我的视频
const loadMyVideos = async () => {
  const userId = userStore.currentUser?.id
  if (!userId) return
  
  try {
    const res = await getMyVideos(userId)
    if (res.code === 200) {
      allVideos.value = res.data
    }
  } catch (error) {
    console.error('加载视频失败:', error)
  }
}

// 重新提交
const resubmitVideo = (video) => {
  alert('重新提交功能开发中')
}

// 删除视频
const deleteVideo = (video) => {
  if (confirm(`确定要删除视频「${video.title}」吗？`)) {
    alert('删除功能开发中')
  }
}

// 跳转到发布视频
const goToUpload = () => {
  router.push('/')
}

onMounted(() => {
  loadMyVideos()
})
</script>

<style scoped>
.my-videos {
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

.video-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.video-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.video-cover {
  width: 80px;
  height: 80px;
  background: #f0f0f0;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
}

.video-info {
  flex: 1;
}

.video-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 6px;
}

.video-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
}

.video-time {
  font-size: 11px;
  color: #999;
  margin-bottom: 8px;
}

.video-status {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 20px;
  font-size: 12px;
  margin-bottom: 8px;
}

.status-pending {
  background: #fff3e0;
  color: #ff9800;
}

.status-approved {
  background: #e8f5e9;
  color: #2ecc71;
}

.status-rejected {
  background: #ffebee;
  color: #f44336;
}

.video-reject {
  font-size: 13px;
  color: #f44336;
  background: #ffebee;
  padding: 8px;
  border-radius: 8px;
  margin: 8px 0;
}

.video-actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.btn-resubmit {
  background: #2196f3;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 12px;
}

.btn-delete {
  background: #f0f0f0;
  color: #ff4757;
  border: none;
  padding: 6px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 12px;
}

.empty-state {
  text-align: center;
  padding: 60px;
  background: white;
  border-radius: 16px;
}

.empty-state span {
  font-size: 64px;
  display: block;
  margin-bottom: 16px;
}

.empty-state p {
  color: #999;
  margin-bottom: 20px;
}

.btn-upload {
  background: #2ecc71;
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 30px;
  cursor: pointer;
}
</style>