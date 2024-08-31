<script lang="ts" setup>
import refreshSvg from '@/assets/images/refresh.svg'
import {onMounted, ref} from 'vue'
import type {ReportType} from '@/types'
import {CaretLeft, CaretRight} from '@element-plus/icons-vue'
import dayjs, {type Dayjs} from 'dayjs'

const props = defineProps<{
  title: string
  reportType?: ReportType
  showRefresh?: boolean
}>()

const emits = defineEmits<{
  query: [Date]
}>()

const date = ref(new Date())

function disabledDate(date: Date) {
  return date > new Date()
}

function query() {
  emits('query', date.value)
}

function left() {
  leftOrRight(-1)
}

function right() {
  leftOrRight(1)
}

function leftOrRight(value: number) {
  let d: Dayjs
  switch (props.reportType) {
    case 'MONTH': {
      d = dayjs(date.value).add(value, 'year')
      break
    }
    case 'DAY': {
      d = dayjs(date.value).add(value, 'month')
      break
    }
    case 'HOUR': {
      d = dayjs(date.value).add(value, 'day')
      break
    }
    case 'MINUTE':
    default: {
      d = dayjs(date.value).add(value, 'hour')
      break
    }
  }
  let dd = d.toDate()
  if (!disabledDate(dd)) {
    date.value = dd
    query()
  }
}

onMounted(() => {
  query()
})
</script>

<template>
  <div class="graph">
    <div v-if="props.showRefresh" class="title title1">
      <span>{{ props.title }}</span>
      <div>
        <!-- 月报表 -->
        <CaretLeft class="arrow" @click="left"/>
        <CaretRight class="arrow" @click="right"/>
        <el-date-picker
            v-if="props.reportType==='MONTH'"
            v-model="date"
            :clearable="false"
            :disabled-date="disabledDate"
            format="YYYY年"
            size="small"
            style="width:80px"
            type="year"
            @change="query"
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
            @change="query"
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
            @change="query"
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
            @change="query"
        />
        <img :src="refreshSvg" alt="刷新" class="refresh" @click="query">
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

.arrow {
  margin-right: 5px;
  height: 20px;
  cursor: pointer;
  color: #666;
}

.refresh {
  margin-left: 5px;
  width: 20px;
  height: 20px;
  cursor: pointer;
}
</style>
