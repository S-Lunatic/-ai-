<template>
  <div class="dispute">
    <h1 class="page-title">纠纷仲裁</h1>
    
    <div class="stats-badge">
      <span class="badge">待处理：{{ pendingDisputes.length }}条</span>
      <span class="badge">已完结：{{ resolvedDisputes.length }}条</span>
    </div>
    
    <div class="tabs">
      <div class="tab" :class="{ active: activeTab === 'pending' }" @click="activeTab = 'pending'">
        待处理 ({{ pendingDisputes.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'resolved' }" @click="activeTab = 'resolved'">
        已完结 ({{ resolvedDisputes.length }})
      </div>
    </div>
    
    <!-- 待处理纠纷 -->
    <div v-if="activeTab === 'pending'" class="dispute-list">
      <div class="dispute-card" v-for="dispute in pendingDisputes" :key="dispute.id">
        <div class="dispute-header">
          <span class="dispute-id">纠纷号：{{ dispute.id }}</span>
          <span class="dispute-status">待处理</span>
        </div>
        <div class="dispute-content">
          <div class="dispute-order">订单号：{{ dispute.orderId }}</div>
          <div class="dispute-reason">纠纷原因：{{ dispute.reason }}</div>
          <div class="dispute-desc">详细描述：{{ dispute.description }}</div>
          <div class="dispute-time">发起时间：{{ formatTime(dispute.createdAt) }}</div>
        </div>
        <div class="dispute-actions">
          <button class="btn-arbitrate" @click="showArbitrateModal(dispute)">⚖️ 开始仲裁</button>
        </div>
      </div>
      <div v-if="pendingDisputes.length === 0" class="empty-state">暂无待处理纠纷</div>
    </div>
    
    <!-- 已完结纠纷 -->
    <div v-if="activeTab === 'resolved'" class="dispute-list">
      <div class="dispute-card resolved" v-for="dispute in resolvedDisputes" :key="dispute.id">
        <div class="dispute-header">
          <span class="dispute-id">纠纷号：{{ dispute.id }}</span>
          <span class="dispute-status status-resolved">已完结</span>
        </div>
        <div class="dispute-content">
          <div class="dispute-order">订单号：{{ dispute.orderId }}</div>
          <div class="dispute-reason">纠纷原因：{{ dispute.reason }}</div>
          <div class="dispute-desc">详细描述：{{ dispute.description }}</div>
          <div class="dispute-result">仲裁结果：{{ dispute.result || '未记录' }}</div>
          <div class="dispute-time">处理时间：{{ formatTime(dispute.resolvedAt) || formatTime(dispute.updatedAt) }}</div>
        </div>
      </div>
      <div v-if="resolvedDisputes.length === 0" class="empty-state">暂无已完结纠纷</div>
    </div>

    <!-- 仲裁模态框 -->
    <div v-if="arbitrateModal.visible" class="modal" @click.self="arbitrateModal.visible = false">
      <div class="modal-content">
        <h3>⚖️ 纠纷仲裁</h3>
        <div class="dispute-info">
          <p><strong>纠纷号：</strong>{{ arbitrateModal.dispute?.id }}</p>
          <p><strong>纠纷原因：</strong>{{ arbitrateModal.dispute?.reason }}</p>
          <p><strong>详细描述：</strong>{{ arbitrateModal.dispute?.description }}</p>
        </div>
        <select v-model="arbitrateForm.result" class="modal-select">
          <option value="">请选择仲裁结果</option>
          <option value="支持用户">✅ 支持用户（退款/赔偿）</option>
          <option value="支持商家">✅ 支持商家（驳回）</option>
          <option value="协商解决">🤝 协商解决</option>
        </select>
        <textarea 
          v-model="arbitrateForm.remark" 
          placeholder="仲裁意见（选填）..." 
          class="modal-textarea"
        ></textarea>
        <div class="modal-actions">
          <button class="btn-cancel" @click="arbitrateModal.visible = false">取消</button>
          <button class="btn-submit" @click="submitArbitrate">确认仲裁</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getPendingDisputes, getResolvedDisputes, resolveDispute } from '../api/dispute'

const activeTab = ref('pending')
const pendingDisputes = ref([])
const resolvedDisputes = ref([])

// 格式化时间
const formatTime = (time) => {
  if (!time) return '未知'
  return new Date(time).toLocaleString()
}

// 加载纠纷
const loadDisputes = async () => {
  try {
    const [pendingRes, resolvedRes] = await Promise.all([
      getPendingDisputes(),
      getResolvedDisputes()
    ])
    if (pendingRes.code === 200) {
      pendingDisputes.value = pendingRes.data
    }
    if (resolvedRes.code === 200) {
      resolvedDisputes.value = resolvedRes.data
    }
  } catch (error) {
    console.error('加载纠纷失败:', error)
  }
}

// 仲裁相关
const arbitrateModal = ref({ visible: false, dispute: null })
const arbitrateForm = ref({ result: '', remark: '' })

const showArbitrateModal = (dispute) => {
  arbitrateModal.value = { visible: true, dispute: dispute }
  arbitrateForm.value = { result: '', remark: '' }
}

const submitArbitrate = async () => {
  if (!arbitrateForm.value.result) {
    alert('请选择仲裁结果')
    return
  }
  
  const resultText = arbitrateForm.value.remark 
    ? `${arbitrateForm.value.result}：${arbitrateForm.value.remark}`
    : arbitrateForm.value.result
  
  try {
    const res = await resolveDispute(
      arbitrateModal.value.dispute.id,
      'resolved',
      resultText
    )
    
    if (res.code === 200) {
      alert('仲裁完成')
      arbitrateModal.value.visible = false
      loadDisputes()
    } else {
      alert('操作失败')
    }
  } catch (error) {
    console.error('仲裁失败:', error)
    alert('操作失败')
  }
}

onMounted(() => {
  loadDisputes()
})
</script>

<style scoped>
.dispute {
  max-width: 900px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 24px;
  color: #1a3c34;
}

.stats-badge {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.badge {
  background: white;
  padding: 8px 20px;
  border-radius: 30px;
  font-size: 14px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
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

.dispute-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.dispute-card.resolved {
  background: #f8f9fa;
}

.dispute-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.dispute-id {
  font-size: 13px;
  color: #999;
}

.dispute-status {
  background: #fff3e0;
  color: #ff9800;
  padding: 2px 12px;
  border-radius: 20px;
  font-size: 12px;
}

.status-resolved {
  background: #e8f5e9;
  color: #2ecc71;
}

.dispute-content {
  margin-bottom: 16px;
}

.dispute-order, .dispute-reason, .dispute-desc, .dispute-time, .dispute-result {
  font-size: 14px;
  color: #555;
  margin-bottom: 6px;
}

.dispute-result {
  color: #2ecc71;
  font-weight: 500;
}

.dispute-actions {
  display: flex;
  gap: 12px;
}

.btn-arbitrate {
  background: #2ecc71;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 24px;
  cursor: pointer;
}

.empty-state {
  text-align: center;
  padding: 60px;
  color: #999;
  background: white;
  border-radius: 16px;
}

/* 模态框 */
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

.dispute-info {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 12px;
  margin-bottom: 16px;
}

.dispute-info p {
  margin: 6px 0;
  font-size: 14px;
}

.modal-select, .modal-textarea {
  width: 100%;
  padding: 12px;
  margin-bottom: 16px;
  border: 1px solid #ddd;
  border-radius: 12px;
  font-size: 14px;
  box-sizing: border-box;
}

.modal-textarea {
  min-height: 100px;
  resize: vertical;
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