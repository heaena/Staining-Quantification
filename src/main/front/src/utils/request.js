import axios from 'axios'
import store from '@/store'
import storage from 'store'
import { VueAxios } from './axios'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { config } from '../config/config'
import { message } from 'ant-design-vue'

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: config().API_BASE_URL,
  timeout: 6000, // 请求超时时间
  withCredentials: true
})

// 异常拦截处理器
const errorHandler = (error) => {
  if (error.response) {
    // 从 localstorage 获取 token
    if (error.response.status === 403) {
      message.error(error.msg)
    }
    if (error.response.status === 401) {
      message.error(error.msg)
      store.dispatch('Logout').then(() => {
        window.location.reload()
      })
    }
  }
  return Promise.reject(error.response.data)
}

// request interceptor
request.interceptors.request.use(config => {
  const token = storage.get(ACCESS_TOKEN)
  // 如果 token 存在
  // 让每个请求携带自定义 token 请根据实际情况自行修改
  if (token) {
    config.headers['Authorization'] = token
  }
  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use((response) => {
  if (!response.data.success) {
    message.error(response.data.msg)
    return Promise.reject(response.data)
  }
  return response.data
}, errorHandler)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  }
}

export default request

export {
  installer as VueAxios,
  request as axios
}
