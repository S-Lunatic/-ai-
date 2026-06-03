import request from './request'

// 提交反馈
export const submitFeedback = (data) => {
  return request.post('/feedback/submit', data)
}

// 获取所有反馈（管理员）
export const getAllFeedback = () => {
  return request.get('/feedback/all')
}

// 获取待处理反馈（管理员）
export const getPendingFeedback = () => {
  return request.get('/feedback/pending')
}

// 处理反馈（管理员）
export const resolveFeedback = (id, category, reply) => {
  return request.post('/feedback/resolve', { id, category, reply })
}