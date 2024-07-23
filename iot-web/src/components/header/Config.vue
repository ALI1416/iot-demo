<script lang="ts" setup>
import Box from '@/components/box/Box.vue'
import configSvg from '@/assets/images/config.svg'
import {useRouter} from 'vue-router'

const router = useRouter()
</script>

<template>
  <Box @click="router.push('/config')">
    <img :src="configSvg" alt="配置" class="img">
  </Box>
</template>

<style scoped>
.img {
  width: 24px;
  height: 24px;
}
</style>
