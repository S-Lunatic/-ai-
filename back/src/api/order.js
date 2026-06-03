import request from './request'

// 创建订单
export const createOrder = (data) => {
  return request.post('/order/create', data)
}

// 获取用户订单
export const getUserOrders = (userId) => {
  return request.get('/order/user', { params: { userId } })
}

// 获取商家订单
export const getShopOrders = (shopName) => {
  return request.get('/order/shop', { params: { shopName } })
}

// 更新订单状态
export const updateOrderStatus = (id, status) => {
  return request.post('/order/update-status', { id, status })
}