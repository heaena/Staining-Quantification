import request from '@/utils/request'

const api = {
  sourceList: '/sourceImage/list',
  folderList: '/fileSystem/listFolder',
  imageList: '/fileSystem/listImage',
  addFolder: '/sourceImage/addFolder',
  removeFile: '/sourceImage/remove'
}

export default api

export function getList (parameter) {
  return request({
    url: api.sourceList,
    method: 'get',
    params: parameter
  })
}

export function getFolderList (parameter) {
  return request({
    url: api.folderList,
    method: 'get',
    params: parameter
  })
}

export function getImageList (parameter) {
  return request({
    url: api.imageList,
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
