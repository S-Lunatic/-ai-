<template>
  <div class="orders">
    <h1 class="page-title">订单处理</h1>
    
    <div class="tabs">
      <div class="tab" :class="{ active: activeTab === 'pending' }" @click="activeTab = 'pending'">
        待确认 ({{ pendingOrders.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'processing' }" @click="activeTab = 'processing'">
        进行中 ({{ processingOrders.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'completed' }" @click="activeTab = 'completed'">
        已完成 ({{ completedOrders.length }})
      </div>
    </div>
    
    <!-- 待确认订单 -->
    <div v-if="activeTab === 'pending'" class="order-list">
      <div class="order-card" v-for="order in pendingOrders" :key="order.id">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <span class="order-status status-pending">待确认</span>
        </div>
        <div class="order-service">{{ order.serviceName }}</div>
        <div class="order-detail">用户：用户{{ order.userId }} · 宠物：{{ order.petName || '未知' }}（{{ order.petSpecies || '' }}）</div>
        <div class="order-detail">时间：{{ formatTime(order.appointTime) }}</div>
        <div class="order-detail">备注：{{ order.remark || '无' }}</div>
        <div class="order-actions">
          <button class="btn-confirm" @click="updateOrderStatus(order, 'processing')">确认接单</button>
          <button class="btn-reject" @click="updateOrderStatus(order, 'cancelled')">拒绝</button>
        </div>
      </div>
      <div v-if="pendingOrders.length === 0" class="empty-state">暂无待确认订单</div>
    </div>
    
    <!-- 进行中订单 -->
    <div v-if="activeTab === 'processing'" class="order-list">
      <div class="order-card" v-for="order in processingOrders" :key="order.id">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <span class="order-status status-progress">进行中</span>
        </div>
        <div class="order-service">{{ order.serviceName }}</div>
        <div class="order-detail">用户：用户{{ order.userId }} · 宠物：{{ order.petName || '未知' }}（{{ order.petSpecies || '' }}）</div>
        <div class="order-detail">时间：{{ formatTime(order.appointTime) }}</div>
        <div class="order-actions">
          <button class="btn-complete" @click="updateOrderStatus(order, 'completed')">标记完成</button>
        </div>
      </div>
      <div v-if="processingOrders.length === 0" class="empty-state">暂无进行中订单</div>
    </div>
    
    <!-- 已完成订单 -->
    <div v-if="activeTab === 'completed'" class="order-list">
      <div class="order-card completed" v-for="order in completedOrders" :key="order.id">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <span class="order-status status-done">已完成</span>
        </div>
        <div class="order-service">{{ order.serviceName }}</div>
        <div class="order-detail">用户：用户{{ order.userId }} · 宠物：{{ order.petName || '未知' }}（{{ order.petSpecies || '' }}）</div>
        <div class="order-detail">时间：{{ formatTime(order.appointTime) }}</div>
        <div class="order-rating" v-if="order.rating">用户评价：{{ '⭐'.repeat(order.rating) }}</div>
        
        <div class="order-actions" v-if="!order.disputeFiled">
          <button class="btn-dispute" @click="showDisputeModal(order)">⚠️ 发起纠纷</button>
        </div>
        <div class="dispute-status" v-else>
          <span class="dispute-pending">⏳ 纠纷处理中</span>
        </div>
      </div>
      <div v-if="completedOrders.length === 0" class="empty-state">暂无已完成订单</div>
    </div>

    <!-- 纠纷发起模态框 -->
    <div v-if="disputeModal.visible" class="modal" @click.self="disputeModal.visible = false">
      <div class="modal-content">
        <h3>⚠️ 发起纠纷</h3>
        <div class="order-info-mini">
          <p>订单号：{{ disputeModal.order?.orderNo }}</p>
          <p>服务：{{ disputeModal.order?.serviceName }}</p>
          <p>用户：用户{{ disputeModal.order?.userId }}</p>
        </div>
        
        <select v-model="disputeForm.reason" class="modal-select">
          <option value="">请选择纠纷原因</option>
          <option value="用户爽约">用户爽约</option>
          <option value="付款争议">付款争议</option>
          <option value="恶意差评">恶意差评</option>
          <option value="其他">其他</option>
        </select>
        
        <textarea 
          v-model="disputeForm.description" 
          placeholder="请详细描述问题..." 
          class="modal-textarea"
        ></textarea>
        
        <div class="modal-actions">
          <button class="btn-cancel" @click="disputeModal.visible = false">取消</button>
          <button class="btn-submit" @click="submitDispute">提交纠纷</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { getShopOrders, updateOrderStatus as updateOrderStatusApi } from '../api/order'
import { createDispute } from '../api/dispute'

const userStore = useUserStore()
const activeTab = ref('pending')
const pendingOrders = ref([])
const processingOrders = ref([])
const completedOrders = ref([])

// 格式化时间
const formatTime = (time) => {
  if (!time) return '未知'
  return new Date(time).toLocaleString()
}

// 加载订单
const loadOrders = async () => {
  const shopName = userStore.currentUser?.shopName
  if (!shopName) {
    console.error('当前用户没有绑定店铺')
    return
  }
  
  try {
    const res = await getShopOrders(shopName)
    if (res.code === 200) {
      const allOrders = res.data
      pendingOrders.value = allOrders.filter(o => o.status === 'pending')
      processingOrders.value = allOrders.filter(o => o.status === 'processing')
      completedOrders.value = allOrders.filter(o => o.status === 'completed')
    }
  } catch (error) {
    console.error('加载订单失败:', error)
  }
}

// 更新订单状态
const updateOrderStatus = async (order, newStatus) => {
  const statusText = {
    'processing': '确认接单',
    'completed': '标记完成',
    'cancelled': '拒绝'
  }
  
  if (!confirm(`确定要${statusText[newStatus]}订单「${order.serviceName}」吗？`)) return
  
  try {
    const res = await updateOrderStatusApi(order.id, newStatus)
    if (res.code === 200) {
      alert(`${statusText[newStatus]}成功`)
      loadOrders()
    } else {
      alert('操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    alert('操作失败')
  }
}

// 纠纷相关
const disputeModal = ref({ visible: false, order: null })
const disputeForm = ref({ reason: '', description: '' })

const showDisputeModal = (order) => {
  disputeModal.value = { visible: true, order: order }
  disputeForm.value = { reason: '', description: '' }
}

const submitDispute = async () => {
  if (!disputeForm.value.reason) {
    alert('请选择纠纷原因')
    return
  }
  if (!disputeForm.value.description) {
    alert('请填写详细描述')
    return
  }
  
  try {
    const res = await createDispute({
      orderId: disputeModal.value.order.id,
      initiatorId: userStore.currentUser?.id,
      targetId: disputeModal.value.order.userId,
      reason: disputeForm.value.reason,
      description: disputeForm.value.description
    })
    
    if (res.code === 200) {
      alert('纠纷已提交，管理员会尽快处理')
      disputeModal.value.visible = false
      
      // 标记订单已发起纠纷
      const order = completedOrders.value.find(o => o.id === disputeModal.value.order.id)
      if (order) order.disputeFiled = true
    } else {
      alert('提交失败')
    }
  } catch (error) {
    console.error('提交纠纷失败:', error)
    alert('提交失败')
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders {
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

.order-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.order-card.completed {
  background: #f8f9fa;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.order-no {
  font-size: 13px;
  color: #999;
}

.order-status {
  padding: 2px 10px;
  border-radius: 20px;
  font-size: 12px;
}

.status-pending {
  background: #e3f2fd;
  color: #2196f3;
}

.status-progress {
  background: #fff3e0;
  color: #ff9800;
}

.status-done {
  background: #e8f5e9;
  color: #2ecc71;
}

.order-service {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.order-detail {
  font-size: 14px;
  color: #555;
  margin-bottom: 6px;
}

.order-rating {
  font-size: 14px;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #eee;
}

.order-actions {
  display: flex;
  gap: 12px;
  margin-top: 12px;
}

.btn-confirm {
  background: #2ecc71;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 24px;
  cursor: pointer;
}

.btn-reject {
  background: #f0f0f0;
  border: none;
  padding: 8px 20px;
  border-radius: 24px;
  cursor: pointer;
  color: #ff4757;
}

.btn-complete {
  background: #ff9800;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 24px;
  cursor: pointer;
}

.btn-dispute {
  background: #ff4757;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 24px;
  cursor: pointer;
}

.dispute-status {
  margin-top: 12px;
}

.dispute-pending {
  background: #fff3e0;
  color: #ff9800;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
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

.order-info-mini {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 12px;
  margin-bottom: 16px;
}

.order-info-mini p {
  margin: 4px 0;
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
  font-family: inherit;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.btn-cancel {
  background: #f0f0f0;
  border: none;
  padding: 10px 20px;
  border-radius: 24px;
  cursor: pointer;
}

.btn-submit {
  background: #2ecc71;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 24px;
  cursor: pointer;
}
</style>