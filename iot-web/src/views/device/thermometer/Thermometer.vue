<script lang="ts" setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {useGatewayStore} from '@/stores/gateway'
import {useGlobalStore} from '@/stores/global'
import type {EChartsType} from 'echarts/core'
import * as echarts from 'echarts/core'
import Graph, {type Item} from '@/components/graph/Graph.vue'
import {dateDay5Minute, getDate6Hour1Minute, getDate7Day30Minute} from '@/utils/DataUtil'

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
  history6HourChart = echarts.init(history6Hour.value, 'darkTheme')
  history6HourChart.setOption(history6HourOption)
  history24HourChart = echarts.init(history24Hour.value, 'darkTheme')
  history24HourChart.setOption(history24HourOption)
  history7DayChart = echarts.init(history7Day.value, 'darkTheme')
  history7DayChart.setOption(history7DayOption)
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

// region 6小时历史温度

const history6Hour = ref()
let history6HourChart: EChartsType

const history6HourOption: any = {
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
    data: getDate6Hour1Minute(0),
    axisLabel: {
      interval: 29,
      formatter: function (value: string) {
        return value.substring(0, 5)
      }
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

// endregion

// region 24小时历史温度

const history24Hour = ref()
let history24HourChart: EChartsType

const history24HourOption: any = {
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
    data: dateDay5Minute,
    axisLabel: {
      interval: 11,
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

// endregion

// region 7日历史温度

const history7Day = ref()
let history7DayChart: EChartsType

const history7DayOption: any = {
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
    data: getDate7Day30Minute(new Date()),
    axisLabel: {
      interval: 47,
      formatter: function (value: string) {
        return value.substring(0, 5)
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

// endregion

function history6HourRefresh() {
  console.log('history6HourRefresh')
}

function history6HourItemClick(key: string | number) {
  console.log(key)
}

function history24HourRefresh() {
  console.log('history24HourRefresh')
}

function history24HourItemClick(key: string | number) {
  console.log(key)
}

function history7DayRefresh() {
  console.log('history7DayRefresh')
}

function history7DayItemClick(key: string | number) {
  console.log(key)
}

// endregion

</script>

<template>
  <div class="content">
    <div class="topBox">
      <Graph title="实时温度">
        <div ref="realtime" class="realtime"/>
      </Graph>
      <!-- 1分钟一条 6x60=360 -->
      <Graph
          :items="items"
          show-refresh
          title="6小时历史温度"
          @itemClick="history6HourItemClick"
          @refresh="history6HourRefresh"
      >
        <div ref="history6Hour" class="history6Hour"/>
      </Graph>
    </div>
    <div class="bottomBox">
      <!-- 5分钟一条 24x60/5=288 -->
      <Graph :items="items"
             show-refresh
             title="24小时历史温度"
             @itemClick="history24HourItemClick"
             @refresh="history24HourRefresh"
      >
        <div ref="history24Hour" class="history24Hour"/>
      </Graph>
      <!-- 30分钟一条 7*24x60/30=336 -->
      <Graph :items="items"
             show-refresh
             title="7日历史温度"
             @itemClick="history7DayItemClick"
             @refresh="history7DayRefresh"
      >
        <div ref="history7Day" class="history7Day"/>
      </Graph>
    </div>
  </div>
</template>

<style scoped>
.content {
  overflow: auto;
}

.topBox {
  padding: 0 10px;
  display: flex;
  justify-content: space-between;
}

.realtime {
  width: 300px;
  height: 300px;
}

.history6Hour {
  width: calc(100vw - 380px);
  height: 300px;
}

.bottomBox {
  padding: 20px 10px 0 10px;
  display: flex;
  justify-content: space-between;
}

.history24Hour, .history7Day {
  width: calc(100vw / 2 - 40px);
  height: 300px;
}
</style>
