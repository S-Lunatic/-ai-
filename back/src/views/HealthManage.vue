<template>
  <div class="health-manage">
    <!-- 提醒卡片 -->
    <div class="reminder-card">
      <div class="reminder-title">💉 疫苗/驱虫提醒</div>
      <div class="reminder-list">
        <div class="reminder-item warning">🔔 狂犬疫苗：剩余7天 ⚠️</div>
        <div class="reminder-item danger">🔔 体内驱虫：已过期 ⚠️⚠️</div>
      </div>
      <button class="appoint-btn" @click="goToAppointment">📅 一键预约复诊</button>
    </div>
    
    <!-- 体重趋势 + 医疗记录 左右布局 -->
    <div class="two-columns">
      <!-- 左侧：体重趋势 -->
      <div class="weight-card">
        <h4>📈 体重趋势（近30天）</h4>
        <div id="weightChart" class="chart-container" v-if="showChart"></div>
        <div v-else class="chart-placeholder">暂无体重数据，请添加体重记录</div>
        <div class="ai-warning" v-if="aiWarning">
          ⚠️ AI预警：{{ aiWarning }}
        </div>
        <button class="add-weight" @click="openAddWeightModal">+ 手动添加体重</button>
      </div>
      
      <!-- 右侧：医疗记录 -->
      <div class="medical-card">
        <h4>📋 医疗记录（可追溯）</h4>
        <div class="medical-list">
          <div class="medical-item" v-for="record in medicalRecords" :key="record.id">
            <div class="medical-date">{{ record.date }}</div>
            <div class="medical-info">{{ record.info }}</div>
          </div>
        </div>
        <a href="#" class="more-link" @click.prevent="goToMedicalRecords">查看全部医疗记录 &gt;</a>
      </div>
    </div>
    
    <!-- 医疗云档案共享摘要 -->
    <div class="share-card">
      <h4>🔗 医疗云档案共享摘要</h4>
      <div class="share-info">
        已授权医院：安心宠物医院（病历可查看）
      </div>
      <div class="share-info">
        最近共享：2026-03-01 疫苗记录被查看
      </div>
    </div>

    <!-- 添加体重弹窗 -->
    <div v-if="showAddWeightModal" class="modal" @click.self="closeAddWeightModal">
      <div class="modal-content">
        <h3>📊 添加体重记录</h3>
        <select v-model="weightForm.petId" class="modal-select">
          <option v-for="pet in pets" :key="pet.id" :value="pet.id">{{ pet.name }}（{{ pet.species }}）</option>
        </select>
        <input type="number" v-model="weightForm.weight" placeholder="体重（kg）" class="modal-input" step="0.1">
        <input type="date" v-model="weightForm.date" class="modal-input">
        <input type="text" v-model="weightForm.remark" placeholder="备注（选填）" class="modal-input">
        <div class="modal-actions">
          <button class="btn-cancel" @click="closeAddWeightModal">取消</button>
          <button class="btn-submit" @click="saveWeight">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { getPets } from '../api/pet'
import { getRecentWeight, addWeight as addWeightApi } from '../api/weight'
import * as echarts from 'echarts'
import { nextTick } from 'vue'

const router = useRouter()
const userStore = useUserStore()

// 宠物列表
const pets = ref([])
const selectedPetId = ref(null)

// 体重数据
const weightData = ref([])
const showChart = ref(false)
const aiWarning = ref('')

// 医疗记录（假数据，后续对接）
const medicalRecords = ref([
  { id: 1, date: '2026-03-01', info: '疫苗第三针 · 安心医院 · 李医生' },
  { id: 2, date: '2026-02-10', info: '年度体检 · 报告：各项正常' }
])

// 加载宠物列表
const loadPets = async () => {
  try {
    const res = await getPets(userStore.currentUser?.id)
    if (res.code === 200) {
      pets.value = res.data
      if (pets.value.length > 0) {
        selectedPetId.value = pets.value[0].id
        loadWeightData()
      }
    }
  } catch (error) {
    console.error('加载宠物失败:', error)
  }
}

// 加载体重数据并渲染图表
const loadWeightData = async () => {
  if (!selectedPetId.value) return
  
  try {
    const res = await getRecentWeight(selectedPetId.value)
    if (res.code === 200) {
      weightData.value = res.data
      showChart.value = weightData.value.length > 0
        await nextTick()  // 等待 DOM 更新
      renderChart()
      calculateWarning()
    }
  } catch (error) {
    console.error('加载体重失败:', error)
  }
}

// 渲染图表
let chart = null
const renderChart = () => {
  if (weightData.value.length === 0) return
  
  const chartDom = document.getElementById('weightChart')
   if (!chartDom) {
    // 如果 DOM 还没准备好，延迟重试
    setTimeout(() => renderChart(), 100)
    return
  }
  
  if (chart) {
    chart.dispose()
  }
  chart = echarts.init(chartDom)
  
  const dates = weightData.value.map(w => w.recordDate?.slice(5) || '')
  const weights = weightData.value.map(w => w.weight)
  
  const option = {
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: dates,
      name: '日期'
    },
    yAxis: {
      type: 'value',
      name: '体重 (kg)'
    },
    series: [{
      data: weights,
      type: 'line',
      smooth: true,
      lineStyle: { color: '#2ecc71', width: 3 },
      areaStyle: { opacity: 0.1, color: '#2ecc71' },
      symbol: 'circle',
      symbolSize: 8
    }]
  }
  chart.setOption(option)
}

// 计算AI预警
const calculateWarning = () => {
  if (weightData.value.length < 2) {
    aiWarning.value = '数据不足，无法分析趋势'
    return
  }
  const latest = weightData.value[weightData.value.length - 1].weight
  const earliest = weightData.value[0].weight
  const change = ((latest - earliest) / earliest * 100).toFixed(1)
  
  if (change < -5) {
    aiWarning.value = `近30天体重下降${Math.abs(change)}%，建议关注饮食状况`
  } else if (change > 5) {
    aiWarning.value = `近30天体重上升${change}%，建议增加运动量`
  } else {
    aiWarning.value = `近30天体重大体稳定，变化${change}%`
  }
}

// 添加体重弹窗
const showAddWeightModal = ref(false)
const weightForm = ref({
  petId: null,
  weight: '',
  date: new Date().toISOString().slice(0, 10),
  remark: ''
})

const openAddWeightModal = () => {
  weightForm.value = {
    petId: selectedPetId.value,
    weight: '',
    date: new Date().toISOString().slice(0, 10),
    remark: ''
  }
  showAddWeightModal.value = true
}

const closeAddWeightModal = () => {
  showAddWeightModal.value = false
}

const saveWeight = async () => {
  if (!weightForm.value.weight) {
    alert('请填写体重')
    return
  }
  if (!weightForm.value.petId) {
    alert('请选择宠物')
    return
  }
  
  try {
    const res = await addWeightApi({
      petId: weightForm.value.petId,
      weight: parseFloat(weightForm.value.weight),
      recordDate: weightForm.value.date,
      remark: weightForm.value.remark
    })
    
    if (res.code === 200) {
      alert('体重记录已添加')
      closeAddWeightModal()
      loadWeightData()
    } else {
      alert('添加失败')
    }
  } catch (error) {
    console.error('添加体重失败:', error)
    alert('添加失败')
  }
}

// 跳转函数
const goToAppointment = () => {
  router.push('/service-hall?tab=merchant')
}

const goToMedicalRecords = () => {
  router.push('/medical-records')
}

// 窗口大小变化时重新调整图表
window.addEventListener('resize', () => {
  if (chart) chart.resize()
})

onMounted(() => {
  loadPets()
})
</script>

<style scoped>
.health-manage {
  max-width: 1200px;
}

.reminder-card {
  background: linear-gradient(135deg, #fff3e0, #ffe0b2);
  border-radius: 16px;
  padding: 20px 24px;
  margin-bottom: 24px;
}

.reminder-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
}

.reminder-list {
  margin-bottom: 16px;
}

.reminder-item {
  padding: 8px 0;
  font-size: 14px;
}

.reminder-item.warning {
  color: #ff9800;
}

.reminder-item.danger {
  color: #f44336;
}

.appoint-btn {
  background: #2ecc71;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 24px;
  cursor: pointer;
  font-weight: 500;
}

.two-columns {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.weight-card, .medical-card, .share-card {
  background: white;
  border-radius: 16px;
  padding: 20px 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.weight-card h4, .medical-card h4, .share-card h4 {
  font-size: 18px;
  color: #1a3c34;
  margin-bottom: 16px;
}

.chart-container {
  width: 100%;
  height: 300px;
  margin-bottom: 16px;
}

.chart-placeholder {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 60px;
  text-align: center;
  color: #999;
  margin-bottom: 16px;
}

.ai-warning {
  background: #fff3e0;
  padding: 12px;
  border-radius: 8px;
  font-size: 13px;
  color: #ff9800;
  margin-bottom: 12px;
}

.add-weight {
  background: none;
  border: 1px solid #2ecc71;
  color: #2ecc71;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
}

.medical-list {
  margin-bottom: 16px;
}

.medical-item {
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  gap: 16px;
}

.medical-date {
  width: 100px;
  color: #999;
  font-size: 13px;
}

.medical-info {
  flex: 1;
  font-size: 14px;
  color: #555;
}

.share-info {
  padding: 8px 0;
  font-size: 14px;
  color: #555;
}

.more-link {
  color: #2ecc71;
  text-decoration: none;
  font-size: 13px;
  cursor: pointer;
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
  width: 400px;
  max-width: 90%;
}

.modal-content h3 {
  margin-bottom: 20px;
  color: #1a3c34;
}

.modal-input, .modal-select {
  width: 100%;
  padding: 12px;
  margin-bottom: 16px;
  border: 1px solid #ddd;
  border-radius: 12px;
  font-size: 14px;
  box-sizing: border-box;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 8px;
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