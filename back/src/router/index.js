import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import Home from '../views/Home.vue'
import PetProfile from '../views/PetProfile.vue'
import HealthManage from '../views/HealthManage.vue'
import DailyRecord from '../views/DailyRecord.vue'
import ServiceHall from '../views/ServiceHall.vue'
import UserCenter from '../views/UserCenter.vue'
import MyOrders from '../views/MyOrders.vue'
import Tutorial from '../views/Tutorial.vue'
import AIRecognize from '../views/AIRecognize.vue'
import QA from '../views/QA.vue'
import MedicalRecords from '../views/MedicalRecords.vue'
import ShopDetail from '../views/ShopDetail.vue'
import MyPublishedTasks from '../views/MyPublishedTasks.vue'
import MyVideos from '../views/MyVideos.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
// 护工页面
import MyWork from '../views/MyWork.vue'
import Earnings from '../views/Earnings.vue'

// 商家页面
import ShopManage from '../views/ShopManage.vue'
import Orders from '../views/Orders.vue'
import Statistics from '../views/Statistics.vue'

// 管理员页面
import VideoAudit from '../views/VideoAudit.vue'
import QualificationAudit from '../views/QualificationAudit.vue'
import Dispute from '../views/Dispute.vue'
import UserFeedback from '../views/UserFeedback.vue'

const routes = [
  // 公开路由（不需要登录）
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  
  // 需要登录的路由（添加 meta: { requiresAuth: true }）
  { path: '/', component: Home, meta: { requiresAuth: true } },
  { path: '/service-hall', component: ServiceHall, meta: { requiresAuth: true } },
  { path: '/user-center', component: UserCenter, meta: { requiresAuth: true } },
  { path: '/tutorial', component: Tutorial, meta: { requiresAuth: true } },
  { path: '/ai-recognize', component: AIRecognize, meta: { requiresAuth: true } },
  { path: '/my-videos', component: MyVideos, meta: { requiresAuth: true } },
  { path: '/qa', component: QA, meta: { requiresAuth: true } },
  
  // 宠物主人专属
  { path: '/pet-profile', component: PetProfile, meta: { requiresAuth: true } },
  { path: '/health-manage', component: HealthManage, meta: { requiresAuth: true } },
  { path: '/daily-record', component: DailyRecord, meta: { requiresAuth: true } },
  { path: '/my-orders', component: MyOrders, meta: { requiresAuth: true } },
  { path: '/medical-records', component: MedicalRecords, meta: { requiresAuth: true } },
  { path: '/shop-detail', component: ShopDetail, meta: { requiresAuth: true } },
  { path: '/my-published-tasks', component: MyPublishedTasks, meta: { requiresAuth: true } },
  
  // 护工专属
  { path: '/my-work', component: MyWork, meta: { requiresAuth: true } },
  { path: '/earnings', component: Earnings, meta: { requiresAuth: true } },
  
  // 商家专属
  { path: '/shop-manage', component: ShopManage, meta: { requiresAuth: true } },
  { path: '/orders', component: Orders, meta: { requiresAuth: true } },
  { path: '/statistics', component: Statistics, meta: { requiresAuth: true } },
  
  // 管理员专属
  { path: '/video-audit', component: VideoAudit, meta: { requiresAuth: true } },
  { path: '/qualification-audit', component: QualificationAudit, meta: { requiresAuth: true } },
  { path: '/dispute', component: Dispute, meta: { requiresAuth: true } },
  { path: '/user-feedback', component: UserFeedback, meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：检查登录状态
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && userStore.isLoggedIn) {
    next('/')
  } else {
    next()
  }
})

export default router