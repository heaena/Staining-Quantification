import router from './router'
import NProgress from 'nprogress' // progress bar
import '@/components/NProgress/nprogress.less' // progress bar custom style
import { setDocumentTitle, domTitle } from '@/utils/domUtil'
import { i18nRender } from '@/locales'
import store from './store'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  to.meta && (typeof to.meta.title !== 'undefined' && setDocumentTitle(`${i18nRender(to.meta.title)} - ${domTitle}`))
  /* 加载页面组件 */
  if (store.state.permission.addRouters.length < 1) {
    store.dispatch('GenerateRoutes', {})
  }
  next()
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
