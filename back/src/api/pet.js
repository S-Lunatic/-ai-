import request from './request'

// 获取宠物列表
export const getPetList = (userId) => {
  return request.get('/pet/list', { params: { userId } })
}
// 添加别名，兼容两种写法
export const getPets = getPetList

// 添加宠物
export const addPet = (data) => {
  return request.post('/pet/add', data)
}

// 更新宠物
export const updatePet = (data) => {
  return request.post('/pet/update', data)
}

// 删除宠物
export const deletePet = (id) => {
  return request.get('/pet/delete', { params: { id } })
}

// ========== 宠物授权 ==========
export const getPetShares = (petId) => {
  return request.get('/pet-share/list', { params: { petId } })
}

export const addPetShare = (data) => {
  return request.post('/pet-share/add', data)
}

export const revokePetShare = (id) => {
  return request.get('/pet-share/revoke', { params: { id } })
}