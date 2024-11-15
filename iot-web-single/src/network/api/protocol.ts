import {http} from '@/network/http'

/**
 * 协议
 */
const PREFIX = 'protocol/'

/**
 * 协议信息
 */
type ProtocolInfo = {
  /**
   * 命令代码
   */
  commandCode: number
  /**
   * 命令名
   */
  name: string
  /**
   * 协议类型 EVENT事件 FAULT故障 INTERACT交互
   */
  type: ProtocolType
  /**
   * 设备类型 GATEWAY网关 THERMOMETER温度计 THERMO_HYGROMETER温湿度计
   */
  deviceType: DeviceType
  /**
   * 特殊处理
   */
  special?: boolean
  /**
   * 备注
   */
  comment?: string
  /**
   * 事件
   */
  event?: FieldInfo[]
  /**
   * 故障
   */
  fault?: FaultInfo[]
  /**
   * 请求
   */
  request?: FieldInfo[]
  /**
   * 响应
   */
  response?: FieldInfo[]
}

/**
 * 协议类型 EVENT事件 FAULT故障 INTERACT交互
 */
type ProtocolType = 'EVENT' | 'FAULT' | 'INTERACT'

/**
 * 设备类型 GATEWAY网关 THERMOMETER温度计 THERMO_HYGROMETER温湿度计
 */
type DeviceType = 'GATEWAY' | 'THERMOMETER' | 'THERMO_HYGROMETER'

/**
 * 字段信息
 */
type FieldInfo = {
  /**
   * 字段键
   */
  key: string
  /**
   * 字段名
   */
  name: string
  /**
   * 字段类型 ACCUMULATE累积 INSTANT瞬时 STATUS状态
   */
  type?: FieldType
  /**
   * 字段数据类型
   */
  dataType: FieldDataType
  /**
   * 字段特殊数据类型
   */
  specialDataType?: FieldSpecialDataType
  /**
   * 字段单位
   */
  unit?: FieldUnit
  /**
   * 推荐字段单位
   */
  recommend?: FieldUnit
  /**
   * 推荐字段单位转换-乘以
   */
  multiply?: number
  /**
   * 推荐字段单位转换-除以
   */
  divide?: number
  /**
   * 字段状态
   */
  status?: FieldStatus[]
  /**
   * 最大值
   */
  max?: string
  /**
   * 最小值
   */
  min?: string
  /**
   * 最大字符长度
   */
  maxLength?: number
  /**
   * 最小字符长度
   */
  minLength?: number
  /**
   * 特殊处理
   */
  special?: boolean
  /**
   * 备注
   */
  comment?: string
  /**
   * 子信息
   */
  child?: FieldInfo
  /**
   * 子信息列表
   */
  children?: FieldInfo[]
}

/**
 * 字段类型 ACCUMULATE累积 INSTANT瞬时 STATUS状态
 */
type FieldType = 'ACCUMULATE' | 'INSTANT' | 'STATUS'

/**
 * 字段数据类型
 */
type FieldDataType =
  'BOOLEAN'
  | 'BYTE'
  | 'CHAR'
  | 'SHORT'
  | 'INT'
  | 'LONG'
  | 'FLOAT'
  | 'DOUBLE'
  | 'STRING'
  | 'OBJECT'
  | 'ARRAY'

/**
 * 字段特殊数据类型 TIME_HHMM hhmm格式时间
 */
type FieldSpecialDataType = 'TIME_HHMM'

/**
 * 字段单位
 */
type FieldUnit = {
  /**
   * 符号
   */
  symbol: string
  /**
   * 名称
   */
  name: string
}

/**
 * 字段状态
 */
type FieldStatus = {
  /**
   * 值
   */
  value: string
  /**
   * 名称
   */
  name: string
}

/**
 * 故障信息
 */
type FaultInfo = {
  /**
   * 分组名
   */
  groupName: string
  /**
   * 故障名
   */
  name: string[]
}

/**
 * 获取协议信息列表
 * @return Promise 协议信息列表
 */
function get(): Promise<ProtocolInfo[]> {
  return http({
    url: PREFIX + 'get'
  })
}

export {
  type ProtocolInfo,
  type ProtocolType,
  type DeviceType,
  type FieldInfo,
  type FieldType,
  type FieldDataType,
  type FieldSpecialDataType,
  type FieldUnit,
  type FieldStatus,
  type FaultInfo,
  get
}
