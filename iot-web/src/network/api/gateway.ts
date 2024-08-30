import {http} from '@/network/http'
import homeSvg from '@/assets/images/home.svg'
import thermometerSvg from '@/assets/images/thermometer.svg'
import thermoHygrometerSvg from '@/assets/images/thermo-hygrometer.svg'
import unknownSvg from '@/assets/images/unknown.svg'

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
   * 设备类型 0网关 1温度计 2温湿度计
   */
  type: DeviceType
  /**
   * 备注
   */
  comment: string
}

/**
 * 设备类型 0网关 1温度计 2温湿度计
 */
type DeviceType = 0 | 1 | 2

/**
 * 获取网关列表
 * @return Promise 网关列表
 */
function get(): Promise<Gateway[]> {
  return http({
    url: PREFIX + 'get'
  })
}

/**
 * 获取设备类型名
 * @param deviceType DeviceType
 * @return 设备类型名
 */
function getDeviceTypeName(deviceType: DeviceType): string {
  switch (deviceType) {
    case 0:
      return '网关'
    case 1:
      return '温度计'
    case 2:
      return '温湿度计'
    default:
      return '未知设备'
  }
}

/**
 * 获取设备图片src
 * @param deviceType DeviceType
 * @return 图片src
 */
function getDeviceTypeImgSrc(deviceType: DeviceType): string {
  switch (deviceType) {
    case 0:
      return homeSvg
    case 1:
      return thermometerSvg
    case 2:
      return thermoHygrometerSvg
    default:
      return unknownSvg
  }
}

export {
  type Gateway,
  type Device,
  type DeviceType,
  get,
  getDeviceTypeName,
  getDeviceTypeImgSrc
}
