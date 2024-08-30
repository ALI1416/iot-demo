<script lang="ts" setup>
import GatewayInfo, {type Device, type Gateway} from '@/components/gatewayInfo/GatewayInfo.vue'
import {useGatewayStore} from '@/stores/gateway'

const gatewayStore = useGatewayStore()

const gatewayList: Gateway[] = []

for (let gateway of gatewayStore.gatewayList) {
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
}
</script>

<template>
  <GatewayInfo :gateway-list="gatewayList"/>
</template>
