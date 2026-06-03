import request from './request'

// 获取待接单任务
export const getPendingTasks = () => {
  return request.get('/task/pending')
}

// 获取用户发布的任务
export const getMyPublishedTasks = (userId) => {
  return request.get('/task/my-published', { params: { userId } })
}

// 获取护工接单的任务
export const getMyAcceptedTasks = (caregiverId) => {
  return request.get('/task/my-accepted', { params: { caregiverId } })
}

// 发布任务
export const publishTask = (data) => {
  return request.post('/task/publish', data)
}

// 接单
export const acceptTask = (taskId, caregiverId) => {
  return request.post('/task/accept', { taskId, caregiverId })
}

// 完成任务
export const completeTask = (taskId) => {
  return request.post('/task/complete', { taskId })
}

// 取消任务
export const cancelTask = (taskId, userId) => {
  return request.post('/task/cancel', { taskId, userId })
}