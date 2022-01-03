import request from '@/utils/request'

const api = {
  folderList: '/fileSystem/list',
  addFolder: '/fileSystem/addFolder',
  removeFile: '/fileSystem/remove'
}

export default api

export function getList (parameter) {
  return request({
    url: api.folderList,
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
