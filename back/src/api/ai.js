import request from './request'

// AI问答
export const chat = (question) => {
  return request.post('/ai/chat', { question })
}

// AI图片识别（传base64）
export const recognize = (base64Image) => {
  return request.post('/ai/recognize', { image: base64Image })
}