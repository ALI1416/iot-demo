import {http} from '@/network/http'

/**
 * 网关
 */
const PREFIX = 'gateway/'

/**
 * 网关
 */
type Gateway = {
  /**
   * id
   */
  id: bigint
  /**
   * 网关序号
   */
  sn: number
  /**
   * 网关名称
   */
  name: string
  /**
   * 上一次在线时间
   */
  lastOnlineTime: string
  /**
   * 备注
   */
  comment: string
  /**
   * 设备列表
   */
  deviceList: Device[]
}

/**
 * 设备
 */
type Device = {
  /**
   * id
   */
  id: bigint
  /**
   * 设备序号
   */
  sn: number
  /**
   * 设备名称
   */
  name: string
  /**
   * 设备类型 0网关 1温度计
   */
  type: DeviceType
  /**
   * 备注
   */
  comment: string
}

/**
 * 设备类型 0网关 1温度计
 */
type DeviceType = 0 | 1

/**
 * 获取网关列表
 * @return Promise 网关列表
 */
function getCache(): Promise<Gateway[]> {
  return http({
    url: PREFIX + 'getCache'
  })
}

export {
  type Gateway,
  type Device,
  type DeviceType,
  getCache
}
