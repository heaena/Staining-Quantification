import request from '@/utils/request'

const api = {
  saveParam: '/analysis/param/save',
  removeParam: '/analysis/param/remove',
  listParam: '/analysis/param/list',
  startTask: '/sourceImage/createTask',
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
  return request({
    url: api.startTask,
    method: 'post',
    data: data
  })
}

export function listAnalysisResult (parameter) {
  return request({
    url: api.listAnalysisResult,
    method: 'get',
    params: parameter
  })
}
