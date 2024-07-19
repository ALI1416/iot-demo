import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Main',
      component: () => import('@/views/main/Main.vue')
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
      path: '/:path(.*)*',
      name: 'NotFound',
      component: () => import('@/views/NotFound.vue')
    }
  ]
})

export default router
