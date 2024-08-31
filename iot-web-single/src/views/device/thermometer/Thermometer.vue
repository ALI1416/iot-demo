<script lang="ts" setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {useSystemStore} from '@/stores/system'
import {useGlobalStore} from '@/stores/global'
import type {EChartsType} from 'echarts/core'
import * as echarts from 'echarts/core'
import Graph from '@/components/graph/Graph.vue'
import {dateDay1Hour, dateHour1Minute, dateYear1Month, getDateMonth1Day, getDateString} from '@/utils/DataUtil'
import {subscribe, unsubscribeGroup} from '@/stores/stomp'
import {ceil5, ceil5Upper, floor5, floor5Lower, round2} from '@/utils/MathUtil'
import {findLast} from '@/network/api/interact'
import {findReportPage, type ReportPageDataResponse} from '@/network/api/event'

// region 系统

const MAX = 30
const MIN = 20
const RANGE_MAX = 35
const RANGE_MIN = 15
const RANGE = RANGE_MAX - RANGE_MIN

const props = defineProps<{
  gatewaySn: number,
  deviceSn: number
}>()

const systemStore = useSystemStore()
const globalStore = useGlobalStore()

/**
 * 10分钟刷新一次
 */
watch(() => globalStore.refresh10Minute, () => {
  queryMinute(minute.value)
})

onMounted(() => {
  realtimeChart = echarts.init(realtime.value, 'darkTheme')
  realtimeChart.setOption(realtimeOption)
  historyMinuteChart = echarts.init(historyMinute.value, 'darkTheme')
  historyMinuteChart.setOption(historyMinuteOption)
  historyHourChart = echarts.init(historyHour.value, 'darkTheme')
  historyHourChart.setOption(historyHourOption)
  historyDayChart = echarts.init(historyDay.value, 'darkTheme')
  historyDayChart.setOption(historyDayOption)
  historyMonthChart = echarts.init(historyMonth.value, 'darkTheme')
  historyMonthChart.setOption(historyMonthOption)
  setRange()
  subscribeData()
})

watch(props, () => {
  setRange()
  unsubscribeGroup(STOMP_GROUP)
  subscribeData()
  resetRealtime()
  resetMinute()
  queryMinute(minute.value)
  resetHour()
  queryHour(hour.value)
  resetDay()
  queryDay(day.value)
  resetMonth()
  queryMonth(month.value)
})

/**
 * 重新调整尺寸
 */
watch(() => systemStore.resize, () => {
  realtimeChart.resize()
  historyMinuteChart.resize()
  historyHourChart.resize()
  historyDayChart.resize()
  historyMonthChart.resize()
})

onUnmounted(() => {
  unsubscribeGroup(STOMP_GROUP)
})

// endregion

// region HTTP 设置界限

function setRange() {
  findLast({gatewaySn: props.gatewaySn, deviceSn: props.deviceSn, commandCode: 40100}).then(res => {
    if (!res) {
      return
    }
    // 整数
    let max: number = res.response.temperatureMax
    let min: number = res.response.temperatureMin
    /* 实时温度 */
    // 最大值
    let rangeMax = ceil5Upper(max)
    realtimeOption.series.max = rangeMax
    // 最小值
    let rangeMin = floor5Lower(min)
    realtimeOption.series.min = rangeMin
    // 分隔符
    let range = rangeMax - rangeMin
    realtimeOption.series.splitNumber = range / 5
    // 上限
    realtimeOption.series.axisLine.lineStyle.color[1][0] = 1 - (rangeMax - max) / range
    // 下限
    realtimeOption.series.axisLine.lineStyle.color[0][0] = (min - rangeMin) / range
    realtimeChart.setOption(realtimeOption)
    /* 分钟报表 */
    // 上限
    historyMinuteOption.visualMap.pieces[2].gt = max
    historyMinuteOption.series.markLine.data[0].yAxis = max
    // 中间
    historyMinuteOption.visualMap.pieces[1].gte = min
    historyMinuteOption.visualMap.pieces[1].lte = max
    // 下限
    historyMinuteOption.visualMap.pieces[0].lt = min
    historyMinuteOption.series.markLine.data[1].yAxis = min
    historyMinuteChart.setOption(historyMinuteOption)
  })
}

// endregion

// region WS 实时温度

const STOMP_GROUP = 'thermometer'

const EVENT_PREFIX = '/topic/iot/event/'

function subscribeData() {
  subscribe(STOMP_GROUP, EVENT_PREFIX + props.gatewaySn + '/' + props.deviceSn + '/' + 10100, function (res) {
    let e = JSON.parse(res.body).event
    updateRealtime(e.temperature)
  })
}

// endregion

// region 实时温度

const realtime = ref()
let realtimeChart: EChartsType

const realtimeOption: any = {
  series: {
    type: 'gauge',
    radius: '75%',
    center: ['50%', '55%'],
    max: RANGE_MAX,
    min: RANGE_MIN,
    splitNumber: RANGE / 5,
    progress: {
      show: true,
      width: 10,
      itemStyle: {
        color: '#55F'
      }
    },
    pointer: {
      show: false
    },
    axisLine: {
      lineStyle: {
        width: 20,
        color: [
          [(MIN - RANGE_MIN) / RANGE, '#6CF'],
          [1 - (RANGE_MAX - MAX) / RANGE, '#9C9'],
          [1, '#F99']
        ]
      }
    },
    axisTick: {
      show: true,
      distance: -30,
      splitNumber: 5,
      lineStyle: {
        color: 'auto',
        width: 2
      }
    },
    splitLine: {
      distance: -32,
      lineStyle: {
        color: 'auto',
        width: 4
      }
    },
    axisLabel: {
      distance: -15,
      fontSize: 15
    },
    detail: {
      offsetCenter: [0, '-5%'],
      fontSize: 40,
      formatter: '{value} ℃'
    }
  }
}

/**
 * 更新实时温度
 * @param temperature 温度
 */
function updateRealtime(temperature: number) {
  realtimeOption.series.data = [round2(temperature / 10000)]
  realtimeChart.setOption(realtimeOption)
}

/**
 * 重置实时温度
 */
function resetRealtime() {
  realtimeChart.clear()
  delete realtimeOption.series.data
  realtimeOption.series.max = RANGE_MAX
  realtimeOption.series.min = RANGE_MIN
  realtimeOption.series.splitNumber = RANGE / 5
  realtimeOption.series.axisLine.lineStyle.color[0][0] = (MIN - RANGE_MIN) / RANGE
  realtimeOption.series.axisLine.lineStyle.color[1][0] = 1 - (RANGE_MAX - MAX) / RANGE
  realtimeChart.setOption(realtimeOption)
}

// endregion

// region 分钟报表

const historyMinute = ref()
let historyMinuteChart: EChartsType

const historyMinuteOption: any = {
  color: ['#9C9'],
  grid: {
    top: 30,
    bottom: 20,
    left: 30,
    right: 20
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    name: '分钟',
    nameTextStyle: {
      lineHeight: 28,
      verticalAlign: 'top'
    },
    nameGap: -20,
    data: dateHour1Minute,
    axisLabel: {
      interval: 4
    }
  },
  yAxis: {
    name: '℃',
    type: 'value',
    max: RANGE_MAX,
    min: RANGE_MIN
  },
  visualMap: {
    type: 'piecewise',
    top: 0,
    right: '50%',
    orient: 'horizontal',
    pieces: [
      {
        lt: MIN,
        color: '#6CF'
      },
      {
        gte: MIN,
        lte: MAX,
        color: '#9C9'
      },
      {
        gt: MAX,
        color: '#F99'
      }
    ],
    outOfRange: {
      color: '#999'
    }
  },
  series: {
    name: '温度',
    type: 'line',
    markPoint: {
      data: [
        {
          itemStyle: {
            color: '#F99',
          },
          name: '最高温度',
          type: 'max'
        },
        {
          itemStyle: {
            color: '#6CF',
          },
          symbolRotate: 180,
          label: {
            offset: [0, 10]
          },
          name: '最低温度',
          type: 'min'
        }
      ]
    },
    markLine: {
      data: [
        {
          lineStyle: {
            color: '#F99',
          },
          name: '温度上限',
          yAxis: MAX
        },
        {
          lineStyle: {
            color: '#6CF',
          },
          name: '温度下限',
          yAxis: MIN
        }
      ]
    }
  }
}

const minute = ref(new Date())
const minuteTitle = ref(getDateString(minute.value, 'MINUTE'))

function queryMinute(date: Date) {
  minute.value = date
  minuteTitle.value = getDateString(date, 'MINUTE')
  findReportPage({
    gatewaySn: props.gatewaySn,
    deviceSn: props.deviceSn,
    commandCode: 10100,
    reportType: 'MINUTE',
    year: date.getFullYear(),
    month: date.getMonth() + 1,
    day: date.getDate(),
    hour: date.getHours(),
    rows: 60,
    orderBy: 'minute'
  }).then(res => {
    updateMinute(res.list)
  })
}

/**
 * 更新分钟报表
 */
function updateMinute(list: ReportPageDataResponse[]) {
  let temperatureAvg: (number | undefined)[] = []
  if (list.length) {
    for (let i = 0; i < 60; i++) {
      let p = list.filter(p => p.minute === i).pop()
      if (p) {
        temperatureAvg.push(round2(p.event.temperatureAvg / 10000))
      } else {
        temperatureAvg.push(undefined)
      }
    }
    let temperatureAvgArray = list.map(p => p.event.temperatureAvg as number)
    let max = Math.max(...temperatureAvgArray) / 10000
    let min = Math.min(...temperatureAvgArray) / 10000
    historyMinuteOption.yAxis.max = ceil5(max)
    historyMinuteOption.yAxis.min = floor5(min)
  }
  historyMinuteOption.series.data = temperatureAvg
  historyMinuteChart.setOption(historyMinuteOption)
}

/**
 * 重置分钟报表
 */
function resetMinute() {
  historyMinuteOption.yAxis.max = RANGE_MAX
  historyMinuteOption.yAxis.min = RANGE_MIN
  historyMinuteOption.visualMap.pieces[0].lt = MIN
  historyMinuteOption.visualMap.pieces[1].gte = MIN
  historyMinuteOption.visualMap.pieces[1].lte = MAX
  historyMinuteOption.visualMap.pieces[2].gt = MAX
  historyMinuteOption.series.markLine.data[0].yAxis = MAX
  historyMinuteOption.series.markLine.data[1].yAxis = MIN
  historyMinuteChart.setOption(historyMinuteOption)
}

// endregion

// region 小时报表

const historyHour = ref()
let historyHourChart: EChartsType

const historyHourOption: any = {
  color: ['#F99', '#9C9', '#6CF'],
  grid: {
    top: 30,
    bottom: 20,
    left: 30,
    right: 10
  },
  legend: {},
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    name: '小时',
    nameTextStyle: {
      lineHeight: 28,
      verticalAlign: 'top'
    },
    nameGap: -20,
    data: dateDay1Hour,
    axisLabel: {
      interval: 1
    }
  },
  yAxis: {
    name: '℃',
    type: 'value',
    max: RANGE_MAX,
    min: RANGE_MIN
  },
  series: [
    {
      name: '最高温度',
      type: 'line'
    },
    {
      name: '平均温度',
      type: 'line'
    },
    {
      name: '最低温度',
      type: 'line'
    }
  ]
}

const hour = ref(new Date())
const hourTitle = ref(getDateString(hour.value, 'HOUR'))

function queryHour(date: Date) {
  hour.value = date
  hourTitle.value = getDateString(date, 'HOUR')
  findReportPage({
    gatewaySn: props.gatewaySn,
    deviceSn: props.deviceSn,
    commandCode: 10100,
    reportType: 'HOUR',
    year: date.getFullYear(),
    month: date.getMonth() + 1,
    day: date.getDate(),
    rows: 24,
    orderBy: 'hour'
  }).then(res => {
    updateHour(res.list)
  })
}

/**
 * 更新小时报表
 */
function updateHour(list: ReportPageDataResponse[]) {
  let temperatureMax: (number | undefined)[] = []
  let temperatureAvg: (number | undefined)[] = []
  let temperatureMin: (number | undefined)[] = []
  if (list.length) {
    for (let i = 0; i < 24; i++) {
      let p = list.filter(p => p.hour === i).pop()
      if (p) {
        temperatureMax.push(round2(p.event.temperatureMax / 10000))
        temperatureAvg.push(round2(p.event.temperatureAvg / 10000))
        temperatureMin.push(round2(p.event.temperatureMin / 10000))
      } else {
        temperatureMax.push(undefined)
        temperatureAvg.push(undefined)
        temperatureMin.push(undefined)
      }
    }
    let max = Math.max(...list.map(p => p.event.temperatureMax as number)) / 10000
    let min = Math.min(...list.map(p => p.event.temperatureMin as number)) / 10000
    historyHourOption.yAxis.max = ceil5(max)
    historyHourOption.yAxis.min = floor5(min)
  }
  historyHourOption.series[0].data = temperatureMax
  historyHourOption.series[1].data = temperatureAvg
  historyHourOption.series[2].data = temperatureMin
  historyHourChart.setOption(historyHourOption)
}

/**
 * 重置小时报表
 */
function resetHour() {
  historyHourOption.yAxis.max = RANGE_MAX
  historyHourOption.yAxis.min = RANGE_MIN
  historyHourChart.setOption(historyHourOption)
}

// endregion

// region 日温度

const historyDay = ref()
let historyDayChart: EChartsType

const day = ref(new Date())
const dayTitle = ref(getDateString(day.value, 'DAY'))

const historyDayOption: any = {
  color: ['#F99', '#9C9', '#6CF'],
  grid: {
    top: 30,
    bottom: 20,
    left: 30,
    right: 10
  },
  legend: {},
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    name: '日',
    nameTextStyle: {
      lineHeight: 28,
      verticalAlign: 'top'
    },
    nameGap: 0,
    data: getDateMonth1Day(day.value),
    axisLabel: {
      interval: 1
    }
  },
  yAxis: {
    name: '℃',
    type: 'value',
    max: RANGE_MAX,
    min: RANGE_MIN
  },
  series: [
    {
      name: '最高温度',
      type: 'line'
    },
    {
      name: '平均温度',
      type: 'line'
    },
    {
      name: '最低温度',
      type: 'line'
    }
  ]
}

function queryDay(date: Date) {
  day.value = date
  dayTitle.value = getDateString(date, 'DAY')
  findReportPage({
    gatewaySn: props.gatewaySn,
    deviceSn: props.deviceSn,
    commandCode: 10100,
    reportType: 'DAY',
    year: date.getFullYear(),
    month: date.getMonth() + 1,
    rows: historyDayOption.xAxis.data.length,
    orderBy: 'day'
  }).then(res => {
    updateDay(res.list)
  })
}

/**
 * 更新日报表
 */
function updateDay(list: ReportPageDataResponse[]) {
  let temperatureMax: (number | undefined)[] = []
  let temperatureAvg: (number | undefined)[] = []
  let temperatureMin: (number | undefined)[] = []
  if (list.length) {
    for (let i = 0; i < historyDayOption.xAxis.data.length; i++) {
      let p = list.filter(p => p.day === i).pop()
      if (p) {
        temperatureMax.push(round2(p.event.temperatureMax / 10000))
        temperatureAvg.push(round2(p.event.temperatureAvg / 10000))
        temperatureMin.push(round2(p.event.temperatureMin / 10000))
      } else {
        temperatureMax.push(undefined)
        temperatureAvg.push(undefined)
        temperatureMin.push(undefined)
      }
    }
    let max = Math.max(...list.map(p => p.event.temperatureMax as number)) / 10000
    let min = Math.min(...list.map(p => p.event.temperatureMin as number)) / 10000
    historyDayOption.yAxis.max = ceil5(max)
    historyDayOption.yAxis.min = floor5(min)
  }
  historyDayOption.series[0].data = temperatureMax
  historyDayOption.series[1].data = temperatureAvg
  historyDayOption.series[2].data = temperatureMin
  historyDayChart.setOption(historyDayOption)
}

/**
 * 重置日报表
 */
function resetDay() {
  historyDayOption.yAxis.max = RANGE_MAX
  historyDayOption.yAxis.min = RANGE_MIN
  historyDayChart.setOption(historyDayOption)
}

// endregion

// region 月报表

const historyMonth = ref()
let historyMonthChart: EChartsType

const historyMonthOption: any = {
  color: ['#F99', '#9C9', '#6CF'],
  grid: {
    top: 30,
    bottom: 20,
    left: 30,
    right: 10
  },
  legend: {},
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    name: '月',
    nameTextStyle: {
      lineHeight: 28,
      verticalAlign: 'top'
    },
    nameGap: 0,
    data: dateYear1Month
  },
  yAxis: {
    name: '℃',
    type: 'value',
    max: RANGE_MAX,
    min: RANGE_MIN
  },
  series: [
    {
      name: '最高温度',
      type: 'line'
    },
    {
      name: '平均温度',
      type: 'line'
    },
    {
      name: '最低温度',
      type: 'line'
    }
  ]
}

const month = ref(new Date())
const monthTitle = ref(getDateString(month.value, 'MONTH'))

function queryMonth(date: Date) {
  month.value = date
  monthTitle.value = getDateString(date, 'MONTH')
  findReportPage({
    gatewaySn: props.gatewaySn,
    deviceSn: props.deviceSn,
    commandCode: 10100,
    reportType: 'MONTH',
    year: date.getFullYear(),
    rows: 12,
    orderBy: 'month'
  }).then(res => {
    updateMonth(res.list)
  })
}

/**
 * 更新月报表
 */
function updateMonth(list: ReportPageDataResponse[]) {
  let temperatureMax: (number | undefined)[] = []
  let temperatureAvg: (number | undefined)[] = []
  let temperatureMin: (number | undefined)[] = []
  if (list.length) {
    for (let i = 0; i < historyMonthOption.xAxis.data.length; i++) {
      let p = list.filter(p => p.month === i).pop()
      if (p) {
        temperatureMax.push(round2(p.event.temperatureMax / 10000))
        temperatureAvg.push(round2(p.event.temperatureAvg / 10000))
        temperatureMin.push(round2(p.event.temperatureMin / 10000))
      } else {
        temperatureMax.push(undefined)
        temperatureAvg.push(undefined)
        temperatureMin.push(undefined)
      }
    }
    let max = Math.max(...list.map(p => p.event.temperatureMax as number)) / 10000
    let min = Math.min(...list.map(p => p.event.temperatureMin as number)) / 10000
    historyMonthOption.yAxis.max = ceil5(max)
    historyMonthOption.yAxis.min = floor5(min)
  }
  historyMonthOption.series[0].data = temperatureMax
  historyMonthOption.series[1].data = temperatureAvg
  historyMonthOption.series[2].data = temperatureMin
  historyMonthChart.setOption(historyMonthOption)
}

/**
 * 重置月报表
 */
function resetMonth() {
  historyMonthOption.yAxis.max = RANGE_MAX
  historyMonthOption.yAxis.min = RANGE_MIN
  historyMonthChart.setOption(historyMonthOption)
}

// endregion

</script>

<template>
  <div class="topBox">
    <Graph title="实时温度">
      <div ref="realtime" class="realtime"/>
    </Graph>
    <Graph
        :title="minuteTitle+' 历史温度'"
        report-type="MINUTE"
        show-refresh
        @query="queryMinute"
    >
      <div ref="historyMinute" class="historyMinute"/>
    </Graph>
  </div>
  <div class="bottomBox">
    <Graph
        :title="hourTitle+' 历史温度'"
        report-type="HOUR"
        show-refresh
        @query="queryHour"
    >
      <div ref="historyHour" class="historyHour"/>
    </Graph>
    <Graph
        :title="dayTitle+' 历史温度'"
        report-type="DAY"
        show-refresh
        @query="queryDay"
    >
      <div ref="historyDay" class="historyDay"/>
    </Graph>
    <Graph
        :title="monthTitle+' 历史温度'"
        report-type="MONTH"
        show-refresh
        @query="queryMonth"
    >
      <div ref="historyMonth" class="historyMonth"/>
    </Graph>
  </div>
</template>

<style scoped>
.topBox {
  padding: 10px;
  display: flex;
  justify-content: space-between;
}

.realtime {
  width: 300px;
  height: 300px;
}

.historyMinute {
  width: calc(100vw - 380px);
  height: 300px;
}

.bottomBox {
  padding: 10px;
  display: flex;
  justify-content: space-between;
}

.historyHour, .historyDay, .historyMonth {
  width: calc(100vw / 3 - 40px);
  height: 250px;
}
</style>
