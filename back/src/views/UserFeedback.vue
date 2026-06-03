<template>
  <div class="user-feedback">
    <h1 class="page-title">用户反馈</h1>

    <div class="stats-badge">
      <span class="badge">待处理：{{ pendingFeedbacks.length }}条</span>
      <span class="badge">已处理：{{ resolvedFeedbacks.length }}条</span>
    </div>

    <div class="tabs">
      <div class="tab" :class="{ active: activeTab === 'pending' }" @click="activeTab = 'pending'">
        待处理 ({{ pendingFeedbacks.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'resolved' }" @click="activeTab = 'resolved'">
        已处理 ({{ resolvedFeedbacks.length }})
      </div>
    </div>

    <!-- 待处理反馈 -->
    <div v-if="activeTab === 'pending'" class="feedback-list">
      <div class="feedback-card" v-for="feedback in pendingFeedbacks" :key="feedback.id">
        <div class="feedback-header">
          <span class="feedback-user">👤 {{ feedback.userName || '用户' + feedback.userId }}</span>
          <span class="feedback-time">{{ formatTime(feedback.createdAt) }}</span>
        </div>
        <div class="feedback-content">{{ feedback.content }}</div>
        <div class="feedback-actions">
          <select v-model="feedback.category">
            <option value="">请选择分类</option>
            <option value="bug">🐛 问题</option>
            <option value="suggest">💡 建议</option>
            <option value="other">📦 其他</option>
          </select>
          <textarea v-model="feedback.reply" placeholder="回复内容..." rows="2"></textarea>
          <button class="btn-resolve" @click="resolveFeedback(feedback)">✓ 处理</button>
        </div>
      </div>
      <div v-if="pendingFeedbacks.length === 0" class="empty-state">暂无待处理反馈</div>
    </div>

    <!-- 已处理反馈 -->
    <div v-if="activeTab === 'resolved'" class="feedback-list">
      <div class="feedback-card resolved" v-for="feedback in resolvedFeedbacks" :key="feedback.id">
        <div class="feedback-header">
          <span class="feedback-user">👤 {{ feedback.userName || '用户' + feedback.userId }}</span>
          <span class="feedback-time">{{ formatTime(feedback.createdAt) }}</span>
          <span class="feedback-category">{{ getCategoryLabel(feedback.category) }}</span>
        </div>
        <div class="feedback-content">{{ feedback.content }}</div>
        <div class="feedback-reply" v-if="feedback.reply">
          <strong>回复：</strong>{{ feedback.reply }}
        </div>
      </div>
      <div v-if="resolvedFeedbacks.length === 0" class="empty-state">暂无已处理反馈</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getAllFeedback, resolveFeedback as resolveFeedbackApi } from '../api/feedback'

const activeTab = ref('pending')
const allFeedbacks = ref([])

// 分类
const pendingFeedbacks = computed(() => allFeedbacks.value.filter(f => f.status === 'pending'))
const resolvedFeedbacks = computed(() => allFeedbacks.value.filter(f => f.status === 'resolved'))

// 格式化时间
const formatTime = (time) => {
  if (!time) return '未知'
  return new Date(time).toLocaleString()
}

// 获取分类标签
const getCategoryLabel = (category) => {
  const labels = { bug: '🐛 问题', suggest: '💡 建议', other: '📦 其他' }
  return labels[category] || category
}

// 加载所有反馈
const loadFeedbacks = async () => {
  try {
    const res = await getAllFeedback()
    if (res.code === 200) {
      allFeedbacks.value = res.data
    }
  } catch (error) {
    console.error('加载反馈失败:', error)
  }
}

// 处理反馈
const resolveFeedback = async (feedback) => {
  if (!feedback.category) {
    alert('请选择分类')
    return
  }

  try {
    const res = await resolveFeedbackApi(feedback.id, feedback.category, feedback.reply || '')
    if (res.code === 200) {
      alert('处理成功')
      await loadFeedbacks()
    } else {
      alert('处理失败')
    }
  } catch (error) {
    console.error('处理失败:', error)
    alert('处理失败')
  }
}

onMounted(() => {
  loadFeedbacks()
})
</script>

<style scoped>
.user-feedback { max-width: 900px; }
.page-title { font-size: 24px; margin-bottom: 24px; color: #1a3c34; }

.stats-badge { display: flex; gap: 16px; margin-bottom: 24px; }
.badge { background: white; padding: 8px 20px; border-radius: 30px; font-size: 14px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }

.tabs { display: flex; gap: 8px; margin-bottom: 24px; background: white; padding: 6px; border-radius: 48px; width: fit-content; }
.tab { padding: 8px 24px; border-radius: 40px; cursor: pointer; font-size: 14px; }
.tab.active { background: #2ecc71; color: white; }

.feedback-card { background: white; border-radius: 16px; padding: 20px; margin-bottom: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.feedback-card.resolved { background: #f8f9fa; }

.feedback-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; flex-wrap: wrap; gap: 8px; }
.feedback-user { font-size: 14px; font-weight: 500; color: #1a3c34; }
.feedback-time { font-size: 12px; color: #999; }
.feedback-category { background: #e8f5e9; color: #2ecc71; padding: 2px 10px; border-radius: 20px; font-size: 12px; }

.feedback-content { font-size: 14px; color: #555; padding: 12px 0; border-bottom: 1px solid #eee; margin-bottom: 12px; }

.feedback-actions { display: flex; flex-direction: column; gap: 12px; }
.feedback-actions select, .feedback-actions textarea { padding: 8px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 13px; }
.feedback-actions textarea { resize: vertical; }

.feedback-reply { margin-top: 12px; padding-top: 12px; border-top: 1px solid #eee; font-size: 13px; color: #666; }

.btn-resolve { background: #2ecc71; color: white; border: none; padding: 8px 16px; border-radius: 20px; cursor: pointer; align-self: flex-end; }

.empty-state { text-align: center; padding: 60px; color: #999; background: white; border-radius: 16px; }
</style>