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
        path: '/tasks',
        name: 'Analysis tasks',
        component: () => import('@/views/picture/AnalysisTasks'),
        meta: { title: 'Analysis tasks', keepAlive: false, icon: 'line-chart' }
      },
      {
        path: '/task/:taskName',
        name: 'Analysis tasks',
        component: () => import('@/views/picture/TaskResult'),
        meta: { title: 'Analysis tasks', keepAlive: false, icon: 'line-chart' },
        hidden: true
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
