import request from '@/utils/request'
import axios from 'axios'

const api = {
  saveParam: '/analysis/param/save',
  removeParam: '/analysis/param/remove',
  listParam: '/analysis/param/list',
  startTask: '/analysis/startTask',
  listAnalysisResult: '/analysis/listAnalysisResult'
}

export default api

export function saveParam (parameter) {
  return request({
    url: api.saveParam,
    method: 'post',
    params: parameter
  })
}

export function removeParam (parameter) {
  return request({
    url: api.removeParam,
    method: 'delete',
    params: parameter
  })
}

export function listParam (parameter) {
  return request({
    url: api.listParam,
    method: 'get',
    params: parameter
  })
}

export function startTask (data) {
  return axios.post('/api' + api.startTask, data)
}

export function listAnalysisResult (parameter) {
  return request({
    url: api.listAnalysisResult,
    method: 'get',
    params: parameter
  })
}
