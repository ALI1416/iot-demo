<script lang="ts" setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {useGatewayStore} from '@/stores/gateway'
import {useGlobalStore} from '@/stores/global'
import type {EChartsType} from 'echarts/core'
import * as echarts from 'echarts/core'
import Graph from '@/components/graph/Graph.vue'

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
  chart = echarts.init(gauge.value, 'darkTheme')
  chart.setOption(option)
})

onUnmounted(() => {
})

const gauge = ref()
let chart: EChartsType

const option: any = {
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

</script>

<template>
  <div class="content">
    <div ref="gauge" class="gauge"/>
    <Graph>
      <div ref="line" class="line"/>
    </Graph>
  </div>
</template>

<style scoped>
.content {
}

.gauge {
  border: black solid 1px;
  width: 300px;
  height: 300px;
}

.line {
  border: black solid 1px;
  width: 100%;
  height: 300px;
}
</style>
