import storage from 'store'
import { login, getInfo } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: null
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          if (response.success) {
            storage.set(ACCESS_TOKEN, response.data.token, 7 * 24 * 60 * 60 * 1000)
            commit('SET_INFO', {
              'nickName': response.data.name
            })
            resolve()
          } else {
            reject(new Error('login must be a non-null array !'))
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          if (response.success) {
            commit('SET_INFO', {
              'nickName': response.data.name
            })
          } else {
            reject(new Error('getInfo: roles must be a non-null array !'))
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        commit('SET_TOKEN', '')
        commit('SET_INFO', null)
        storage.remove(ACCESS_TOKEN)
        resolve()
      })
    }

  }
}

export default user
