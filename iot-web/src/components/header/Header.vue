<script lang="ts" setup>
import Datetime from '@/components/header/Datetime.vue'
import Refresh from '@/components/header/Refresh.vue'
import FullScreen from '@/components/header/FullScreen.vue'

const webTitle = import.meta.env.VITE_WEB_TITLE
</script>

<template>
  <div class="header">
    <div class="title">{{ webTitle }}</div>
    <div class="right">
      <Refresh/>
      <FullScreen/>
      <Datetime/>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  height: 60px;
  width: 100%;
  padding: 5px 10px;
  box-sizing: border-box;
  justify-content: space-between;
}

.right {
  display: flex;
  width: 350px;
  justify-content: right;
}

.title {
  margin-left: 350px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  font-weight: bold;
}
</style>
