<script lang="ts" setup>
import Box from '@/components/box/Box.vue'
import refreshSvg from '@/assets/images/refresh.svg'

function refresh() {
  location.reload()
}
</script>

<template>
  <Box @click="refresh">
    <img :src="refreshSvg" alt="刷新" class="img">
  </Box>
</template>

<style scoped>
.img {
  width: 24px;
  height: 24px;
}
</style>
