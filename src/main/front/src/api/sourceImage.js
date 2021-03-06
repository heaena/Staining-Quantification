import request from '@/utils/request'

const api = {
  sourceList: '/sourceImage/list',
  addFolder: '/sourceImage/addFolder',
  removeFile: '/sourceImage/remove',
  createTask: '/sourceImage/createTask'
}

export default api

export function createTask (data) {
  return request({
    url: api.createTask,
    method: 'post',
    data: data
  })
}

export function getList (parameter) {
  return request({
    url: api.sourceList,
    method: 'get',
    params: parameter
  })
}

export function addFolder (parameter) {
  return request({
    url: api.addFolder,
    method: 'post',
    params: parameter
  })
}

export function removeFile (parameter) {
  return request({
    url: api.removeFile,
    method: 'delete',
    params: parameter
  })
}
