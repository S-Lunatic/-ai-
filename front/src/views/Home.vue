<template>
  <div class="home">
    <!-- 顶部横幅 -->
    <div class="banner">
      🎉 春季体检8折活动 · 点击查看 &gt;
    </div>
    
    <!-- 快捷入口（根据角色动态显示） -->
    <div class="shortcuts">
      <div class="shortcut-item" v-for="item in shortcuts" :key="item.name" @click="handleShortcut(item)">
        <div class="shortcut-icon">{{ item.icon }}</div>
        <div class="shortcut-name">{{ item.name }}</div>
      </div>
    </div>
    
    <!-- 新手教程专区 - 显示最新视频 -->
    <div class="section">
      <div class="section-header">
        <h3>📹 新手教程专区</h3>
        <a href="#" class="more" @click.prevent="router.push('/tutorial')">查看更多 &gt;</a>
      </div>
      <div class="video-grid">
        <div class="video-card" v-for="video in latestVideos" :key="video.id" @click="goToVideo(video)">
          <div class="video-cover">{{ video.cover || '🎬' }}</div>
          <div class="video-title">{{ video.title }}</div>
          <div class="video-author">👤 {{ video.author }}</div>
        </div>
        <div v-if="latestVideos.length === 0" class="empty-video">
          暂无视频，快去发布吧～
        </div>
      </div>
    </div>
    
    <!-- 智能问答（点击跳转到独立页面） -->
    <div class="section qa-clickable" @click="router.push('/qa')">
      <div class="section-header">
        <h3>🤖 智能问答</h3>
        <a href="#" class="more" @click.prevent="router.push('/qa')">去问答 &gt;</a>
      </div>
      <div class="qa-preview">
        <div class="preview-item">💬 疫苗间隔多久？</div>
        <div class="preview-item">💬 驱虫药怎么选？</div>
        <div class="preview-item">💬 狗狗咳嗽怎么办？</div>
        <div class="preview-hint">点击此处进入完整问答界面 →</div>
      </div>
    </div>

    <!-- 发视频模态框 -->
    <div v-if="showVideoModal" class="modal" @click.self="showVideoModal = false">
      <div class="modal-content">
        <h3>发布视频</h3>
        <input type="text" v-model="videoForm.title" placeholder="视频标题" class="modal-input">
        <textarea v-model="videoForm.desc" placeholder="视频描述" class="modal-textarea"></textarea>
        <div class="upload-area" @click="triggerFileUpload">
          <span v-if="!videoForm.file">📹 点击上传视频</span>
          <span v-else>✅ 已选择：{{ videoForm.file.name }}</span>
        </div>
        <input type="file" ref="fileInput" @change="handleFileSelect" accept="video/*" style="display:none">
        <div class="modal-actions">
          <button class="btn-cancel" @click="showVideoModal = false">取消</button>
          <button class="btn-submit" @click="submitVideo">发布</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { storeToRefs } from 'pinia'
import { uploadVideo, publishVideo, getApprovedVideos } from '../api/video'

const router = useRouter()
const userStore = useUserStore()
const { currentRole } = storeToRefs(userStore)

// ========== 快捷入口（根据角色动态显示） ==========
const getShortcutsByRole = () => {
  const role = currentRole.value
  
  if (role === 'petOwner') {
    return [
      { name: '教程', icon: '📺', path: '/tutorial' },
      { name: '记录', icon: '📝', path: '/daily-record' },
      { name: 'AI识别', icon: '🤖', path: '/ai-recognize' },
      { name: '问答', icon: '💬', path: '/qa' },
      { name: '疫苗', icon: '💉', path: '/health-manage?tab=vaccine' },
      { name: '体重', icon: '⚖️', path: '/health-manage?tab=weight' },
      { name: '预约', icon: '🛎️', path: '/service-hall?tab=merchant' },
      { name: '发视频', icon: '📹', action: 'upload' }
    ]
  }
  
  if (role === 'caregiver') {
    return [
      { name: '教程', icon: '📺', path: '/tutorial' },
      { name: 'AI识别', icon: '🤖', path: '/ai-recognize' },
      { name: '问答', icon: '💬', path: '/qa' },
      { name: '我的工作', icon: '✅', path: '/my-work' },
      { name: '收益', icon: '💰', path: '/earnings' },
      { name: '任务大厅', icon: '📋', path: '/service-hall?tab=task' },
      { name: '发视频', icon: '📹', action: 'upload' }
    ]
  }
  
  if (role === 'merchant') {
    return [
      { name: '教程', icon: '📺', path: '/tutorial' },
      { name: 'AI识别', icon: '🤖', path: '/ai-recognize' },
      { name: '问答', icon: '💬', path: '/qa' },
      { name: '店铺管理', icon: '🏪', path: '/shop-manage' },
      { name: '订单处理', icon: '📦', path: '/orders' },
      { name: '数据统计', icon: '📊', path: '/statistics' },
      { name: '发视频', icon: '📹', action: 'upload' }
    ]
  }
  
  return [
    { name: '视频审核', icon: '🎬', path: '/video-audit' },
    { name: '资质审核', icon: '✅', path: '/qualification-audit' },
    { name: '纠纷仲裁', icon: '⚖️', path: '/dispute' },
    { name: '用户反馈', icon: '💬', path: '/user-feedback' }
  ]
}

const shortcuts = ref(getShortcutsByRole())

watch(currentRole, () => {
  shortcuts.value = getShortcutsByRole()
})

const handleShortcut = (item) => {
  if (item.action === 'upload') {
    showVideoModal.value = true
  } else if (item.path) {
    router.push(item.path)
  } else {
    alert(`${item.name}功能开发中`)
  }
}

// ========== 最新视频（新手教程专区） ==========
// 官方视频
const officialVideos = ref([
  { id: 1, title: '训练狗狗坐下', cover: '🐕', author: '专业训犬师', submitTime: '2026-01-01' },
  { id: 2, title: '猫咪喂养指南', cover: '🐈', author: '资深猫奴', submitTime: '2026-01-15' },
  { id: 3, title: '宠物医疗急救', cover: '🏥', author: '宠物医生', submitTime: '2026-02-01' }
])

// 用户发布的已通过视频
const userApprovedVideos = ref([])

// 从后端加载已通过视频
const loadUserVideos = async () => {
  try {
    const res = await getApprovedVideos()
    if (res.code === 200) {
      userApprovedVideos.value = res.data.map(v => ({
        id: v.id,
        title: v.title,
        cover: '🎬',
        author: `用户${v.userId}`,
        submitTime: v.createdAt
      }))
    }
  } catch (error) {
    console.error('加载用户视频失败:', error)
  }
}

// 所有视频合并，按时间排序取前4个
const latestVideos = computed(() => {
  const all = [...officialVideos.value, ...userApprovedVideos.value]
  const sorted = all.sort((a, b) => new Date(b.submitTime) - new Date(a.submitTime))
  return sorted.slice(0, 4)
})

const goToVideo = (video) => {
  router.push('/tutorial')
}

// ========== 发视频相关 ==========
const showVideoModal = ref(false)
const fileInput = ref(null)
const uploading = ref(false)
const videoForm = ref({
  title: '',
  desc: '',
  file: null
})

const triggerFileUpload = () => {
  fileInput.value.click()
}

const handleFileSelect = (e) => {
  const file = e.target.files[0]
  if (file) {
    // 检查文件类型
    if (!file.type.startsWith('video/')) {
      alert('请选择视频文件')
      return
    }
    // 检查文件大小（100MB）
    if (file.size > 100 * 1024 * 1024) {
      alert('视频文件不能超过100MB')
      return
    }
    videoForm.value.file = file
  }
}

const submitVideo = async () => {
  if (!videoForm.value.title) {
    alert('请填写视频标题')
    return
  }
  if (!videoForm.value.file) {
    alert('请选择视频文件')
    return
  }
  
  uploading.value = true
  
  try {
    // 1. 上传视频文件
    const uploadRes = await uploadVideo(videoForm.value.file)
    if (uploadRes.code !== 200) {
      alert(uploadRes.message || '上传失败')
      return
    }
    const videoUrl = uploadRes.data
    
    // 2. 发布视频信息
    const publishRes = await publishVideo({
      userId: userStore.currentUser?.id,
      title: videoForm.value.title,
      description: videoForm.value.desc,
      videoUrl: videoUrl,
      category: 'care'
    })
    
    if (publishRes.code === 200) {
      alert(`视频「${videoForm.value.title}」已提交，等待审核`)
      showVideoModal.value = false
      videoForm.value = { title: '', desc: '', file: null }
      if (fileInput.value) fileInput.value.value = ''
    } else {
      alert('发布失败')
    }
  } catch (error) {
    console.error('发布失败:', error)
    alert('发布失败')
  } finally {
    uploading.value = false
  }
}

// ========== 初始化 ==========
onMounted(() => {
  loadUserVideos()
})
</script>

<style scoped>
.home {
  max-width: 1200px;
}

.banner {
  background: linear-gradient(135deg, #2ecc71, #27ae60);
  color: white;
  padding: 16px 24px;
  border-radius: 16px;
  margin-bottom: 28px;
  cursor: pointer;
  font-weight: 500;
}

.shortcuts {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 32px;
}

.shortcut-item {
  background: white;
  padding: 20px;
  border-radius: 16px;
  text-align: center;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s;
}

.shortcut-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.shortcut-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.shortcut-name {
  font-size: 14px;
  color: #333;
}

.section {
  background: white;
  border-radius: 16px;
  padding: 20px 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-header h3 {
  font-size: 18px;
  color: #1a3c34;
}

.more {
  color: #2ecc71;
  text-decoration: none;
  font-size: 14px;
  cursor: pointer;
}

.video-grid {
  display: flex;
  gap: 20px;
}

.video-card {
  flex: 1;
  background: #f8f9fa;
  border-radius: 12px;
  padding: 16px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.video-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.video-cover {
  font-size: 48px;
  margin-bottom: 12px;
}

.video-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.video-author {
  font-size: 12px;
  color: #999;
}

.empty-video {
  flex: 1;
  text-align: center;
  padding: 40px;
  color: #999;
}

/* 智能问答区域 */
.qa-clickable {
  cursor: pointer;
  transition: all 0.2s;
}

.qa-clickable:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.qa-preview {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.preview-item {
  padding: 10px 14px;
  background: #f8f9fa;
  border-radius: 24px;
  font-size: 13px;
  color: #555;
  transition: background 0.2s;
}

.qa-clickable:hover .preview-item {
  background: #e8f5e9;
}

.preview-hint {
  margin-top: 8px;
  font-size: 12px;
  color: #2ecc71;
  text-align: right;
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
  z-index: 1000;
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

.upload-area {
  background: #f8f9fa;
  border: 2px dashed #ddd;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  margin-bottom: 20px;
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