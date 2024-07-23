<script lang="ts" setup>
import {ref} from 'vue'
import Box from '@/components/box/Box.vue'
import shrinkSvg from '@/assets/images/fullscreen-shrink.svg'
import expandSvg from '@/assets/images/fullscreen-expand.svg'

const isFullscream = ref(false)

function fullscream() {
  if (document.fullscreenElement) {
    document.exitFullscreen()
    isFullscream.value = false
  } else {
    document.documentElement.requestFullscreen()
    isFullscream.value = true
  }
}
</script>

<template>
  <Box @click="fullscream">
    <img v-if="isFullscream" :src="shrinkSvg" alt="取消全屏" class="img">
    <img v-else :src="expandSvg" alt="全屏" class="img">
  </Box>
</template>

<style scoped>
.img {
  width: 24px;
  height: 24px;
}
</style>
