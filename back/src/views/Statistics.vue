<template>
  <div class="statistics">
    <h1 class="page-title">数据统计</h1>
    
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">📅</div>
        <div class="stat-value">28</div>
        <div class="stat-label">本月订单量</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">💰</div>
        <div class="stat-value">¥3,280</div>
        <div class="stat-label">本月营业额</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">⭐</div>
        <div class="stat-value">4.9</div>
        <div class="stat-label">综合评分</div>
      </div>
    </div>
    
    <!-- 近7天订单趋势 -->
    <div class="chart-card">
      <h3>📊 近7天订单趋势</h3>
      <div class="chart-bars">
        <div class="bar-item" v-for="day in weekData" :key="day.name">
          <div class="bar" :style="{ height: day.height }"></div>
          <div class="bar-label">{{ day.name }}</div>
          <div class="bar-value">{{ day.count }}</div>
        </div>
      </div>
    </div>
    
    <!-- 服务分类统计 -->
    <div class="info-card">
      <h3>📋 服务分类统计</h3>
      <div class="category-list">
        <div class="category-item">
          <span>疫苗服务</span>
          <div class="progress-bar"><div class="progress" style="width: 35%"></div></div>
          <span>35%</span>
        </div>
        <div class="category-item">
          <span>体检服务</span>
          <div class="progress-bar"><div class="progress" style="width: 28%"></div></div>
          <span>28%</span>
        </div>
        <div class="category-item">
          <span>美容服务</span>
          <div class="progress-bar"><div class="progress" style="width: 22%"></div></div>
          <span>22%</span>
        </div>
        <div class="category-item">
          <span>寄养服务</span>
          <div class="progress-bar"><div class="progress" style="width: 15%"></div></div>
          <span>15%</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const weekData = [
  { name: '5/5', count: 3, height: '30px' },
  { name: '5/6', count: 5, height: '50px' },
  { name: '5/7', count: 4, height: '40px' },
  { name: '5/8', count: 7, height: '70px' },
  { name: '5/9', count: 6, height: '60px' },
  { name: '5/10', count: 8, height: '80px' },
  { name: '5/11', count: 4, height: '40px' }
]
</script>

<style scoped>
.statistics { max-width: 900px; }
.page-title { font-size: 24px; margin-bottom: 24px; color: #1a3c34; }
.stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-bottom: 24px; }
.stat-card { background: white; border-radius: 20px; padding: 24px; text-align: center; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.stat-icon { font-size: 36px; margin-bottom: 12px; }
.stat-value { font-size: 32px; font-weight: 600; color: #1a3c34; }
.stat-label { font-size: 14px; color: #666; margin-top: 8px; }
.chart-card, .info-card { background: white; border-radius: 16px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.chart-card h3, .info-card h3 { font-size: 18px; color: #1a3c34; margin-bottom: 20px; }
.chart-bars { display: flex; justify-content: space-around; align-items: flex-end; height: 200px; }
.bar-item { text-align: center; width: 40px; }
.bar { background: #2ecc71; width: 30px; border-radius: 8px 8px 0 0; margin: 0 auto; transition: height 0.3s; }
.bar-label { font-size: 12px; margin-top: 8px; color: #666; }
.bar-value { font-size: 12px; font-weight: 600; margin-top: 4px; }
.category-item { display: flex; align-items: center; gap: 16px; margin-bottom: 16px; }
.category-item span:first-child { width: 80px; font-size: 14px; }
.progress-bar { flex: 1; height: 8px; background: #eee; border-radius: 4px; overflow: hidden; }
.progress { height: 100%; background: #2ecc71; border-radius: 4px; }
.category-item span:last-child { width: 40px; font-size: 13px; color: #666; }
</style>