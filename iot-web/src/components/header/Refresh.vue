<script lang="ts" setup>
import refreshSvg from '@/assets/images/refresh.min.svg'

function refresh() {
  location.reload()
}
</script>

<template>
  <div class="refresh" @click="refresh">
    <img :src="refreshSvg" alt="刷新">
  </div>
</template>

<style scoped>
.refresh {
  width: 34px;
  height: 30px;
  border-radius: 3px;
  margin-right: 10px;
  padding: 4px;
  border: 1px solid #CCC;
  cursor: pointer;
}
</style>
