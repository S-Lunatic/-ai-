import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useUserStore } from './stores/user'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// 初始化认证状态（登录状态、用户信息）
const userStore = useUserStore()
userStore.initAuth()              // 新增：初始化登录状态
userStore.initRole()              // 保留：初始化角色
userStore.loadCertificationStatus() // 保留：加载认证申请状态

app.mount('#app')