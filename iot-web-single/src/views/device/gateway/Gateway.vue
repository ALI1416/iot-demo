<script lang="ts" setup>
import {useGatewayStore} from '@/stores/gateway'
import GatewayInfo, {type Device, type Gateway} from '@/components/gatewayInfo/GatewayInfo.vue'

const props = defineProps<{
  gatewaySn: number
}>()

const gatewayStore = useGatewayStore()

function getGatewayList() {
  let gatewayList: Gateway[] = []
  let gateway = gatewayStore.getGateway(props.gatewaySn) as Gateway
  let deviceList: Device[] = []
  for (let device of gateway.deviceList) {
    deviceList.push({
      sn: device.sn,
      name: device.name,
      type: device.type
    })
  }
  gatewayList.push({
    sn: gateway.sn,
    name: gateway.name,
    deviceList
  })
  return gatewayList
}
</script>

<template>
  <GatewayInfo :gateway-list="getGatewayList()"/>
</template>
