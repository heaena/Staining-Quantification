import request from '@/utils/request'

export function listQps (parameter) {
  return request({
    url: '/metric/queryTopResourceMetric.json',
    method: 'get',
    params: parameter
  })
}

export function listApps () {
  return request({
    url: '/app/names.json',
    method: 'get'
  })
}

export function getIdentity (parameter) {
  return request({
    url: '/resource/machineResource.json',
    method: 'get',
    params: parameter
  })
}

export function getMachineList (app) {
  return request({
    url: '/app/' + app + '/machines.json',
    method: 'get'
  })
}

export function saveRule (param) {
  if (param.id) {
    return request({
      url: '/v2/flow/rule/' + param.id,
      method: 'put',
      data: param
    })
  } else {
    return request({
      url: '/v2/flow/rule',
      method: 'post',
      data: param
    })
  }
}

export function listRule (param) {
  return request({
    url: '/v2/flow/rules',
    method: 'get',
    params: param
  })
}

export function delRule (param) {
  return request({
    url: '/v2/flow/rule/' + param,
    method: 'delete'
  })
}

export function getUserList (param) {
  return request({
    url: '/usergroup/list',
    method: 'post',
    data: param
  })
}

export function delUser (param) {
  return request({
    url: '/usergroup/' + param,
    method: 'delete'
  })
}

export function saveUser (param) {
  return request({
    url: '/usergroup/saveUser',
    method: 'post',
    data: param
  })
}
