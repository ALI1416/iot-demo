import {defineStore} from 'pinia'
import {computed, ref} from 'vue'
import type {Device, Gateway} from '@/network/api/gateway'

/**
 * 网关
 */
export const useGatewayStore = defineStore('gateway', () => {

  /**
   * 网关列表
   */
  const gatewayList = ref<Gateway[]>([])

  /**
   * 网关序号列表
   */
  const gatewaySnList = computed<number[]>(() => {
    return gatewayList.value.map(m => m.sn)
  })

  /**
   * 获取网关
   * @param gatewaySn 网关序号
   * @return 网关
   */
  function getGateway(gatewaySn: number): Gateway | undefined {
    let gateway = gatewayList.value.filter(g => g.sn === gatewaySn)
    if (gateway.length) {
      return gateway[0]
    }
  }

  /**
   * 获取设备
   * @param gatewaySn 网关序号
   * @param deviceSn 设备序号
   * @return 设备
   */
  function getDevice(gatewaySn: number, deviceSn: number): Device | undefined {
    let gateway = getGateway(gatewaySn)
    if (gateway) {
      let device = gateway.deviceList.filter(d => d.sn === deviceSn)
      if (device.length) {
        return device[0]
      }
    }
  }

  return {
    gatewayList, gatewaySnList, getGateway, getDevice
  }

})
