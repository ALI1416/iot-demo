<script lang="ts" setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {useGatewayStore} from '@/stores/gateway'
import {useGlobalStore} from '@/stores/global'
import type {EChartsType} from 'echarts/core'
import * as echarts from 'echarts/core'
import Graph from '@/components/graph/Graph.vue'
import {dateDay1Hour, dateHour1Minute, getDateMonth1Day, getDateString, getDateYear1Day} from '@/utils/DataUtil'

const props = defineProps<{
  gatewaySn: number,
  deviceSn: number
}>()

const gatewayStore = useGatewayStore()
const globalStore = useGlobalStore()

/**
 * 10分钟刷新一次
 */
watch(() => globalStore.refresh10Minute, () => {
})

onMounted(() => {
  realtimeChart = echarts.init(realtime.value, 'darkTheme')
  realtimeChart.setOption(realtimeOption)
  historyHourChart = echarts.init(historyHour.value, 'darkTheme')
  historyHourChart.setOption(historyHourOption)
  historyDayChart = echarts.init(historyDay.value, 'darkTheme')
  historyDayChart.setOption(historyDayOption)
  historyMonthChart = echarts.init(historyMonth.value, 'darkTheme')
  historyMonthChart.setOption(historyMonthOption)
  historyYearChart = echarts.init(historyYear.value, 'darkTheme')
  historyYearChart.setOption(historyYearOption)
})

onUnmounted(() => {
})

const itemPrevious = 'previous'
const itemNext = 'next'
const items: Item[] = [
  {
    key: itemPrevious,
    value: '上一组'
  },
  {
    key: itemNext,
    value: '下一组'
  }
]

// region 实时温度

const realtime = ref()
let realtimeChart: EChartsType

const realtimeOption: any = {
  series: {
    type: 'gauge',
    radius: '75%',
    center: ['50%', '55%'],
    min: -100,
    max: 100,
    splitNumber: 10,
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
          [0.2, '#6CF'],
          [0.8, '#9C9'],
          [1, '#F99']
        ]
      }
    },
    axisTick: {
      show: true,
      distance: -30,
      splitNumber: 10,
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
    },
    data: [-88.88]
  }
}

// endregion

// region 小时历史温度

const historyHour = ref()
let historyHourChart: EChartsType

const historyHourOption: any = {
  color: ['#9C9'],
  grid: {
    top: 30,
    bottom: 70,
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
  dataZoom: {
    type: 'slider'
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

// region 日历史温度

const historyDay = ref()
let historyDayChart: EChartsType

const historyDayOption: any = {
  color: ['#9C9', '#F99', '#6CF'],
  grid: {
    top: 30,
    bottom: 70,
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
  dataZoom: {
    type: 'slider'
  },
  series: [
    {
      name: '平均温度',
      type: 'line',
      data: [26, 27.5, 28, 27.1, 26, 27, 28.88, 24, 25, 26.23, 27.77]
    },
    {
      name: '最高温度',
      type: 'line',
      data: [28, 28, 28.5, 28, 27, 28, 29, 28, 28, 28, 28]
    },
    {
      name: '最低温度',
      type: 'line',
      data: [25, 26, 25, 26, 25, 26, 26, 23.5, 25, 25, 26]
    }
  ]
}

const day = ref(new Date())
const dayTitle = ref(getDateString(day.value, 'DAY'))

function historyDayQuery(date: Date) {
  day.value = date
  dayTitle.value = getDateString(date, 'DAY')
}

function historyDayRefresh() {
  console.log('historyDayRefresh')
}

// endregion

// region 月历史温度

const historyMonth = ref()
let historyMonthChart: EChartsType

const historyMonthOption: any = {
  color: ['#9C9', '#F99', '#6CF'],
  grid: {
    top: 30,
    bottom: 70,
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
    nameGap: -8,
    data: getDateMonth1Day(2024, 8),
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
  dataZoom: {
    type: 'slider'
  },
  series: [
    {
      name: '平均温度',
      type: 'line',
      data: [26, 27.5, 28, 27.1, 26, 27, 28.88, 24, 25, 26.23, 27.77]
    },
    {
      name: '最高温度',
      type: 'line',
      data: [28, 28, 28.5, 28, 27, 28, 29, 28, 28, 28, 28]
    },
    {
      name: '最低温度',
      type: 'line',
      data: [25, 26, 25, 26, 25, 26, 26, 23.5, 25, 25, 26]
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

// region 年历史温度

const historyYear = ref()
let historyYearChart: EChartsType

const historyYearOption: any = {
  color: ['#9C9', '#F99', '#6CF'],
  grid: {
    top: 30,
    bottom: 70,
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
    nameGap: -8,
    data: getDateYear1Day(2024),
    axisLabel: {
      interval: 30,
      formatter: function (value: string) {
        return Number(value.substring(0, 2))
      }
    }
  },
  yAxis: {
    name: '℃',
    type: 'value',
    max: 30,
    min: 22
  },
  dataZoom: {
    type: 'slider'
  },
  series: [
    {
      name: '平均温度',
      type: 'line',
      data: [26, 27.5, 28, 27.1, 26, 27, 28.88, 24, 25, 26.23, 27.77]
    },
    {
      name: '最高温度',
      type: 'line',
      data: [28, 28, 28.5, 28, 27, 28, 29, 28, 28, 28, 28]
    },
    {
      name: '最低温度',
      type: 'line',
      data: [25, 26, 25, 26, 25, 26, 26, 23.5, 25, 25, 26]
    }
  ]
}

const year = ref(new Date())
const yearTitle = ref(getDateString(year.value, 'YEAR'))

function historyYearQuery(date: Date) {
  year.value = date
  yearTitle.value = getDateString(date, 'YEAR')
}

function historyYearRefresh() {
  console.log('historyYearRefresh')
}

// endregion

</script>

<template>
  <div class="topBox">
    <Graph title="实时温度">
      <div ref="realtime" class="realtime"/>
    </Graph>
    <Graph
        :title="hourTitle+' 历史温度'"
        date-type="HOUR"
        show-refresh
        @query="historyHourQuery"
        @refresh="historyHourRefresh"
    >
      <div ref="historyHour" class="historyHour"/>
    </Graph>
  </div>
  <div class="centerBox">
    <Graph
        :title="dayTitle+' 历史温度'"
        date-type="DAY"
        show-refresh
        @query="historyDayQuery"
        @refresh="historyDayRefresh"
    >
      <div ref="historyDay" class="historyDay"/>
    </Graph>
    <Graph
        :title="monthTitle+' 历史温度'"
        date-type="MONTH"
        show-refresh
        @query="historyMonthQuery"
        @refresh="historyMonthRefresh"
    >
      <div ref="historyMonth" class="historyMonth"/>
    </Graph>
  </div>
  <div class="bottomBox">
    <Graph
        :title="yearTitle+' 历史温度'"
        date-type="YEAR"
        show-refresh
        @query="historyYearQuery"
        @refresh="historyYearRefresh"
    >
      <div ref="historyYear" class="historyYear"/>
    </Graph>
  </div>
</template>

<style scoped>
.topBox {
  padding: 0 10px;
  display: flex;
  justify-content: space-between;
}

.realtime {
  width: 300px;
  height: 300px;
}

.historyHour {
  width: calc(100vw - 380px);
  height: 300px;
}

.centerBox {
  padding: 20px 10px;
  display: flex;
  justify-content: space-between;
}

.historyDay, .historyMonth {
  width: calc(100vw / 2 - 40px);
  height: 300px;
}

.bottomBox {
  padding: 0 10px;
}

.historyYear {
  width: calc(100vw - 50px);
  height: 300px;
}
</style>
