<template>
  <div class="daily-record">
    <!-- 快速添加 -->
    <div class="quick-add">
      <span class="add-label">➕ 快速添加记录</span>
      <div class="add-buttons">
        <button class="add-btn" @click="showAddForm('饮食')">🍖 饮食</button>
        <button class="add-btn" @click="showAddForm('遛宠')">🚶 遛宠</button>
        <button class="add-btn" @click="showAddForm('便便')">💩 便便</button>
        <button class="add-btn" @click="goToAIRecognize">📷 AI识别</button>
      </div>
    </div>
    
    <!-- 左右布局 -->
    <div class="two-columns">
      <!-- 左侧：AI识别入口 -->
      <div class="ai-card" @click="goToAIRecognize">
        <h4>📸 AI识别</h4>
        <div class="camera-placeholder">
          <div class="camera-icon">📷</div>
          <div class="camera-text">点击拍照或上传图片</div>
          <div class="camera-hint">识别便便形态、皮肤状况等</div>
        </div>
        <div class="ai-example">
          <div class="example-title">💡 示例识别结果</div>
          <div class="example-item">便便形态：正常 ✅</div>
          <div class="example-item">健康评分：⭐⭐⭐⭐</div>
        </div>
      </div>
      
      <!-- 右侧：最近一周记录 -->
      <div class="timeline-card">
        <h4>📅 最近一周记录</h4>
        <div class="timeline">
          <div class="timeline-day" v-for="day in timeline" :key="day.fullDate">
            <div class="day-header">{{ day.date }} {{ day.weekday }}</div>
            <div class="day-records">
              <div class="record" v-for="record in day.records" :key="record.text">
                {{ record.icon }} {{ record.text }}
              </div>
            </div>
          </div>
          <div v-if="timeline.every(d => d.records.length === 0)" class="empty-timeline">
            暂无记录，点击上方按钮添加
          </div>
        </div>
      </div>
    </div>
    
    <!-- 粮余量提醒卡片 -->
    <div class="reminder-card" :class="{ warning: foodStock < 500, danger: foodStock < 200 }">
      <div class="reminder-icon">🛒</div>
      <div class="reminder-content">
        <div class="reminder-title">粮余量管理</div>
        <div class="stock-info">
          当前余量：<strong>{{ foodStock }}g</strong>
          <span v-if="foodStock < 500 && foodStock >= 200" class="stock-warning">⚠️ 建议补充</span>
          <span v-if="foodStock < 200" class="stock-danger">❗ 严重不足</span>
          <span v-else class="stock-normal">✅ 充足</span>
        </div>
        <div class="stock-actions">
          <button class="btn-update" @click="showUpdateStockModal">📝 更新余量</button>
          <button class="btn-buy" @click="goToShop" v-if="foodStock < 500">🛒 立即购买</button>
        </div>
      </div>
    </div>

    <!-- 更新余量弹窗 -->
    <div v-if="showStockModal" class="modal" @click.self="closeStockModal">
      <div class="modal-content">
        <h3>📝 更新粮余量</h3>
        <select v-model="stockForm.petId" class="modal-select">
          <option value="1">豆豆（金毛）</option>
          <option value="2">咪咪（英短）</option>
        </select>
        <input type="number" v-model="stockForm.amount" placeholder="当前余量（g）" class="modal-input" step="100">
        <div class="modal-actions">
          <button class="btn-cancel" @click="closeStockModal">取消</button>
          <button class="btn-submit" @click="updateStock">保存</button>
        </div>
      </div>
    </div>

    <!-- 快速添加记录弹窗 -->
    <div v-if="showRecordModal" class="modal" @click.self="closeRecordModal">
      <div class="modal-content">
        <h3>📝 添加{{ recordType }}记录</h3>
        
        <!-- 饮食表单 -->
        <template v-if="recordType === '饮食'">
          <select v-model="recordForm.petId" class="modal-select">
            <option value="1">豆豆（金毛）</option>
            <option value="2">咪咪（英短）</option>
          </select>
          <input type="date" v-model="recordForm.date" class="modal-input">
          <input type="text" v-model="recordForm.foodType" placeholder="食物类型（如：狗粮、罐头）" class="modal-input">
          <input type="number" v-model="recordForm.amount" placeholder="数量（g）" class="modal-input" step="10">
          <input type="time" v-model="recordForm.time" class="modal-input">
        </template>
        
        <!-- 遛宠表单 -->
        <template v-if="recordType === '遛宠'">
          <select v-model="recordForm.petId" class="modal-select">
            <option value="1">豆豆（金毛）</option>
            <option value="2">咪咪（英短）</option>
          </select>
          <input type="date" v-model="recordForm.date" class="modal-input">
          <input type="number" v-model="recordForm.duration" placeholder="时长（分钟）" class="modal-input" step="5">
          <input type="text" v-model="recordForm.location" placeholder="地点" class="modal-input">
          <input type="time" v-model="recordForm.time" class="modal-input">
        </template>
        
        <!-- 便便表单 -->
        <template v-if="recordType === '便便'">
          <select v-model="recordForm.petId" class="modal-select">
            <option value="1">豆豆（金毛）</option>
            <option value="2">咪咪（英短）</option>
          </select>
          <input type="date" v-model="recordForm.date" class="modal-input">
          <select v-model="recordForm.consistency" class="modal-select">
            <option value="">便便形态</option>
            <option value="正常">正常 ✅</option>
            <option value="偏软">偏软 ⚠️</option>
            <option value="偏硬">偏硬 ⚠️</option>
            <option value="腹泻">腹泻 ❌</option>
          </select>
          <select v-model="recordForm.color" class="modal-select">
            <option value="">便便颜色</option>
            <option value="黄褐色">黄褐色（正常）</option>
            <option value="黑色">黑色（可能消化道出血）</option>
            <option value="红色">红色（可能肠道问题）</option>
            <option value="灰色">灰色（可能肝胆问题）</option>
          </select>
          <input type="time" v-model="recordForm.time" class="modal-input">
        </template>
        
        <div class="modal-actions">
          <button class="btn-cancel" @click="closeRecordModal">取消</button>
          <button class="btn-submit" @click="saveRecord">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// ========== 粮余量管理 ==========
const foodStock = ref(350)

const loadFoodStock = () => {
  const stored = localStorage.getItem('foodStock')
  if (stored !== null) {
    foodStock.value = parseInt(stored)
  } else {
    foodStock.value = 350
    localStorage.setItem('foodStock', foodStock.value)
  }
}

const saveFoodStock = (value) => {
  foodStock.value = value
  localStorage.setItem('foodStock', value)
}

const showStockModal = ref(false)
const stockForm = ref({ petId: '1', amount: '' })

const showUpdateStockModal = () => {
  stockForm.value = { petId: '1', amount: foodStock.value }
  showStockModal.value = true
}

const closeStockModal = () => {
  showStockModal.value = false
}

const updateStock = () => {
  const amount = parseInt(stockForm.value.amount)
  if (isNaN(amount) || amount < 0) {
    alert('请输入有效的余量')
    return
  }
  saveFoodStock(amount)
  closeStockModal()
  alert(`已更新粮余量为 ${amount}g`)
}

const goToShop = () => {
  router.push('/service-hall')
}

// ========== 跳转AI识别 ==========
const goToAIRecognize = () => {
  router.push('/ai-recognize')
}

// ========== 最近一周记录 ==========
const timeline = ref([])

const loadTimeline = () => {
  const allRecords = JSON.parse(localStorage.getItem('dailyRecords') || '[]')
  
  const today = new Date()
  const last7Days = []
  for (let i = 6; i >= 0; i--) {
    const date = new Date()
    date.setDate(today.getDate() - i)
    const dateStr = `${date.getMonth() + 1}月${date.getDate()}日`
    const weekday = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'][date.getDay()]
    last7Days.push({ 
      date: dateStr, 
      weekday, 
      records: [], 
      fullDate: date.toISOString().slice(0, 10)
    })
  }
  
  for (const record of allRecords) {
    const recordDate = record.date || record.timestamp?.slice(0, 10) || new Date(record.id).toISOString().slice(0, 10)
    const dayItem = last7Days.find(d => d.fullDate === recordDate)
    if (dayItem) {
      let icon = '📝'
      let text = ''
      if (record.type === '饮食') {
        icon = '🍖'
        text = `${record.foodType} ${record.amount}g · ${record.time}`
      } else if (record.type === '遛宠') {
        icon = '🚶'
        text = `遛宠 ${record.duration}分钟 · ${record.time}`
      } else if (record.type === '便便') {
        icon = '💩'
        const consistencyText = {
          '正常': '正常 ✅',
          '偏软': '偏软 ⚠️',
          '偏硬': '偏硬 ⚠️',
          '腹泻': '腹泻 ❌'
        }
        text = `便便 ${consistencyText[record.consistency] || record.consistency} · ${record.time}`
      }
      dayItem.records.push({ icon, text })
    }
  }
  
  timeline.value = last7Days
}

// ========== 快速添加记录弹窗 ==========
const showRecordModal = ref(false)
const recordType = ref('')
const recordForm = ref({
  petId: '1',
  date: new Date().toISOString().slice(0, 10),
  foodType: '',
  amount: '',
  duration: '',
  location: '',
  consistency: '',
  color: '',
  time: new Date().toLocaleTimeString().slice(0, 5)
})

const showAddForm = (type) => {
  recordType.value = type
  recordForm.value = {
    petId: '1',
    date: new Date().toISOString().slice(0, 10),
    foodType: '',
    amount: '',
    duration: '',
    location: '',
    consistency: '',
    color: '',
    time: new Date().toLocaleTimeString().slice(0, 5)
  }
  showRecordModal.value = true
}

const closeRecordModal = () => {
  showRecordModal.value = false
}

const saveRecord = () => {
  const petName = recordForm.value.petId === '1' ? '豆豆' : '咪咪'
  let recordText = ''
  
  if (recordType.value === '饮食') {
    if (!recordForm.value.foodType || !recordForm.value.amount) {
      alert('请填写食物类型和数量')
      return
    }
    recordText = `${recordForm.value.foodType} ${recordForm.value.amount}g · ${recordForm.value.time}`
    
    // 扣减粮余量
    const newStock = Math.max(0, foodStock.value - parseInt(recordForm.value.amount))
    saveFoodStock(newStock)
    alert(`已扣除 ${recordForm.value.amount}g 粮食，当前余量 ${newStock}g`)
    
  } else if (recordType.value === '遛宠') {
    if (!recordForm.value.duration) {
      alert('请填写时长')
      return
    }
    recordText = `遛宠 ${recordForm.value.duration}分钟 · ${recordForm.value.time}`
  } else if (recordType.value === '便便') {
    if (!recordForm.value.consistency) {
      alert('请选择便便形态')
      return
    }
    const consistencyText = {
      '正常': '正常 ✅',
      '偏软': '偏软 ⚠️',
      '偏硬': '偏硬 ⚠️',
      '腹泻': '腹泻 ❌'
    }
    recordText = `便便 ${consistencyText[recordForm.value.consistency]} · ${recordForm.value.time}`
  }
  
  // 保存到 localStorage
  const records = JSON.parse(localStorage.getItem('dailyRecords') || '[]')
  records.push({
    id: Date.now(),
    petId: recordForm.value.petId,
    type: recordType.value,
    date: recordForm.value.date,
    timestamp: new Date(recordForm.value.date).toISOString(),
    dateTime: `${recordForm.value.date} ${recordForm.value.time}`,
    ...recordForm.value
  })
  localStorage.setItem('dailyRecords', JSON.stringify(records))
  
  // 刷新最近一周记录
  loadTimeline()
  
  alert(`已为「${petName}」添加${recordType.value}记录：\n${recordText}`)
  closeRecordModal()
}

// 初始化加载
onMounted(() => {
  loadFoodStock()
  loadTimeline()
})
</script>

<style scoped>
.daily-record {
  max-width: 1200px;
}

.quick-add {
  background: white;
  border-radius: 16px;
  padding: 20px 24px;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.add-label {
  font-weight: 600;
  color: #1a3c34;
}

.add-buttons {
  display: flex;
  gap: 12px;
}

.add-btn {
  padding: 8px 20px;
  background: #f0f0f0;
  border: none;
  border-radius: 24px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.add-btn:hover {
  background: #2ecc71;
  color: white;
}

.two-columns {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.ai-card {
  background: white;
  border-radius: 16px;
  padding: 20px 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.2s;
}

.ai-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.ai-card h4 {
  font-size: 18px;
  color: #1a3c34;
  margin-bottom: 16px;
}

.camera-placeholder {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  border: 2px dashed #ddd;
  margin-bottom: 16px;
}

.camera-icon {
  font-size: 48px;
  margin-bottom: 8px;
}

.camera-text {
  color: #999;
  font-size: 14px;
  margin-bottom: 4px;
}

.camera-hint {
  color: #ccc;
  font-size: 12px;
}

.ai-example {
  background: #e8f5e9;
  border-radius: 12px;
  padding: 16px;
}

.example-title {
  font-weight: 600;
  font-size: 13px;
  margin-bottom: 8px;
  color: #2ecc71;
}

.example-item {
  font-size: 13px;
  color: #555;
  padding: 4px 0;
}

.timeline-card {
  background: white;
  border-radius: 16px;
  padding: 20px 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  overflow-y: auto;
  max-height: 500px;
}

.timeline-card h4 {
  font-size: 18px;
  color: #1a3c34;
  margin-bottom: 16px;
}

.timeline-day {
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.day-header {
  font-weight: 600;
  color: #1a3c34;
  margin-bottom: 8px;
  font-size: 14px;
}

.record {
  font-size: 13px;
  color: #666;
  padding: 4px 0;
  padding-left: 8px;
}

.empty-timeline {
  text-align: center;
  padding: 40px;
  color: #999;
}

/* 粮余量卡片样式 */
.reminder-card {
  background: linear-gradient(135deg, #fff8e1, #ffecb3);
  border-radius: 16px;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  transition: all 0.3s;
}

.reminder-card.warning {
  background: linear-gradient(135deg, #fff8e1, #ffe0b2);
  border-left: 4px solid #ff9800;
}

.reminder-card.danger {
  background: linear-gradient(135deg, #ffebee, #ffcdd2);
  border-left: 4px solid #f44336;
}

.reminder-icon {
  font-size: 32px;
}

.reminder-content {
  flex: 1;
}

.reminder-title {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 8px;
}

.stock-info {
  font-size: 14px;
  margin-bottom: 12px;
}

.stock-warning {
  color: #ff9800;
  margin-left: 8px;
}

.stock-danger {
  color: #f44336;
  margin-left: 8px;
  font-weight: 600;
}

.stock-normal {
  color: #2ecc71;
  margin-left: 8px;
}

.stock-actions {
  display: flex;
  gap: 12px;
}

.btn-update {
  background: #2196f3;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 13px;
}

.btn-buy {
  background: #ff9800;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 13px;
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