<script lang="ts" setup>
const props = defineProps<{
  gatewaySn: number,
  deviceSn: number
}>()
</script>

<template>
  Thermometer {{ props.gatewaySn }} {{ props.deviceSn }}
</template>

<style scoped>

</style>
