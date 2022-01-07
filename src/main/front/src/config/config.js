const envList = {
  local: {
    API_BASE_URL: '/api'
  },
  development: {
    API_BASE_URL: 'http://dev-sentinel.codemao.cn'
  },
  test: {
    API_BASE_URL: 'http://test-sentinel.codemao.cn'
  },
  staging: {
    API_BASE_URL: 'http://staging-sentinel.codemao.cn'
  },
  production: {
    API_BASE_URL: 'http://sentinel.codemao.cn'
  }
}
export const config = () => {
  if (!window.__env__) {
    window.__env__ = 'local'
  }
  return envList[window.__env__]
}
