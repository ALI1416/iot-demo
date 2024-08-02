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
  border-radius: 3px;
  margin: 0 5px;
  border: 1px solid #CCC;
  padding: 2px;
  cursor: pointer;
}
</style>
