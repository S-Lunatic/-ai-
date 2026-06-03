<template>
  <div class="tutorial">
    <h1 class="page-title">📺 视频教程</h1>
    
    <!-- 搜索框 -->
    <div class="search-bar">
      <input 
        type="text" 
        v-model="searchKeyword" 
        placeholder="🔍 搜索视频标题、描述、作者..." 
        class="search-input"
      />
    </div>
    
    <!-- 分类标签 -->
    <div class="tabs">
      <div class="tab" :class="{ active: activeCategory === 'all' }" @click="activeCategory = 'all'">
        全部 ({{ allVideos.length }})
      </div>
      <div class="tab" :class="{ active: activeCategory === 'training' }" @click="activeCategory = 'training'">
        🐕 训练技巧 ({{ trainingVideos.length }})
      </div>
      <div class="tab" :class="{ active: activeCategory === 'care' }" @click="activeCategory = 'care'">
        🏥 医疗养护 ({{ careVideos.length }})
      </div>
      <div class="tab" :class="{ active: activeCategory === 'feed' }" @click="activeCategory = 'feed'">
        🍖 喂养知识 ({{ feedVideos.length }})
      </div>
    </div>
    
    <!-- 视频列表 -->
    <div class="video-list">
      <div class="video-card" v-for="video in filteredVideos" :key="video.id" @click="playVideo(video)">
        <div class="video-thumbnail">
          <span class="play-icon">▶️</span>
          <span class="video-cover">{{ video.cover }}</span>
        </div>
        <div class="video-info">
          <div class="video-title">{{ video.title }}</div>
          <div class="video-meta">
            <span>👤 {{ video.author }}</span>
            <span>👁️ {{ video.views }}次播放</span>
            <span>⭐ {{ video.likes }}点赞</span>
            <span v-if="video.source === 'user'" class="user-tag">📹 用户投稿</span>
          </div>
          <div class="video-desc">{{ video.desc }}</div>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-if="filteredVideos.length === 0" class="empty-state">
      <span>📹</span>
      <p>暂无视频，敬请期待</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getApprovedVideos } from '../api/video'

const activeCategory = ref('all')
const searchKeyword = ref('')

// 官方视频（假数据）
const officialVideos = ref([
  {
    id: 1,
    title: '如何训练狗狗坐下',
    author: '专业训犬师',
    views: 1234,
    likes: 89,
    desc: '详细讲解训练狗狗坐下的步骤和技巧',
    cover: '🐕',
    category: 'training',
    source: 'official'
  },
  {
    id: 2,
    title: '猫咪洗澡注意事项',
    author: '资深猫奴',
    views: 2345,
    likes: 156,
    desc: '猫咪洗澡的正确步骤和安全注意事项',
    cover: '🐈',
    category: 'care',
    source: 'official'
  },
  {
    id: 3,
    title: '宠物急救知识大全',
    author: '宠物医生',
    views: 3456,
    likes: 267,
    desc: '突发情况的急救处理方法，关键时刻能救命',
    cover: '🏥',
    category: 'care',
    source: 'official'
  },
  {
    id: 4,
    title: '金毛喂养指南',
    author: '资深犬主',
    views: 987,
    likes: 67,
    desc: '金毛犬的饮食搭配和营养需求',
    cover: '🍖',
    category: 'feed',
    source: 'official'
  },
  {
    id: 5,
    title: '狗狗拒食训练',
    author: '专业训犬师',
    views: 765,
    likes: 54,
    desc: '防止狗狗乱吃路边食物的训练方法',
    cover: '🐕',
    category: 'training',
    source: 'official'
  },
  {
    id: 6,
    title: '猫咪驱虫完全指南',
    author: '宠物医生',
    views: 543,
    likes: 32,
    desc: '体内外驱虫的时间和方法',
    cover: '💊',
    category: 'care',
    source: 'official'
  }
])

// 用户发布的已通过视频
const userVideos = ref([])

// 加载用户发布的已通过视频（从后端获取）
const loadUserVideos = async () => {
  try {
    const res = await getApprovedVideos()
    if (res.code === 200) {
      userVideos.value = res.data.map(v => ({
        id: v.id,
        title: v.title,
        author: `用户${v.userId}`,
        views: v.views || 0,
        likes: v.likes || 0,
        desc: v.description || '',
        cover: '🎬',
        category: v.category || 'care',
        source: 'user',
        videoUrl: v.videoUrl,
        status: v.status
      }))
    }
  } catch (error) {
    console.error('加载用户视频失败:', error)
  }
}

// 所有视频（官方 + 用户）
const allVideos = computed(() => {
  return [...officialVideos.value, ...userVideos.value]
})

// 按分类过滤（不包含搜索）
const videosByCategory = computed(() => {
  if (activeCategory.value === 'all') return allVideos.value
  return allVideos.value.filter(v => v.category === activeCategory.value)
})

// 按分类 + 搜索过滤
const filteredVideos = computed(() => {
  let result = videosByCategory.value
  
  // 按关键词搜索
  if (searchKeyword.value.trim()) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(v => 
      v.title.toLowerCase().includes(keyword) || 
      v.desc.toLowerCase().includes(keyword) ||
      v.author.toLowerCase().includes(keyword)
    )
  }
  
  return result
})

// 各分类数量（用于显示）
const trainingVideos = computed(() => allVideos.value.filter(v => v.category === 'training'))
const careVideos = computed(() => allVideos.value.filter(v => v.category === 'care'))
const feedVideos = computed(() => allVideos.value.filter(v => v.category === 'feed'))

// 播放视频 - 直接跳转到视频URL
const playVideo = (video) => {
  if (video.videoUrl) {
    window.open(video.videoUrl, '_blank')
  } else {
    alert('该视频暂无播放地址')
  }
}

onMounted(() => {
  loadUserVideos()
})
</script>

<style scoped>
.tutorial {
  max-width: 1000px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 24px;
  color: #1a3c34;
}

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 30px;
  font-size: 14px;
  outline: none;
  transition: all 0.2s;
}

.search-input:focus {
  border-color: #2ecc71;
  box-shadow: 0 0 0 2px rgba(46, 204, 113, 0.1);
}

.tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: white;
  padding: 6px;
  border-radius: 48px;
  width: fit-content;
  flex-wrap: wrap;
}

.tab {
  padding: 8px 24px;
  border-radius: 40px;
  cursor: pointer;
  font-size: 14px;
}

.tab.active {
  background: #2ecc71;
  color: white;
}

.video-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.video-card {
  background: white;
  border-radius: 16px;
  padding: 16px;
  display: flex;
  gap: 16px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s;
}

.video-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.video-thumbnail {
  position: relative;
  width: 160px;
  height: 100px;
  background: #f0f0f0;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.video-cover {
  font-size: 48px;
}

.play-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 32px;
  opacity: 0;
  transition: opacity 0.2s;
}

.video-card:hover .play-icon {
  opacity: 0.8;
}

.video-info {
  flex: 1;
}

.video-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.video-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
  flex-wrap: wrap;
  align-items: center;
}

.user-tag {
  background: #e8f5e9;
  color: #2ecc71;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 11px;
}

.video-desc {
  font-size: 13px;
  color: #666;
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
</style>