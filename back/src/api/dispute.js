import request from './request'

// 发起纠纷
export const createDispute = (data) => {
  return request.post('/dispute/create', data)
}

// 获取用户发起的纠纷
export const getMyInitiatedDisputes = (userId) => {
  return request.get('/dispute/my-initiated', { params: { userId } })
}

// 获取待处理纠纷（管理员）
export const getPendingDisputes = () => {
  return request.get('/dispute/pending')
}

// 获取已完结纠纷（管理员）
export const getResolvedDisputes = () => {
  return request.get('/dispute/resolved')
}

// 处理纠纷（管理员）
export const resolveDispute = (id, status, result) => {
  return request.post('/dispute/resolve', { id, status, result })
}