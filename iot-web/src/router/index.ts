import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/home/Home.vue')
    },
    {
      path: '/config',
      name: 'Config',
      component: () => import('@/views/config/Config.vue')
    },
    {
      path: '/debug',
      name: 'Debug',
      component: () => import('@/views/debug/Debug.vue')
    },
    {
      path: '/device/:gatewaySn/:deviceSn',
      name: 'Device',
      component: () => import('@/views/device/Device.vue')
    },
    {
      path: '/:path(.*)*',
      name: 'NotFound',
      component: () => import('@/views/NotFound.vue')
    }
  ]
})

export default router
