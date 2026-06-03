<template>
  <div class="earnings">
    <h1 class="page-title">收益提现</h1>
    
    <!-- 收益总览 -->
    <div class="overview-card">
      <div class="balance">
        <div class="balance-label">可提现余额</div>
        <div class="balance-amount">¥ 1,280.00</div>
      </div>
      <div class="balance-actions">
        <button class="btn-withdraw">提现到微信</button>
        <button class="btn-withdraw">提现到支付宝</button>
      </div>
    </div>
    
    <!-- 收益明细 -->
    <div class="section">
      <div class="section-header">
        <h3>收益明细</h3>
      </div>
      <div class="record-list">
        <div class="record-item">
          <div class="record-info">
            <div class="record-title">遛狗任务 · 阳光小区</div>
            <div class="record-time">2026-05-10 19:00</div>
          </div>
          <div class="record-amount">+¥50.00</div>
        </div>
        <div class="record-item">
          <div class="record-info">
            <div class="record-title">上门喂养 · 地铁站附近</div>
            <div class="record-time">2026-05-08 14:00</div>
          </div>
          <div class="record-amount">+¥80.00</div>
        </div>
        <div class="record-item">
          <div class="record-info">
            <div class="record-title">遛狗陪玩 · 中央公园</div>
            <div class="record-time">2026-05-05 20:00</div>
          </div>
          <div class="record-amount">+¥40.00</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.earnings { max-width: 800px; }
.page-title { font-size: 24px; margin-bottom: 24px; color: #1a3c34; }
.overview-card { background: linear-gradient(135deg, #1a3c34, #0f2a24); border-radius: 20px; padding: 28px; margin-bottom: 24px; color: white; }
.balance-label { font-size: 14px; opacity: 0.8; margin-bottom: 8px; }
.balance-amount { font-size: 42px; font-weight: 600; margin-bottom: 20px; }
.balance-actions { display: flex; gap: 16px; }
.btn-withdraw { background: rgba(255,255,255,0.2); border: none; padding: 10px 24px; border-radius: 30px; color: white; cursor: pointer; }
.section { background: white; border-radius: 16px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.section-header { margin-bottom: 16px; }
.section-header h3 { font-size: 18px; color: #1a3c34; }
.record-item { display: flex; justify-content: space-between; align-items: center; padding: 14px 0; border-bottom: 1px solid #eee; }
.record-title { font-size: 15px; font-weight: 500; margin-bottom: 4px; }
.record-time { font-size: 12px; color: #999; }
.record-amount { font-size: 16px; font-weight: 600; color: #2ecc71; }
</style>