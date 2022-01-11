// eslint-disable-next-line
import { UserLayout, BasicLayout } from '@/layouts'

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    redirect: '/folder',
    children: [
      {
        path: '/folder',
        name: '文件夹',
        component: () => import('@/views/picture/FolderTableList'),
        meta: { title: '文件夹', keepAlive: false, icon: 'folder' }
      },
      {
        path: '/images/:folderName',
        name: '图片',
        component: () => import('@/views/picture/ImageTableList'),
        meta: { title: '图片', keepAlive: false, icon: 'line-chart' }
      }
    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/admin/user',
    component: UserLayout,
    hidden: true,
    children: [
      {
        path: '/admin/user/login',
        name: 'Login',
        meta: { title: '登录', keepAlive: false },
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }

]
