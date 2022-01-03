import router from './router'
import NProgress from 'nprogress' // progress bar
import '@/components/NProgress/nprogress.less' // progress bar custom style
import { setDocumentTitle, domTitle } from '@/utils/domUtil'
import { i18nRender } from '@/locales'
import store from './store'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import storage from 'store'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const allowList = ['Login', 'register', 'registerResult'] // no redirect allowList
const loginRoutePath = '/admin/user/login'
const defaultRoutePath = '/admin'

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  to.meta && (typeof to.meta.title !== 'undefined' && setDocumentTitle(`${i18nRender(to.meta.title)} - ${domTitle}`))
  /* 加载页面组件 */
  if (store.state.permission.addRouters.length < 1) {
    store.dispatch('GenerateRoutes', {})
  }
  // 判断是否有登录态
  if (store.state.user.info) {
    if (to.path === loginRoutePath) {
      next({ path: defaultRoutePath })
      NProgress.done()
    } else {
      next()
      // check login user.roles is null
    }
  } else {
    if (allowList.includes(to.name)) {
      // 在免登录名单，直接进入
      next()
    } else if (storage.get(ACCESS_TOKEN)) {
      // check login
      store.dispatch('GetInfo').then(res => {
        next()
      }).catch(() => {
        console.log(from)
        next({ path: loginRoutePath, query: { redirect: to.fullPath } })
      })
    } else {
      next({ path: loginRoutePath, query: { redirect: to.fullPath } })
    }
    NProgress.done()
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
