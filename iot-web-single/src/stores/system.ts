import {defineStore} from 'pinia'
import {ref} from 'vue'

/**
 * 系统
 */
export const useSystemStore = defineStore('system', () => {

  /**
   * 重新调整尺寸
   */
  const resize = ref(0)

  return {resize}

})
