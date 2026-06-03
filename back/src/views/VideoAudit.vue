<template>
  <div class="video-audit">
    <h1 class="page-title">视频审核</h1>
    
    <div class="stats-badge">
      <span class="badge">待审核：{{ pendingVideos.length }}条</span>
      <span class="badge">已通过：{{ approvedVideos.length }}条</span>
      <span class="badge">已驳回：{{ rejectedVideos.length }}条</span>
    </div>
    
    <div class="tabs">
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
    
    <!-- 待审核视频 -->
    <div v-if="activeTab === 'pending'" class="video-list">
      <div class="video-card" v-for="video in pendingVideos" :key="video.id">
        <div class="video-preview">
          <video 
            :src="video.videoUrl" 
            controls 
            class="preview-video"
          ></video>
        </div>
        <div class="video-info">
          <div class="video-title">{{ video.title }}</div>
          <div class="video-meta">上传者：用户{{ video.userId }} · 提交时间：{{ formatTime(video.createdAt) }}</div>
          <div class="video-desc">{{ video.description }}</div>
        </div>
        <div class="video-actions">
          <select v-model="video.selectedCategory" class="category-select">
            <option value="">请选择分类</option>
            <option value="training">🐕 训练技巧</option>
            <option value="care">🏥 医疗养护</option>
            <option value="feed">🍖 喂养知识</option>
          </select>
          <button class="btn-pass" @click="handleApprove(video)">✓ 通过</button>
          <button class="btn-reject" @click="handleReject(video.id)">✗ 驳回</button>
        </div>
      </div>
      <div v-if="pendingVideos.length === 0" class="empty-state">暂无待审核视频</div>
    </div>
    
    <!-- 已通过视频 -->
    <div v-if="activeTab === 'approved'" class="video-list">
      <div class="video-card" v-for="video in approvedVideos" :key="video.id">
        <div class="video-preview">
          <video :src="video.videoUrl" controls class="preview-video"></video>
        </div>
        <div class="video-info">
          <div class="video-title">{{ video.title }}</div>
          <div class="video-meta">上传者：用户{{ video.userId }} · 审核时间：{{ formatTime(video.auditTime) }}</div>
          <div class="video-desc">{{ video.description }}</div>
          <div class="video-category">分类：{{ getCategoryName(video.category) }}</div>
        </div>
        <div class="video-actions">
          <button class="btn-delete" @click="deleteVideo(video.id)">🗑️ 删除</button>
        </div>
      </div>
      <div v-if="approvedVideos.length === 0" class="empty-state">暂无已通过视频</div>
    </div>
    
    <!-- 已驳回视频 -->
    <div v-if="activeTab === 'rejected'" class="video-list">
      <div class="video-card" v-for="video in rejectedVideos" :key="video.id">
        <div class="video-preview">
          <video :src="video.videoUrl" controls class="preview-video"></video>
        </div>
        <div class="video-info">
          <div class="video-title">{{ video.title }}</div>
          <div class="video-meta">上传者：用户{{ video.userId }}</div>
          <div class="video-desc">{{ video.description }}</div>
          <div class="video-reject-reason">
            ❌ 驳回原因：{{ video.rejectReason }}
          </div>
        </div>
        <div class="video-actions">
          <button class="btn-delete" @click="deleteVideo(video.id)">🗑️ 删除</button>
          <button class="btn-resubmit" @click="resubmitVideo(video)">🔄 重新审核</button>
        </div>
      </div>
      <div v-if="rejectedVideos.length === 0" class="empty-state">暂无已驳回视频</div>
    </div>

    <!-- 驳回原因弹窗 -->
    <div v-if="showRejectModal" class="modal" @click.self="showRejectModal = false">
      <div class="modal-content">
        <h3>驳回视频</h3>
        <textarea 
          v-model="rejectReason" 
          placeholder="请输入驳回原因..." 
          class="modal-textarea"
        ></textarea>
        <div class="modal-actions">
          <button class="btn-cancel" @click="showRejectModal = false">取消</button>
          <button class="btn-submit" @click="confirmReject">确认驳回</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getPendingVideos, auditVideo } from '../api/video'

const activeTab = ref('pending')
const pendingVideos = ref([])
const approvedVideos = ref([])
const rejectedVideos = ref([])

const showRejectModal = ref(false)
const rejectId = ref(null)
const rejectReason = ref('')

const formatTime = (time) => {
  if (!time) return '未知'
  return new Date(time).toLocaleString()
}

const getCategoryName = (category) => {
  const names = { training: '训练技巧', care: '医疗养护', feed: '喂养知识' }
  return names[category] || category
}

const loadVideos = async () => {
  try {
    const res = await getPendingVideos()
    if (res.code === 200) {
      const all = res.data
      pendingVideos.value = all.filter(v => v.status === 'pending').map(v => ({ ...v, selectedCategory: '' }))
      approvedVideos.value = all.filter(v => v.status === 'approved')
      rejectedVideos.value = all.filter(v => v.status === 'rejected')
    }
  } catch (error) {
    console.error('加载视频失败:', error)
  }
}

const handleApprove = async (video) => {
  if (!video.selectedCategory) {
    alert('请选择视频分类')
    return
  }
  if (!confirm('确定要通过该视频吗？')) return
  
  try {
    const res = await auditVideo(video.id, 'approved', null)
    if (res.code === 200) {
      // 更新视频分类（需要后端支持）
      await fetch(`/api/video/update-category?id=${video.id}&category=${video.selectedCategory}`, { method: 'POST' })
      alert('审核通过')
      loadVideos()
    } else {
      alert('操作失败')
    }
  } catch (error) {
    console.error('审核失败:', error)
    alert('操作失败')
  }
}

const handleReject = (id) => {
  rejectId.value = id
  rejectReason.value = ''
  showRejectModal.value = true
}

const confirmReject = async () => {
  if (!rejectReason.value.trim()) {
    alert('请填写驳回原因')
    return
  }
  
  try {
    const res = await auditVideo(rejectId.value, 'rejected', rejectReason.value)
    if (res.code === 200) {
      alert('已驳回')
      showRejectModal.value = false
      loadVideos()
    } else {
      alert('操作失败')
    }
  } catch (error) {
    console.error('驳回失败:', error)
    alert('操作失败')
  }
}

const deleteVideo = async (id) => {
  if (!confirm('确定要删除该视频吗？')) return
  alert('删除功能开发中')
}

const resubmitVideo = async (video) => {
  if (!confirm('确定要将该视频重新提交审核吗？')) return
  alert('重新审核功能开发中')
}

onMounted(() => {
  loadVideos()
})
</script>

<style scoped>
.video-audit { max-width: 900px; }
.page-title { font-size: 24px; margin-bottom: 24px; color: #1a3c34; }
.stats-badge { display: flex; gap: 16px; margin-bottom: 24px; flex-wrap: wrap; }
.badge { background: white; padding: 8px 20px; border-radius: 30px; font-size: 14px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.tabs { display: flex; gap: 8px; margin-bottom: 24px; background: white; padding: 6px; border-radius: 48px; width: fit-content; }
.tab { padding: 8px 24px; border-radius: 40px; cursor: pointer; font-size: 14px; }
.tab.active { background: #2ecc71; color: white; }
.video-list { display: flex; flex-direction: column; gap: 16px; }
.video-card { background: white; border-radius: 16px; padding: 20px; display: flex; gap: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.video-preview { width: 200px; }
.preview-video { width: 100%; max-height: 120px; border-radius: 8px; }
.video-info { flex: 1; }
.video-title { font-size: 16px; font-weight: 600; margin-bottom: 6px; }
.video-meta { font-size: 12px; color: #999; margin-bottom: 4px; }
.video-desc { font-size: 13px; color: #666; margin-bottom: 8px; }
.video-category { font-size: 12px; color: #2ecc71; margin-top: 6px; }
.video-reject-reason { font-size: 13px; color: #f44336; background: #ffebee; padding: 8px; border-radius: 8px; margin-top: 8px; }
.video-actions { display: flex; flex-direction: column; gap: 12px; min-width: 150px; }
.category-select { padding: 8px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 13px; }
.btn-pass { background: #2ecc71; color: white; border: none; padding: 8px 16px; border-radius: 20px; cursor: pointer; }
.btn-reject { background: #ff4757; color: white; border: none; padding: 8px 16px; border-radius: 20px; cursor: pointer; }
.btn-delete { background: #f0f0f0; color: #ff4757; border: none; padding: 8px 16px; border-radius: 20px; cursor: pointer; }
.btn-resubmit { background: #2196f3; color: white; border: none; padding: 8px 16px; border-radius: 20px; cursor: pointer; }
.empty-state { text-align: center; padding: 60px; color: #999; background: white; border-radius: 16px; }

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
  width: 400px;
  max-width: 90%;
}
.modal-content h3 { margin-bottom: 20px; color: #1a3c34; }
.modal-textarea { width: 100%; padding: 12px; margin-bottom: 16px; border: 1px solid #ddd; border-radius: 12px; font-size: 14px; min-height: 100px; resize: vertical; }
.modal-actions { display: flex; gap: 12px; justify-content: flex-end; }
.btn-cancel { background: #f0f0f0; border: none; padding: 10px 20px; border-radius: 24px; cursor: pointer; }
.btn-submit { background: #2ecc71; color: white; border: none; padding: 10px 20px; border-radius: 24px; cursor: pointer; }
</style>