<script lang="ts" setup>
import {ref} from 'vue'
import minSvg from '@/assets/images/fullscreen-shrink.min.svg'
import maxSvg from '@/assets/images/fullscreen-expand.min.svg'

const isFullScream = ref(false)

function fullScream() {
  if (document.fullscreenElement) {
    document.exitFullscreen()
    isFullScream.value = false
  } else {
    document.documentElement.requestFullscreen()
    isFullScream.value = true
  }
}
</script>

<template>
  <div class="fullScream" @click="fullScream">
    <img
        :src="isFullScream ? minSvg : maxSvg"
        alt="全屏"
    >
  </div>
</template>

<style scoped>
.fullScream {
  width: 30px;
  height: 30px;
  border-radius: 3px;
  margin-right: 10px;
  padding: 4px;
  border: 1px solid #CCC;
  cursor: pointer;
}
</style>
