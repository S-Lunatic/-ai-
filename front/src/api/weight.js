import request from './request'

// 获取近30天体重记录（图表用）
export const getRecentWeight = (petId) => {
  return request.get('/weight/recent', { params: { petId } })
}

// 获取最新体重
export const getLatestWeight = (petId) => {
  return request.get('/weight/latest', { params: { petId } })
}

// 添加体重记录
export const addWeight = (data) => {
  return request.post('/weight/add', data)
}