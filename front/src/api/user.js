import request from './request'

// 注册
export const register = (data) => {
  return request.post('/user/register', data)
}

// 登录
export const login = (data) => {
  return request.post('/user/login', data)
}