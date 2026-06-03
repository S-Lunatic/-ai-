<template>
  <div class="qualification-audit">
    <h1 class="page-title">资质审核</h1>
    
    <div class="stats-badge">
      <span class="badge">待审核：{{ pendingList.length }}条</span>
    </div>
    
    <div class="tabs">
      <div class="tab" :class="{ active: activeTab === 'merchant' }" @click="activeTab = 'merchant'">
        商户入驻申请 ({{ merchantApplications.length }})
      </div>
      <div class="tab" :class="{ active: activeTab === 'caregiver' }" @click="activeTab = 'caregiver'">
        护工认证申请 ({{ caregiverApplications.length }})
      </div>
    </div>
    
    <!-- 商户入驻申请 -->
    <div v-if="activeTab === 'merchant'" class="apply-list">
      <div class="apply-card" v-for="item in merchantApplications" :key="item.id">
        <div class="apply-header">
          <span class="shop-name">🏪 {{ item.shopName }}</span>
          <span class="apply-status">待审核</span>
        </div>
        <div class="apply-detail">营业执照号：{{ item.license }}</div>
        <div class="apply-detail">店铺地址：{{ item.shopAddress }}</div>
        <div class="apply-detail">联系电话：{{ item.shopPhone }}</div>
        <div class="apply-actions">
          <button class="btn-pass" @click="handleApprove(item.id)">✓ 通过</button>
          <button class="btn-reject" @click="handleReject(item.id)">✗ 驳回</button>
        </div>
      </div>
      <div v-if="merchantApplications.length === 0" class="empty-state">
        暂无商户入驻申请
      </div>
    </div>
    
    <!-- 护工认证申请 -->
    <div v-if="activeTab === 'caregiver'" class="apply-list">
      <div class="apply-card" v-for="item in caregiverApplications" :key="item.id">
        <div class="apply-header">
          <span class="shop-name">🤝 {{ item.realName }}</span>
          <span class="apply-status">待审核</span>
        </div>
        <div class="apply-detail">身份证号：{{ item.idCard }}</div>
        <div class="apply-detail">宠物经验：{{ item.experience || '无' }}</div>
        <div class="apply-actions">
          <button class="btn-pass" @click="handleApprove(item.id)">✓ 通过</button>
          <button class="btn-reject" @click="handleReject(item.id)">✗ 驳回</button>
        </div>
      </div>
      <div v-if="caregiverApplications.length === 0" class="empty-state">
        暂无护工认证申请
      </div>
    </div>

    <!-- 驳回原因弹窗 -->
    <div v-if="showRejectModal" class="modal" @click.self="showRejectModal = false">
      <div class="modal-content">
        <h3>驳回申请</h3>
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
import { ref, computed, onMounted } from 'vue'
import { getPendingCertifications, approveCertification, rejectCertification } from '../api/certification'

const activeTab = ref('merchant')
const pendingList = ref([])

// 驳回弹窗
const showRejectModal = ref(false)
const rejectId = ref(null)
const rejectReason = ref('')

// 按类型分类
const merchantApplications = computed(() => 
  pendingList.value.filter(item => item.type === 'merchant')
)
const caregiverApplications = computed(() => 
  pendingList.value.filter(item => item.type === 'caregiver')
)

// 加载待审核列表
const loadPendingList = async () => {
  try {
    const res = await getPendingCertifications()
    if (res.code === 200) {
      pendingList.value = res.data
      console.log('加载成功:', pendingList.value.length)
    }
  } catch (error) {
    console.error('加载待审核列表失败:', error)
  }
}

// 通过审核
const handleApprove = async (id) => {
  if (!confirm('确定要通过该申请吗？')) return
  
  try {
    const res = await approveCertification(id)
    if (res.code === 200) {
      alert('审核通过')
      loadPendingList()
    } else {
      alert('操作失败')
    }
  } catch (error) {
    console.error('审核失败:', error)
    alert('操作失败')
  }
}

// 驳回审核
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
    const res = await rejectCertification(rejectId.value, rejectReason.value)
    if (res.code === 200) {
      alert('已驳回')
      showRejectModal.value = false
      loadPendingList()
    } else {
      alert('操作失败')
    }
  } catch (error) {
    console.error('驳回失败:', error)
    alert('操作失败')
  }
}

onMounted(() => {
  loadPendingList()
})
</script>

<style scoped>
.qualification-audit { max-width: 800px; }
.page-title { font-size: 24px; margin-bottom: 24px; color: #1a3c34; }

.stats-badge { margin-bottom: 20px; }
.badge { background: white; padding: 8px 20px; border-radius: 30px; font-size: 14px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); display: inline-block; }

.tabs { display: flex; gap: 8px; margin-bottom: 24px; background: white; padding: 6px; border-radius: 48px; width: fit-content; }
.tab { padding: 8px 24px; border-radius: 40px; cursor: pointer; font-size: 14px; }
.tab.active { background: #2ecc71; color: white; }

.apply-card { background: white; border-radius: 16px; padding: 20px; margin-bottom: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.apply-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.shop-name { font-size: 16px; font-weight: 600; }
.apply-status { background: #fff3e0; color: #ff9800; padding: 2px 12px; border-radius: 20px; font-size: 12px; }
.apply-detail { font-size: 14px; color: #555; margin-bottom: 8px; }
.apply-actions { display: flex; gap: 12px; margin-top: 12px; }
.btn-pass { background: #2ecc71; color: white; border: none; padding: 6px 20px; border-radius: 24px; cursor: pointer; }
.btn-reject { background: #ff4757; color: white; border: none; padding: 6px 20px; border-radius: 24px; cursor: pointer; }
.empty-state { text-align: center; padding: 60px; color: #999; background: white; border-radius: 16px; }

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
  width: 400px;
  max-width: 90%;
}
.modal-content h3 { margin-bottom: 20px; color: #1a3c34; }
.modal-textarea { width: 100%; padding: 12px; margin-bottom: 16px; border: 1px solid #ddd; border-radius: 12px; font-size: 14px; min-height: 100px; resize: vertical; }
.modal-actions { display: flex; gap: 12px; justify-content: flex-end; }
.btn-cancel { background: #f0f0f0; border: none; padding: 10px 20px; border-radius: 24px; cursor: pointer; }
.btn-submit { background: #2ecc71; color: white; border: none; padding: 10px 20px; border-radius: 24px; cursor: pointer; }
</style>