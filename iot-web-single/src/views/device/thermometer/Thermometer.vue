<script lang="ts" setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {useSystemStore} from '@/stores/system'
import {useGlobalStore} from '@/stores/global'
import type {EChartsType} from 'echarts/core'
import * as echarts from 'echarts/core'
import Graph from '@/components/graph/Graph.vue'
import {dateDay1Hour, dateHour1Minute, dateYear1Month, getDateMonth1Day, getDateString} from '@/utils/DataUtil'
import {subscribe, unsubscribeGroup} from '@/stores/stomp'
import {round2} from '@/utils/MathUtil'
import {findLast} from '@/network/api/interact'

// region 系统

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
  realtimeChart.clear()
  updateRealtimeData(undefined)
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

// region 设置界限

function setRange() {
  findLast({gatewaySn: props.gatewaySn, deviceSn: props.deviceSn, commandCode: 40100}).then(res => {
    if (!res) {
      return
    }
    // 整数
    let max: number = res.response.temperatureMax
    let min: number = res.response.temperatureMin
    // 最大值为上限+5并且是5的倍数
    let seriesMax = Math.ceil((max + 0.1) / 5) * 5
    realtimeOption.series.max = seriesMax
    // 最小值为上限-5并且是5的倍数
    let seriesMin = Math.floor((min - 0.1) / 5) * 5
    realtimeOption.series.min = seriesMin
    // 分隔符每隔5度一个
    let seriesTotal = seriesMax - seriesMin
    realtimeOption.series.splitNumber = seriesTotal / 5
    // 上限
    realtimeOption.series.axisLine.lineStyle.color[1][0] = 1 - (seriesMax - max) / seriesTotal
    // 下限
    realtimeOption.series.axisLine.lineStyle.color[0][0] = (min - seriesMin) / seriesTotal
    realtimeChart.setOption(realtimeOption)
  })
}

// endregion

// region stomp

const STOMP_GROUP = 'thermometer'

const EVENT_PREFIX = '/topic/iot/event/'

function subscribeData() {
  subscribe(STOMP_GROUP, EVENT_PREFIX + props.gatewaySn + '/' + props.deviceSn + '/' + 10100, function (res) {
    let e = JSON.parse(res.body).event
    updateRealtimeData(e.temperature)
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
    max: 35,
    min: 15,
    splitNumber: 4,
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
          [0.25, '#6CF'],
          [0.75, '#9C9'],
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
 * 更新实时温度数据
 * @param temperature 温度
 */
function updateRealtimeData(temperature: number | undefined) {
  if (typeof temperature !== 'undefined') {
    realtimeOption.series.data = [round2(temperature / 10000)]
  } else {
    delete realtimeOption.series.data
  }
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
    right: 10
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
    max: 30,
    min: 22
  },
  visualMap: {
    type: 'piecewise',
    top: 0,
    right: '50%',
    orient: 'horizontal',
    pieces: [
      {
        lt: 25,
        color: '#6CF'
      },
      {
        gte: 25,
        lte: 28,
        color: '#9C9'
      },
      {
        gt: 28,
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
          yAxis: 28
        },
        {
          lineStyle: {
            color: '#6CF',
          },
          name: '温度下限',
          yAxis: 25
        }
      ]
    },
    data: [26, 27, 27.5, 28, 27.6, 27.1, 26, 27, 28.88, 28.7, 28.6, 23.33, 25, 26.23, 27.77, 25, 24, 24.5, 24.6, 28.5, 24]
  }
}

const minute = ref(new Date())
const minuteTitle = ref(getDateString(minute.value, 'MINUTE'))

function historyMinuteQuery(date: Date) {
  minute.value = date
  minuteTitle.value = getDateString(date, 'MINUTE')
}

function historyMinuteRefresh() {
  console.log('historyMinuteRefresh')
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
    max: 30,
    min: 22
  },
  series: [
    {
      name: '最高温度',
      type: 'line',
      data: [28, 28, 28.5, 28, 27, 28, 29, 28, 28, 28, 28]
    },
    {
      name: '平均温度',
      type: 'line',
      data: [26, 27.5, 28, 27.1, 26, 27, 28.88, 24, 25, 26.23, 27.77]
    },
    {
      name: '最低温度',
      type: 'line',
      data: [25, 26, 25, 26, 25, 26, 26, 23.5, 25, 25, 26]
    }
  ]
}

const hour = ref(new Date())
const hourTitle = ref(getDateString(hour.value, 'HOUR'))

function historyHourQuery(date: Date) {
  hour.value = date
  hourTitle.value = getDateString(date, 'HOUR')
}

function historyHourRefresh() {
  console.log('historyHourRefresh')
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
    max: 30,
    min: 22
  },
  series: [
    {
      name: '最高温度',
      type: 'line',
      data: [28, 28, 28.5, 28, 27, 28, 29, 28, 28, 28, 28]
    },
    {
      name: '平均温度',
      type: 'line',
      data: [26, 27.5, 28, 27.1, 26, 27, 28.88, 24, 25, 26.23, 27.77]
    },
    {
      name: '最低温度',
      type: 'line',
      data: [25, 26, 25, 26, 25, 26, 26, 23.5, 25, 25, 26]
    }
  ]
}

function historyDayQuery(date: Date) {
  day.value = date
  dayTitle.value = getDateString(date, 'DAY')
}

function historyDayRefresh() {
  console.log('historyDayRefresh')
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
    max: 30,
    min: 22
  },
  series: [
    {
      name: '最高温度',
      type: 'line',
      data: [28, 28, 28.5, 28, 27, 28, 29, 28, 28, 28, 28, 28]
    },
    {
      name: '平均温度',
      type: 'line',
      data: [26, 27.5, 28, 27.1, 26, 27, 28.88, 24, 25, 26.23, 27.77, 27]
    },
    {
      name: '最低温度',
      type: 'line',
      data: [25, 26, 25, 26, 25, 26, 26, 23.5, 25, 25, 26, 26]
    }
  ]
}

const month = ref(new Date())
const monthTitle = ref(getDateString(month.value, 'MONTH'))

function historyMonthQuery(date: Date) {
  month.value = date
  monthTitle.value = getDateString(date, 'MONTH')
}

function historyMonthRefresh() {
  console.log('historyMonthRefresh')
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
        @query="historyMinuteQuery"
        @refresh="historyMinuteRefresh"
    >
      <div ref="historyMinute" class="historyMinute"/>
    </Graph>
  </div>
  <div class="bottomBox">
    <Graph
        :title="hourTitle+' 历史温度'"
        report-type="HOUR"
        show-refresh
        @query="historyHourQuery"
        @refresh="historyHourRefresh"
    >
      <div ref="historyHour" class="historyHour"/>
    </Graph>
    <Graph
        :title="dayTitle+' 历史温度'"
        report-type="DAY"
        show-refresh
        @query="historyDayQuery"
        @refresh="historyDayRefresh"
    >
      <div ref="historyDay" class="historyDay"/>
    </Graph>
    <Graph
        :title="monthTitle+' 历史温度'"
        report-type="MONTH"
        show-refresh
        @query="historyMonthQuery"
        @refresh="historyMonthRefresh"
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
