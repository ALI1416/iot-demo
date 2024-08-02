<script lang="ts" setup>
import Icon from '@/components/icon/Icon.vue'
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
      <Icon @click="router.push('/')">
        <img :src="homeSvg" alt="主页" class="img">
      </Icon>
      <Menu/>
      <Icon @click="router.push('/config')">
        <img :src="configSvg" alt="配置" class="img">
      </Icon>
      <Icon @click="router.push('/debug')">
        <img :src="debugSvg" alt="调试" class="img">
      </Icon>
    </div>
    <div class="title">{{ webTitle }}</div>
    <div class="right">
      <Icon @click="refresh">
        <img :src="refreshSvg" alt="刷新" class="img">
      </Icon>
      <Icon @click="fullscream">
        <img v-if="isFullscream" :src="shrinkSvg" alt="取消全屏" class="img">
        <img v-else :src="expandSvg" alt="全屏" class="img">
      </Icon>
      <Datetime/>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  height: 50px;
  justify-content: space-between;
}

.left, .right {
  display: flex;
  padding-top: 5px;
}

.title {
  font-size: 36px;
  font-weight: bold;
}

.img {
  width: 24px;
  height: 24px;
}
</style>
