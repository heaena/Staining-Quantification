/**
 * 向后端请求用户的菜单，动态生成路由
 */
import { constantRouterMap, asyncRouterMap } from '@/config/router.config'

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRoutes ({ commit }, data) {
      return new Promise(resolve => {
        commit('SET_ROUTERS', asyncRouterMap)
      })
    }
  }
}

export default permission
