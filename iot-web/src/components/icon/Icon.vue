<script lang="ts" setup>
const emits = defineEmits<{
  click: [void]
}>()
</script>

<template>
  <div class="box" @click="emits('click')">
    <slot/>
  </div>
</template>

<style scoped>
.box {
  width: 30px;
  height: 30px;
  margin: 0 5px;
  padding: 2px;
  border: 1px solid #CCC;
  border-radius: 3px;
  cursor: pointer;
}
</style>
