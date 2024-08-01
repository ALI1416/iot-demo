<script lang="ts" setup>
import {useRoute} from 'vue-router'
import {ref, watch} from 'vue'
import {useGatewayStore} from '@/stores/gateway'
import Gateway from '@/views/device/gateway/Gateway.vue'
import Thermometer from '@/views/device/thermometer/Thermometer.vue'
import ThermoHygrometer from '@/views/device/thermoHygrometer/ThermoHygrometer.vue'
import Unknown from '@/views/device/unknown/Unknown.vue'

const route = useRoute()

const gatewayStore = useGatewayStore()

const gatewaySn = ref(Number(route.params.gatewaySn as string))
const deviceSn = ref(Number(route.params.deviceSn as string))

const deviceType = ref(gatewayStore.getDevice(gatewaySn.value, deviceSn.value)?.type)

watch(() => route.params, (params) => {
  gatewaySn.value = Number(params.gatewaySn as string)
  deviceSn.value = Number(params.deviceSn as string)
  deviceType.value = gatewayStore.getDevice(gatewaySn.value, deviceSn.value)?.type
})
</script>

<template>
  <Gateway v-if="deviceType===0" :gateway-sn="gatewaySn"/>
  <Thermometer v-else-if="deviceType===1" :device-sn="deviceSn" :gateway-sn="gatewaySn"/>
  <ThermoHygrometer v-else-if="deviceType===2" :device-sn="deviceSn" :gateway-sn="gatewaySn"/>
  <Unknown v-else/>
</template>
