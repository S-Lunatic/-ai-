<template>
  <div class="ai-recognize">
    <h1 class="page-title">🤖 AI宠物识别</h1>
    
    <div class="upload-area" @click="triggerFileUpload">
      <div class="upload-icon">📷</div>
      <div class="upload-text">{{ uploadedImage ? '点击重新上传' : '点击拍照或上传图片' }}</div>
      <div class="upload-hint">支持：宠物便便、皮肤、口腔照片</div>
    </div>
    
    <input type="file" ref="fileInput" @change="handleFileSelect" accept="image/*" style="display:none">
    
    <!-- 图片预览 -->
    <div v-if="uploadedImage" class="preview-area">
      <img :src="uploadedImage" class="preview-img">
      <button class="btn-clear" @click="clearImage">清除</button>
    </div>
    
    <!-- 识别结果 -->
    <div v-if="result" class="result-card">
      <div class="result-header">
        <span>🔍 识别结果</span>
        <span class="ai-badge">AI分析</span>
      </div>
      <div class="result-content">
        <div class="result-text">{{ result }}</div>
      </div>
    </div>
    
    <!-- 识别按钮 -->
    <button class="btn-recognize" @click="recognize" :disabled="!uploadedImage || loading">
      {{ loading ? '识别中...' : '开始识别' }}
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { recognize as recognizeApi } from '../api/ai'

const fileInput = ref(null)
const uploadedImage = ref(null)
const loading = ref(false)
const result = ref(null)

const triggerFileUpload = () => {
  fileInput.value.click()
}

const handleFileSelect = (e) => {
  const file = e.target.files[0]
  if (file) {
    if (!file.type.startsWith('image/')) {
      alert('请选择图片文件')
      return
    }
    if (file.size > 10 * 1024 * 1024) {
      alert('图片不能超过10MB')
      return
    }
    const reader = new FileReader()
    reader.onload = (event) => {
      uploadedImage.value = event.target.result
      result.value = null
    }
    reader.readAsDataURL(file)
  }
}

const clearImage = () => {
  uploadedImage.value = null
  result.value = null
  fileInput.value.value = ''
}

const recognize = async () => {
  if (!uploadedImage.value) return
  
  loading.value = true
  
  try {
    const res = await recognizeApi(uploadedImage.value)
    if (res.code === 200) {
      result.value = res.data
    } else {
      alert('识别失败，请重试')
    }
  } catch (error) {
    console.error('识别失败:', error)
    alert('识别失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.ai-recognize {
  max-width: 600px;
  margin: 0 auto;
}

.page-title {
  font-size: 24px;
  margin-bottom: 24px;
  color: #1a3c34;
  text-align: center;
}

.upload-area {
  background: #f8f9fa;
  border: 2px dashed #ddd;
  border-radius: 20px;
  padding: 48px;
  text-align: center;
  cursor: pointer;
  margin-bottom: 24px;
  transition: all 0.2s;
}

.upload-area:hover {
  border-color: #2ecc71;
  background: #f0fdf4;
}

.upload-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.upload-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 8px;
}

.upload-hint {
  font-size: 12px;
  color: #999;
}

.preview-area {
  position: relative;
  margin-bottom: 24px;
  text-align: center;
}

.preview-img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.btn-clear {
  position: absolute;
  top: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 20px;
  cursor: pointer;
}

.result-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  font-weight: 600;
  font-size: 16px;
}

.ai-badge {
  background: #2ecc71;
  color: white;
  padding: 2px 10px;
  border-radius: 20px;
  font-size: 12px;
}

.result-text {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}

.btn-recognize {
  width: 100%;
  padding: 14px;
  background: #2ecc71;
  color: white;
  border: none;
  border-radius: 40px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-recognize:hover {
  background: #27ae60;
  transform: translateY(-2px);
}

.btn-recognize:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
}
</style>