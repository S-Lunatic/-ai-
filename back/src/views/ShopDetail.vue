<template>
  <div class="shop-detail">
    <button class="back-btn" @click="router.back()">← 返回</button>
    
    <div class="shop-card" v-if="shop">
      <!-- 店铺头部 -->
      <div class="shop-header">
        <div class="shop-name">{{ shop.name }}</div>
        <div class="shop-rating">
          <span class="stars">⭐ {{ shop.rating }}</span>
          <span class="distance">· 距离{{ shop.distance }}</span>
        </div>
      </div>
      
      <!-- 服务项目 -->
      <div class="info-section">
        <h4>📋 服务项目</h4>
        <div class="services-list">
          <span class="service-tag" v-for="service in serviceList" :key="service">
            {{ service }}
          </span>
        </div>
      </div>
      
      <!-- 营业时间 -->
      <div class="info-section">
        <h4>🕐 营业时间</h4>
        <p>{{ shop.hours }}</p>
      </div>
      
      <!-- 店铺地址 -->
      <div class="info-section">
        <h4>📍 店铺地址</h4>
        <p>{{ shop.address || '暂未录入' }}</p>
        <button class="map-btn" v-if="shop.address" @click="openMap">🗺️ 地图导航</button>
      </div>
      
      <!-- 联系电话 -->
      <div class="info-section">
        <h4>📞 联系电话</h4>
        <p>{{ shop.phone || '暂未录入' }}</p>
        <button class="call-btn" v-if="shop.phone" @click="makeCall">📱 拨打</button>
      </div>
      
      <!-- 预约按钮 -->
      <button class="appoint-btn" @click="goToAppoint">立即预约</button>
    </div>
    
    <!-- 加载失败 -->
    <div v-else class="empty-state">
      <div class="empty-icon">🏪</div>
      <p>店铺信息加载失败</p>
      <button class="back-home-btn" @click="router.push('/service-hall')">返回服务大厅</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const shop = ref(null)

// 解析服务项目为数组
const serviceList = computed(() => {
  if (!shop.value?.services) return []
  return shop.value.services.split('/')
})

onMounted(() => {
  const shopData = sessionStorage.getItem('currentShop')
  if (shopData) {
    shop.value = JSON.parse(shopData)
  }
})

// 返回预约页面
const goToAppoint = () => {
  router.back()
}

// 打开地图（模拟）
const openMap = () => {
  alert(`打开地图导航到：${shop.value.address}\n（功能开发中）`)
}

// 拨打电话（模拟）
const makeCall = () => {
  alert(`拨打电话：${shop.value.phone}\n（功能开发中）`)
}
</script>

<style scoped>
.shop-detail {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.back-btn {
  background: none;
  border: none;
  font-size: 16px;
  color: #2ecc71;
  cursor: pointer;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.back-btn:hover {
  color: #27ae60;
}

.shop-card {
  background: white;
  border-radius: 24px;
  padding: 28px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.shop-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.shop-name {
  font-size: 26px;
  font-weight: 700;
  color: #1a3c34;
  margin-bottom: 8px;
}

.shop-rating {
  font-size: 14px;
  color: #ff9800;
}

.distance {
  color: #999;
}

.info-section {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.info-section:last-of-type {
  border-bottom: none;
}

.info-section h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1a3c34;
  margin-bottom: 12px;
}

.info-section p {
  font-size: 14px;
  color: #555;
  line-height: 1.5;
}

.services-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.service-tag {
  background: #e8f5e9;
  color: #2ecc71;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 13px;
}

.map-btn, .call-btn {
  margin-top: 10px;
  background: #f0f0f0;
  border: none;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.map-btn:hover, .call-btn:hover {
  background: #e0e0e0;
}

.appoint-btn {
  width: 100%;
  padding: 14px;
  background: #2ecc71;
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 16px;
  transition: all 0.2s;
}

.appoint-btn:hover {
  background: #27ae60;
  transform: translateY(-2px);
}

.empty-state {
  text-align: center;
  padding: 60px;
  background: white;
  border-radius: 24px;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-state p {
  color: #999;
  margin-bottom: 20px;
}

.back-home-btn {
  background: #2ecc71;
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 30px;
  cursor: pointer;
}
</style>