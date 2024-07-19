import {defineStore} from 'pinia'
import {computed, ref} from 'vue'
import type {ProtocolInfo} from '@/network/api/protocol'

/**
 * 协议
 */
export const useProtocolStore = defineStore('protocol', () => {

  /**
   * 协议信息列表
   */
  const protocolInfoList = ref<ProtocolInfo[]>([])

  /**
   * 事件信息列表
   */
  const eventInfoList = computed<ProtocolInfo[]>(() => {
    return protocolInfoList.value.filter(p => p.type === 'EVENT')
  })

  /**
   * 故障信息列表
   */
  const faultInfoList = computed<ProtocolInfo[]>(() => {
    return protocolInfoList.value.filter(p => p.type === 'FAULT')
  })

  /**
   * 交互信息列表
   */
  const interactInfoList = computed<ProtocolInfo[]>(() => {
    return protocolInfoList.value.filter(p => p.type === 'INTERACT')
  })

  /**
   * 获取命令
   * @param commandCode 命令序号
   * @return 命令
   */
  function getCommand(commandCode: number): ProtocolInfo | undefined {
    let command = protocolInfoList.value.filter(p => p.commandCode === commandCode)
    if (command.length) {
      return command[0]
    }
  }

  return {
    protocolInfoList, eventInfoList, faultInfoList, interactInfoList,
    getCommand
  }

})
