<script lang="ts" setup>
import refreshSvg from '@/assets/images/refresh.svg'

const emits = defineEmits<{
  refresh: [void]
}>()

function refresh() {
  emits('refresh')
}
</script>

<template>
  <div class="graph">
    <div class="title">
      <img :src="refreshSvg" alt="刷新" class="refresh" @click="refresh">
    </div>
    <slot/>
  </div>
</template>

<style scoped>
.graph {
  padding: 5px;
  border: black solid 1px;
}

.title {
  text-align: right;
}

.refresh {
  width: 20px;
  height: 20px;
  cursor: pointer;
}
</style>
