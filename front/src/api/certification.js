import request from './request'

export const submitCertification = (data) => {
  return request.post('/certification/submit', data)
}

export const getPendingCertifications = () => {
  return request.get('/certification/pending')
}

export const approveCertification = (id) => {
  return request.post('/certification/approve', { id })
}

export const rejectCertification = (id, reason) => {
  return request.post('/certification/reject', { id, reason })
}