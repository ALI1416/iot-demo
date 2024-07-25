<script lang="ts" setup>
import Box from '@/components/box/Box.vue'
import Menu from '@/components/header/Menu.vue'
import Datetime from '@/components/header/Datetime.vue'
import homeSvg from '@/assets/images/home.svg'
import configSvg from '@/assets/images/config.svg'
import debugSvg from '@/assets/images/debug.svg'
import refreshSvg from '@/assets/images/refresh.svg'
import shrinkSvg from '@/assets/images/fullscreen-shrink.svg'
import expandSvg from '@/assets/images/fullscreen-expand.svg'
import {useRouter} from 'vue-router'
import {ref} from 'vue'

const webTitle = import.meta.env.VITE_WEB_TITLE

const router = useRouter()

const isFullscream = ref(false)

function refresh() {
  location.reload()
}

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
  <div class="header">
    <div class="left">
      <Box @click="router.push('/')">
        <img :src="homeSvg" alt="主页" class="img">
      </Box>
      <Menu/>
      <Box @click="router.push('/config')">
        <img :src="configSvg" alt="配置" class="img">
      </Box>
      <Box @click="router.push('/debug')">
        <img :src="debugSvg" alt="调试" class="img">
      </Box>
    </div>
    <div class="title">{{ webTitle }}</div>
    <div class="right">
      <Box @click="refresh">
        <img :src="refreshSvg" alt="刷新" class="img">
      </Box>
      <Box @click="fullscream">
        <img v-if="isFullscream" :src="shrinkSvg" alt="取消全屏" class="img">
        <img v-else :src="expandSvg" alt="全屏" class="img">
      </Box>
      <Datetime/>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  width: 100%;
  height: 50px;
  padding: 5px;
  justify-content: space-between;
}

.left, .right {
  padding-top: 5px;
  display: flex;
}

.title {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  font-weight: bold;
}

.img {
  width: 24px;
  height: 24px;
}
</style>
