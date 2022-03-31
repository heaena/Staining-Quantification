// eslint-disable-next-line
import { UserLayout, BasicLayout } from '@/layouts'

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    redirect: '/source',
    children: [
      {
        path: '/source',
        name: 'Source image',
        component: () => import('@/views/picture/SourceImage'),
        meta: { title: 'Source image', keepAlive: false, icon: 'folder' }
      },
      {
        path: '/task/:folderName',
        name: 'Analysis task',
        component: () => import('@/views/picture/ImageTableList'),
        meta: { title: 'Analysis task', keepAlive: false, icon: 'line-chart' }
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
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }

]
