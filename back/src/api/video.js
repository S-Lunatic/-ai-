import request from './request'

// 上传视频文件
export const uploadVideo = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/video/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 发布视频
export const publishVideo = (data) => {
  return request.post('/video/publish', data)
}

// 获取待审核视频（管理员）
export const getPendingVideos = () => {
  return request.get('/video/pending')
}

// 获取已通过视频（教程页面）
export const getApprovedVideos = () => {
  return request.get('/video/approved')
}

// 审核视频
export const auditVideo = (id, status, rejectReason) => {
  return request.post('/video/audit', { id, status, rejectReason })
}

// 获取用户自己的视频
export const getMyVideos = (userId) => {
  return request.get('/video/my-videos', { params: { userId } })
}