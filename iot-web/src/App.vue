<script lang="ts" setup>
import {zhCn} from 'element-plus/es/locale/index'
import {useSystemStore} from '@/stores/system'
import {useGatewayStore} from '@/stores/gateway'
import {useProtocolStore} from '@/stores/protocol'
import {getCache} from '@/network/api/gateway'
import {getInfo} from '@/network/api/protocol'
import {onUnmounted, ref} from 'vue'
import {activate, deactivate} from '@/stores/stomp'
import Header from '@/components/header/Header.vue'

const systemStore = useSystemStore()
const gatewayStore = useGatewayStore()
const protocolStore = useProtocolStore()

const gatewayListOk = ref(false)
const protocolInfoListOk = ref(false)
const wsOk = ref(false)

// 获取网关列表
getCache().then(res => {
  gatewayStore.gatewayList = res
  gatewayListOk.value = true
})

// 获取协议信息列表
getInfo().then(res => {
  protocolStore.protocolInfoList = res
  protocolInfoListOk.value = true
})

// stomp连接
activate(connectCallback)

/**
 * stomp连接成功回调
 */
function connectCallback() {
  wsOk.value = true
}

// 页面关闭
onUnmounted(() => {
  // 关闭stomp连接
  deactivate()
})

// 重新调整尺寸(200毫秒防抖)
let timer: number | undefined = undefined
window.onresize = () => {
  if (!timer) {
    timer = setTimeout(() => {
      timer = undefined
      systemStore.resize = Math.random()
    }, 200)
  }
}

// 23小时59分刷新一次
setInterval(() => {
  location.reload()
}, (24 * 60 - 1) * 60 * 1000)
</script>

<template>
  <!-- ElementPlus国际化：中文 -->
  <el-config-provider
      v-if="gatewayListOk&&protocolInfoListOk&&wsOk"
      :locale="zhCn"
  >
    <Header/>
    <router-view/>
  </el-config-provider>
</template>
