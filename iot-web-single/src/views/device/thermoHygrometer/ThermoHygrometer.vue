<script lang="ts" setup>
const props = defineProps<{
  gatewaySn: number,
  deviceSn: number
}>()
</script>

<template>
  ThermoHygrometer {{ props.gatewaySn }} {{ props.deviceSn }}
</template>

<style scoped>

</style>
