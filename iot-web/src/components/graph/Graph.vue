<script lang="ts" setup>
import refreshSvg from '@/assets/images/refresh.svg'
import {ref} from 'vue'
import type {DateType} from '@/types'

const props = defineProps<{
  title: string
  dateType?: DateType
  showRefresh?: boolean
}>()

const emits = defineEmits<{
  query: [Date]
  refresh: [void]
}>()

const year = ref(new Date())
const month = ref(new Date())
const day = ref(new Date())
const hour = ref(new Date())

function disabledDate(date: Date) {
  return date > new Date()
}
</script>

<template>
  <div class="graph">
    <div v-if="props.showRefresh" class="title title1">
      <span>{{ props.title }}</span>
      <div>
        <!-- 年 -->
        <el-date-picker
            v-if="props.dateType==='YEAR'"
            v-model="year"
            :clearable="false"
            :disabled-date="disabledDate"
            format="YYYY"
            size="small"
            type="year"
            @change="emits('query',year)"
        />
        <!-- 月 -->
        <el-date-picker
            v-if="props.dateType==='MONTH'"
            v-model="month"
            :clearable="false"
            :disabled-date="disabledDate"
            format="YYYY-MM"
            size="small"
            type="month"
            @change="emits('query',month)"
        />
        <!-- 日 -->
        <el-date-picker
            v-if="props.dateType==='DAY'"
            v-model="day"
            :clearable="false"
            :disabled-date="disabledDate"
            format="YYYY-MM-DD"
            size="small"
            type="date"
            @change="emits('query',day)"
        />
        <!-- 小时 -->
        <el-date-picker
            v-if="props.dateType==='HOUR'"
            v-model="hour"
            :clearable="false"
            :disabled-date="disabledDate"
            format="YYYY-MM-DD HH时"
            size="small"
            type="datetime"
            @change="emits('query',hour)"
        />
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
  margin-left: 30px;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
</style>
