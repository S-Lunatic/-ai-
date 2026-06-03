<template>
  <div class="pet-profile">
    <!-- 宠物切换栏 -->
    <div class="pet-tabs">
      <div 
        v-for="pet in pets" 
        :key="pet.id"
        class="pet-tab"
        :class="{ active: currentPet?.id === pet.id }"
        @click="selectPet(pet)"
      >
        <span class="pet-avatar">{{ pet.species === '金毛' ? '🐕' : '🐈' }}</span>
        <span class="pet-name">{{ pet.name }}</span>
      </div>
      <div class="pet-tab add-pet" @click="openAddPetModal">
        <span class="pet-avatar">➕</span>
        <span class="pet-name">添加</span>
      </div>
    </div>
    
    <!-- 当前宠物详情 -->
    <div v-if="currentPet" class="pet-detail">
      <!-- 基本信息 -->
      <div class="info-card">
        <h4>📋 基本信息</h4>
        <div class="info-grid">
          <div class="info-item"><label>名字：</label>{{ currentPet.name }}</div>
          <div class="info-item"><label>品种：</label>{{ currentPet.species }}</div>
          <div class="info-item"><label>年龄：</label>{{ currentPet.age }}岁</div>
          <div class="info-item"><label>芯片号：</label>{{ currentPet.chipNumber || '未录入' }}</div>
        </div>
      </div>
      
      <!-- 医疗云档案 -->
      <div class="info-card">
        <h4>🏥 医疗云档案</h4>
        <button class="auth-btn" @click="openAuthModal">+ 授权医院/护工查看</button>
        <div class="auth-list">
        <div class="auth-tag" v-for="auth in authorizedList" :key="auth.id">
  <span>✅ {{ auth.sharedWithName }}（{{ auth.sharedWithType === 'hospital' ? '医院' : '护工' }}）</span>
  <span v-if="auth.expireAt" class="auth-expire">到期：{{ formatDate(auth.expireAt) }}</span>
  <span class="auth-remove" @click="removeAuth(auth.id)">✖</span>
</div>
          <span v-if="authorizedList.length === 0" class="auth-empty">暂无授权</span>
        </div>
      </div>
      
      <!-- 健康数据互通 -->
      <div class="info-card">
        <h4>📊 健康数据互通</h4>
        <div class="health-stats">
          <div class="stat">最近体重：{{ latestWeight }}kg ({{ weightDate }})</div>
          <div class="stat">下次疫苗：2026-06-15 (剩余34天)</div>
          <div class="stat">下次驱虫：2026-05-20 (剩余9天)</div>
        </div>
        <button class="goto-btn" @click="goToHealth">前往健康管理 &gt;</button>
      </div>
      
      <!-- 最近医疗记录 -->
      <div class="info-card">
        <h4>📑 最近医疗记录</h4>
        <div class="record-item">
          <span class="record-date">2025-12-01</span>
          <span class="record-hospital">安心医院</span>
          <span class="record-desc">皮肤科 · 过敏治疗</span>
        </div>
        <div class="record-item">
          <span class="record-date">2025-08-10</span>
          <span class="record-hospital">安心医院</span>
          <span class="record-desc">年度体检 · 健康</span>
        </div>
        <a href="#" class="more-link" @click.prevent="goToMedicalRecords">查看全部 &gt;</a>
      </div>
      
      <!-- 最近日常记录 -->
      <div class="info-card">
        <h4>📝 最近日常记录</h4>
        <div class="record-item">
          <span class="record-date">昨天 18:30</span>
          <span class="record-desc">便便正常 ⭐⭐⭐⭐</span>
          <span class="photo-icon">📷</span>
        </div>
        <div class="record-item">
          <span class="record-date">昨天 08:00</span>
          <span class="record-desc">狗粮150g</span>
        </div>
        <a href="#" class="more-link" @click.prevent="goToDailyRecords">查看全部 &gt;</a>
      </div>
    </div>

    <!-- 添加宠物弹窗 -->
    <div v-if="showAddPetModal" class="modal" @click.self="closeAddPetModal">
      <div class="modal-content">
        <h3>➕ 添加宠物</h3>
        <input type="text" v-model="newPet.name" placeholder="宠物名称 *" class="modal-input">
        <input type="text" v-model="newPet.species" placeholder="品种 *" class="modal-input">
        <input type="text" v-model="newPet.age" placeholder="年龄（岁）" class="modal-input">
        <input type="text" v-model="newPet.chipNumber" placeholder="芯片号（选填）" class="modal-input">
        <div class="modal-actions">
          <button class="btn-cancel" @click="closeAddPetModal">取消</button>
          <button class="btn-submit" @click="savePet">保存</button>
        </div>
      </div>
    </div>

    <!-- 授权弹窗 -->
    <div v-if="showAuthModal" class="modal" @click.self="closeAuthModal">
      <div class="modal-content">
        <h3>🔐 授权医疗云档案查看</h3>
        <div class="auth-select-area">
          <div class="auth-select-title">选择授权对象：</div>
          <div class="auth-options">
            <label class="auth-option">
              <input type="radio" v-model="authType" value="hospital"> 🏥 医院
            </label>
            <label class="auth-option">
              <input type="radio" v-model="authType" value="caregiver"> 🤝 护工
            </label>
          </div>
          
          <!-- 选择医院 -->
          <select v-if="authType === 'hospital'" v-model="selectedHospital" class="modal-select">
            <option value="">请选择医院</option>
            <option v-for="hospital in hospitals" :key="hospital.id" :value="hospital.id">
              {{ hospital.name }}
            </option>
          </select>
          
          <!-- 选择护工 -->
          <select v-if="authType === 'caregiver'" v-model="selectedCaregiver" class="modal-select">
            <option value="">请选择护工</option>
            <option v-for="caregiver in caregivers" :key="caregiver.id" :value="caregiver.id">
              {{ caregiver.name }}（好评率{{ caregiver.rating }}%）
            </option>
          </select>
          
          <div class="auth-expire">
            <label>授权有效期：</label>
            <select v-model="expireDays" class="expire-select">
              <option value="7">7天</option>
              <option value="30">30天</option>
              <option value="90">90天</option>
              <option value="365">永久</option>
            </select>
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="closeAuthModal">取消</button>
          <button class="btn-submit" @click="saveAuth">确认授权</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { getPetList, addPet } from '../api/pet'
import { getPetShares, addPetShare, revokePetShare } from '../api/pet'
import { getLatestWeight } from '../api/weight'

const router = useRouter()
const userStore = useUserStore()

// 宠物列表
const pets = ref([])
const currentPet = ref(null)
const currentPetWeight = ref(null)
const currentPetWeightDate = ref('')

// 最新体重
const latestWeight = ref('--')
const weightDate = ref('')

// 加载最新体重
const loadLatestWeight = async (petId) => {
  try {
    const res = await getLatestWeight(petId)
    if (res.code === 200 && res.data) {
      currentPetWeight.value = res.data.weight
      latestWeight.value = res.data.weight
      weightDate.value = res.data.recordDate || '未知'
    } else {
      latestWeight.value = '暂无'
      weightDate.value = '无记录'
    }
  } catch (error) {
    console.error('加载体重失败:', error)
  }
}

// 授权列表
const authorizedList = ref([])

// 加载授权列表
const loadAuthorizedList = async (petId) => {
  try {
    const res = await getPetShares(petId)
    if (res.code === 200) {
      authorizedList.value = res.data
    }
  } catch (error) {
    console.error('加载授权列表失败:', error)
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

// 可授权的医院列表（模拟数据，后续从后端获取）
const hospitals = ref([
  { id: 1, name: '安心宠物医院' },
  { id: 2, name: '瑞康动物医院' },
  { id: 3, name: '宠乐宠物医院' }
])

// 可授权的护工列表（模拟数据）
const caregivers = ref([
  { id: 2, name: '李护工', rating: 98 },
  { id: 6, name: '王护工', rating: 95 },
  { id: 7, name: '张护工', rating: 92 }
])

// 授权弹窗
const showAuthModal = ref(false)
const authType = ref('hospital')
const selectedHospital = ref('')
const selectedCaregiver = ref('')
const expireDays = ref(30)

// 加载宠物列表
const loadPets = async () => {
  const userId = userStore.currentUser?.id
  if (!userId) return
  
  try {
    const res = await getPetList(userId)
    if (res.code === 200) {
      pets.value = res.data
      if (pets.value.length > 0) {
        currentPet.value = pets.value[0]
        loadLatestWeight(currentPet.value.id)
        loadAuthorizedList(currentPet.value.id)
      }
    }
  } catch (error) {
    console.error('加载宠物失败:', error)
  }
}

// 切换宠物
const selectPet = (pet) => {
  currentPet.value = pet
  loadLatestWeight(pet.id)
  loadAuthorizedList(pet.id)
}

// 跳转健康管理
const goToHealth = () => {
  router.push('/health-manage')
}

const goToMedicalRecords = () => {
  router.push('/medical-records')
}

const goToDailyRecords = () => {
  router.push('/daily-record')
}

// 添加宠物
const showAddPetModal = ref(false)
const newPet = ref({
  name: '',
  species: '',
  age: '',
  chipNumber: ''
})

const openAddPetModal = () => {
  showAddPetModal.value = true
}

const closeAddPetModal = () => {
  showAddPetModal.value = false
  newPet.value = { name: '', species: '', age: '', chipNumber: '' }
}

const savePet = async () => {
  if (!newPet.value.name || !newPet.value.species) {
    alert('请填写宠物名称和品种')
    return
  }
  
  try {
    const res = await addPet({
      userId: userStore.currentUser?.id,
      name: newPet.value.name,
      species: newPet.value.species,
      age: newPet.value.age || 0,
      chipNumber: newPet.value.chipNumber || ''
    })
    
    if (res.code === 200) {
      alert(`宠物「${newPet.value.name}」添加成功`)
      closeAddPetModal()
      loadPets()
    } else {
      alert('添加失败')
    }
  } catch (error) {
    console.error('添加宠物失败:', error)
    alert('添加失败')
  }
}

// 授权相关
const openAuthModal = () => {
  authType.value = 'hospital'
  selectedHospital.value = ''
  selectedCaregiver.value = ''
  expireDays.value = 30
  showAuthModal.value = true
}

const closeAuthModal = () => {
  showAuthModal.value = false
}

const saveAuth = async () => {
  let targetId = null
  let targetType = null
  let targetName = ''
  
  if (authType.value === 'hospital') {
    if (!selectedHospital.value) {
      alert('请选择医院')
      return
    }
    targetId = parseInt(selectedHospital.value)
    targetType = 'hospital'
    const hospital = hospitals.value.find(h => h.id === targetId)
    targetName = hospital?.name || '医院'
  } else {
    if (!selectedCaregiver.value) {
      alert('请选择护工')
      return
    }
    targetId = parseInt(selectedCaregiver.value)
    targetType = 'caregiver'
    const caregiver = caregivers.value.find(c => c.id === targetId)
    targetName = caregiver?.name || '护工'
  }
  
  try {
    const res = await addPetShare({
      petId: currentPet.value.id,
      sharedWithId: targetId,
      sharedWithType: targetType,
      expireDays: parseInt(expireDays.value)
    })
    
    if (res.code === 200) {
      alert(`已授权「${targetName}」查看「${currentPet.value.name}」的医疗档案`)
      closeAuthModal()
      loadAuthorizedList(currentPet.value.id)
    } else {
      alert('授权失败')
    }
  } catch (error) {
    console.error('授权失败:', error)
    alert('授权失败')
  }
}

const removeAuth = async (id) => {
  if (!confirm('确定要取消该授权吗？')) return
  
  try {
    const res = await revokePetShare(id)
    if (res.code === 200) {
      alert('已取消授权')
      loadAuthorizedList(currentPet.value.id)
    } else {
      alert('操作失败')
    }
  } catch (error) {
    console.error('取消授权失败:', error)
    alert('操作失败')
  }
}

onMounted(() => {
  loadPets()
})
</script>

<style scoped>
.pet-profile {
  max-width: 1000px;
}

.pet-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 28px;
  flex-wrap: wrap;
}

.pet-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: white;
  border-radius: 40px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.2s;
}

.pet-tab.active {
  background: #2ecc71;
  color: white;
  box-shadow: 0 4px 12px rgba(46, 204, 113, 0.3);
}

.pet-tab.add-pet {
  background: #f0f0f0;
}

.pet-avatar {
  font-size: 24px;
}

.pet-name {
  font-size: 15px;
  font-weight: 500;
}

.info-card {
  background: white;
  border-radius: 16px;
  padding: 20px 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.info-card h4 {
  font-size: 18px;
  color: #1a3c34;
  margin-bottom: 16px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.info-item {
  font-size: 14px;
  color: #555;
}

.info-item label {
  font-weight: 600;
  color: #333;
}

.auth-btn {
  background: #f0f0f0;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 13px;
  margin-bottom: 12px;
}

.auth-list {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.auth-tag {
  background: #e8f5e9;
  color: #2ecc71;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 13px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.auth-expire {
  font-size: 11px;
  color: #999;
}

.auth-remove {
  cursor: pointer;
  font-size: 14px;
  color: #ff4757;
  margin-left: 4px;
}

.auth-empty {
  font-size: 13px;
  color: #999;
}

.health-stats {
  margin-bottom: 12px;
}

.stat {
  font-size: 14px;
  color: #555;
  padding: 6px 0;
}

.goto-btn {
  background: none;
  border: none;
  color: #2ecc71;
  cursor: pointer;
  font-size: 13px;
}

.record-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}

.record-date {
  width: 100px;
  color: #999;
}

.record-hospital {
  width: 100px;
  color: #555;
}

.record-desc {
  flex: 1;
  color: #333;
}

.photo-icon {
  cursor: pointer;
  color: #2ecc71;
}

.more-link {
  display: inline-block;
  margin-top: 12px;
  color: #2ecc71;
  text-decoration: none;
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
  width: 450px;
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

.auth-select-area {
  margin-bottom: 20px;
}

.auth-select-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 12px;
  color: #333;
}

.auth-options {
  display: flex;
  gap: 24px;
  margin-bottom: 16px;
}

.auth-option {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  cursor: pointer;
}

.auth-expire {
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.expire-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 12px;
  font-size: 14px;
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