<template>
  <div class="qa">
    <h1 class="page-title">💬 智能问答助手</h1>
    
    <!-- 聊天记录区 -->
    <div class="chat-area" ref="chatAreaRef">
      <div v-for="(msg, index) in messages" :key="index" class="message" :class="msg.role">
        <div class="message-avatar">{{ msg.role === 'user' ? '👤' : '🤖' }}</div>
        <div class="message-content">{{ msg.content }}</div>
      </div>
      
      <!-- 加载状态 -->
      <div v-if="loading" class="message assistant">
        <div class="message-avatar">🤖</div>
        <div class="message-content typing">正在思考中...</div>
      </div>
    </div>
    
    <!-- 常见问题 -->
    <div class="quick-questions">
      <span class="quick-label">常见问题：</span>
      <span class="quick-tag" @click="askQuick('狗狗疫苗多久打一次？')">狗狗疫苗多久打一次？</span>
      <span class="quick-tag" @click="askQuick('猫咪呕吐怎么办？')">猫咪呕吐怎么办？</span>
      <span class="quick-tag" @click="askQuick('狗狗可以吃巧克力吗？')">狗狗可以吃巧克力吗？</span>
    </div>
    
    <!-- 输入区 -->
    <div class="input-area">
      <input 
        type="text" 
        v-model="question" 
        @keyup.enter="askQuestion"
        placeholder="输入您的问题..." 
        class="question-input"
      >
      <button @click="askQuestion" :disabled="loading || !question.trim()" class="send-btn">
        发送
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { chat } from '../api/ai'

const question = ref('')
const loading = ref(false)
const messages = ref([
  { role: 'assistant', content: '您好！我是宠物万事通智能助手，可以为您解答养宠相关问题。请问有什么可以帮您？' }
])
const chatAreaRef = ref(null)

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (chatAreaRef.value) {
    chatAreaRef.value.scrollTop = chatAreaRef.value.scrollHeight
  }
}

// 快速提问
const askQuick = (q) => {
  question.value = q
  askQuestion()
}

// 提问
const askQuestion = async () => {
  if (!question.value.trim() || loading.value) return
  
  const userQuestion = question.value.trim()
  
  // 添加用户消息
  messages.value.push({ role: 'user', content: userQuestion })
  question.value = ''
  await scrollToBottom()
  
  loading.value = true
  
  try {
    const res = await chat(userQuestion)
    if (res.code === 200) {
      messages.value.push({ role: 'assistant', content: res.data })
    } else {
      messages.value.push({ role: 'assistant', content: '抱歉，AI服务暂时不可用，请稍后再试。' })
    }
  } catch (error) {
    console.error('问答失败:', error)
    messages.value.push({ role: 'assistant', content: '网络错误，请检查连接后重试。' })
  } finally {
    loading.value = false
    await scrollToBottom()
  }
}
</script>

<style scoped>
.qa {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  height: calc(100vh - 100px);
}

.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #1a3c34;
}

.chat-area {
  flex: 1;
  overflow-y: auto;
  background: white;
  border-radius: 20px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.message {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.message.user {
  justify-content: flex-end;
}

.message.user .message-content {
  background: #2ecc71;
  color: white;
  border-radius: 20px 20px 4px 20px;
}

.message.assistant .message-content {
  background: #f0f0f0;
  color: #333;
  border-radius: 20px 20px 20px 4px;
}

.message-avatar {
  width: 36px;
  height: 36px;
  background: #f0f0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.message-content {
  max-width: 70%;
  padding: 12px 16px;
  font-size: 14px;
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-word;
}

.typing {
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 0.6; }
  50% { opacity: 1; }
}

.quick-questions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  background: white;
  padding: 12px 16px;
  border-radius: 40px;
}

.quick-label {
  font-size: 13px;
  color: #666;
}

.quick-tag {
  background: #f0f0f0;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.quick-tag:hover {
  background: #2ecc71;
  color: white;
}

.input-area {
  display: flex;
  gap: 12px;
}

.question-input {
  flex: 1;
  padding: 14px 20px;
  border: 1px solid #ddd;
  border-radius: 40px;
  font-size: 15px;
  outline: none;
}

.question-input:focus {
  border-color: #2ecc71;
}

.send-btn {
  padding: 14px 28px;
  background: #2ecc71;
  color: white;
  border: none;
  border-radius: 40px;
  cursor: pointer;
  font-weight: 600;
}

.send-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>