import request from '@/utils/request'

const api = {
  remove: '/task/remove',
  taskList: '/task/listTask',
  taskResult: '/task/taskResult'
}

export default api

export function getTaskResult (parameter) {
  return request({
    url: api.taskResult,
    method: 'get',
    params: parameter
  })
}

export function getTaskList (parameter) {
  return request({
    url: api.taskList,
    method: 'get',
    params: parameter
  })
}

export function remove (parameter) {
  return request({
    url: api.remove,
    method: 'delete',
    params: parameter
  })
}
