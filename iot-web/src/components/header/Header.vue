<script lang="ts" setup>
import Menu from '@/components/header/Menu.vue'
import Config from '@/components/header/Config.vue'
import Debug from '@/components/header/Debug.vue'
import Refresh from '@/components/header/Refresh.vue'
import Fullscreen from '@/components/header/Fullscreen.vue'
import Datetime from '@/components/header/Datetime.vue'

const webTitle = import.meta.env.VITE_WEB_TITLE
</script>

<template>
  <div class="header">
    <div class="left">
      <Menu/>
      <Config/>
      <Debug/>
    </div>
    <div class="title">{{ webTitle }}</div>
    <div class="right">
      <Refresh/>
      <Fullscreen/>
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
</style>
