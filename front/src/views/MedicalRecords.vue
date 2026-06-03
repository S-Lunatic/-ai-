<template>
  <div class="medical-records">
    <h1 class="page-title">📋 全部医疗记录</h1>
    
    <!-- 筛选栏 -->
    <div class="filter-bar">
      <select v-model="filterPet" class="filter-select">
        <option value="all">全部宠物</option>
        <option value="1">豆豆（金毛）</option>
        <option value="2">咪咪（英短）</option>
      </select>
      <input type="month" v-model="filterMonth" class="filter-date" placeholder="按月份筛选">
    </div>
    
    <!-- 医疗记录列表 -->
    <div class="records-list">
      <div class="record-card" v-for="record in filteredRecords" :key="record.id">
        <div class="record-header">
          <span class="record-pet">{{ record.petName }}</span>
          <span class="record-date">{{ record.date }}</span>
        </div>
        <div class="record-hospital">🏥 {{ record.hospital }}</div>
        <div class="record-diagnosis">📋 {{ record.diagnosis }}</div>
        <div class="record-desc" v-if="record.description">{{ record.description }}</div>
        <div class="record-doctor">👨‍⚕️ 医生：{{ record.doctor }}</div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-if="filteredRecords.length === 0" class="empty-state">
      <span>📭</span>
      <p>暂无医疗记录</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// 筛选条件
const filterPet = ref('all')
const filterMonth = ref('')

// 医疗记录数据（假数据，后续从后端获取）
const allRecords = ref([
  {
    id: 1,
    petId: 1,
    petName: '豆豆',
    date: '2026-03-01',
    hospital: '安心宠物医院',
    diagnosis: '疫苗第三针',
    description: '接种狂犬疫苗和八联疫苗，无不良反应',
    doctor: '李医生'
  },
  {
    id: 2,
    petId: 1,
    petName: '豆豆',
    date: '2026-02-10',
    hospital: '安心宠物医院',
    diagnosis: '年度体检',
    description: '体重28kg，心肺正常，血液检查正常',
    doctor: '李医生'
  },
  {
    id: 3,
    petId: 1,
    petName: '豆豆',
    date: '2025-12-20',
    hospital: '安心宠物医院',
    diagnosis: '皮肤病就诊',
    description: '背部有红疹，诊断为过敏性皮炎，开具药膏',
    doctor: '王医生'
  },
  {
    id: 4,
    petId: 2,
    petName: '咪咪',
    date: '2026-02-15',
    hospital: '瑞康动物医院',
    diagnosis: '驱虫',
    description: '体内外驱虫，食欲良好',
    doctor: '张医生'
  },
  {
    id: 5,
    petId: 2,
    petName: '咪咪',
    date: '2025-11-10',
    hospital: '瑞康动物医院',
    diagnosis: '绝育手术',
    description: '已顺利完成，恢复良好',
    doctor: '张医生'
  }
])

// 筛选后的记录
const filteredRecords = computed(() => {
  let records = allRecords.value
  
  // 按宠物筛选
  if (filterPet.value !== 'all') {
    records = records.filter(r => r.petId === parseInt(filterPet.value))
  }
  
  // 按月份筛选
  if (filterMonth.value) {
    records = records.filter(r => r.date.startsWith(filterMonth.value))
  }
  
  return records
})
</script>

<style scoped>
.medical-records {
  max-width: 800px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 24px;
  color: #1a3c34;
}

.filter-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.filter-select, .filter-date {
  padding: 10px 16px;
  border: 1px solid #ddd;
  border-radius: 24px;
  font-size: 14px;
  background: white;
}

.records-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.record-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s;
}

.record-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.record-pet {
  font-size: 16px;
  font-weight: 600;
  color: #1a3c34;
}

.record-date {
  font-size: 13px;
  color: #999;
}

.record-hospital, .record-diagnosis, .record-desc, .record-doctor {
  font-size: 14px;
  color: #555;
  margin-bottom: 8px;
}

.record-desc {
  padding-left: 20px;
  color: #666;
  font-size: 13px;
}

.empty-state {
  text-align: center;
  padding: 80px;
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
}
</style>