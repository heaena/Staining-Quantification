import request from '@/utils/request'

const api = {
  remove: '/task/remove',
  taskList: '/task/listTask'
}

export default api

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
