<script lang="ts" setup>
import refreshSvg from '@/assets/images/refresh.svg'
import {ref} from 'vue'
import type {ReportType} from '@/types'

const props = defineProps<{
  title: string
  reportType?: ReportType
  showRefresh?: boolean
}>()

const emits = defineEmits<{
  query: [Date]
  refresh: [void]
}>()

const date = ref(new Date())

function disabledDate(date: Date) {
  return date > new Date()
}

function dateChange() {
  emits('query', date.value)
}
</script>

<template>
  <div class="graph">
    <div v-if="props.showRefresh" class="title title1">
      <span>{{ props.title }}</span>
      <div>
        <!-- 月报表 -->
        <el-date-picker
            v-if="props.reportType==='MONTH'"
            v-model="date"
            :clearable="false"
            :disabled-date="disabledDate"
            format="YYYY年"
            size="small"
            style="width:80px"
            type="year"
            @change="dateChange"
        />
        <!-- 日报表 -->
        <el-date-picker
            v-if="props.reportType==='DAY'"
            v-model="date"
            :clearable="false"
            :disabled-date="disabledDate"
            format="YYYY年M月"
            size="small"
            style="width:100px"
            type="month"
            @change="dateChange"
        />
        <!-- 小时报表 -->
        <el-date-picker
            v-if="props.reportType==='HOUR'"
            v-model="date"
            :clearable="false"
            :disabled-date="disabledDate"
            format="YYYY年M月D日"
            size="small"
            style="width:120px"
            type="date"
            @change="dateChange"
        />
        <!-- 分钟报表 -->
        <el-date-picker
            v-if="props.reportType==='MINUTE'"
            v-model="date"
            :clearable="false"
            :disabled-date="disabledDate"
            format="YYYY年M月D日H时"
            size="small"
            style="width:150px"
            type="datetime"
            @change="dateChange"
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
  margin-bottom: 5px;
}

.title1 {
  display: flex;
  justify-content: space-between;
}

.title2 {
  text-align: center;
}

.refresh {
  margin-left: 5px;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
</style>
