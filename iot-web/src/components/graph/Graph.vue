<script lang="ts" setup>
import refreshSvg from '@/assets/images/refresh.svg'
import type {StyleValue} from '@vue/runtime-dom'

export type Item = {
  key: string | number
  value: string
}

const props = defineProps<{
  style?: StyleValue
  title: string
  items?: Item[]
  showRefresh?: boolean
}>()

const emits = defineEmits<{
  itemClick: [Item['key']]
  refresh: [void]
}>()

</script>

<template>
  <div :style="props.style" class="graph">
    <div v-if="props.showRefresh" class="title title1">
      <span>{{ props.title }}</span>
      <div>
        <button v-for="item in props.items"
                :key="item.key"
                class="item"
                @click="emits('itemClick',item.key)"
        >{{ item.value }}
        </button>
        <img :src="refreshSvg" alt="刷新" class="refresh" @click="emits('refresh')">
      </div>
    </div>
    <div v-else class="title title2">
      <span>{{ props.title }}</span>
    </div>
    <slot/>
  </div>
</template>

<style scoped>
.graph {
  padding: 5px;
  border: 1px solid #AAA;
  border-radius: 3px;
  display: inline-block;
}

.title {
  height: 25px;
  line-height: 25px;
  font-size: 20px;
}

.title1 {
  display: flex;
  justify-content: space-between;
}

.title2 {
  text-align: center;
}

.item {
  margin: 0 2px;
  height: 20px;
  cursor: pointer;
}

.refresh {
  margin-left: 5px;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
</style>
