import request from './request'

// ========== 商品管理 ==========
export const getProducts = (shopId) => {
  return request.get('/shop/product/list', { params: { shopId } })
}

export const addProduct = (data) => {
  return request.post('/shop/product/add', data)
}

export const updateProduct = (data) => {
  return request.post('/shop/product/update', data)
}

export const deleteProduct = (id) => {
  return request.get('/shop/product/delete', { params: { id } })
}

// ========== 员工管理 ==========
export const getEmployees = (shopId) => {
  return request.get('/shop/employee/list', { params: { shopId } })
}

export const addEmployee = (data) => {
  return request.post('/shop/employee/add', data)
}

export const deleteEmployee = (id) => {
  return request.get('/shop/employee/delete', { params: { id } })
}