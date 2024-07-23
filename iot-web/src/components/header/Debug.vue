<script lang="ts" setup>
import Box from '@/components/box/Box.vue'
import debugSvg from '@/assets/images/debug.svg'
import {useRouter} from 'vue-router'

const router = useRouter()
</script>

<template>
  <Box @click="router.push('/debug')">
    <img :src="debugSvg" alt="调试" class="img">
  </Box>
</template>

<style scoped>
.img {
  width: 24px;
  height: 24px;
}
</style>
