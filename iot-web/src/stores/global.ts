import {defineStore} from 'pinia'
import {ref} from 'vue'

/**
 * 全局
 */
export const useGlobalStore = defineStore('global', () => {

  /**
   * 10分钟刷新一次
   */
  const refresh10Minute = ref(0)
  setInterval(() => {
    refresh10Minute.value = Math.random()
  }, 10 * 60 * 1000)

  return {refresh10Minute}

})
