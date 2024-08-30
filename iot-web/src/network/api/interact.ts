import {http} from '@/network/http'

/**
 * 交互
 */
const PREFIX = 'interact/'

/**
 * 获取最后一条成功消息请求
 */
type LastRequest = {
  /**
   * 网关序号
   */
  gatewaySn: number
  /**
   * 设备序号
   */
  deviceSn: number
  /**
   * 命令代码
   */
  commandCode: number
}

/**
 * 获取最后一条成功消息响应
 */
type LastResponse = {
  /**
   * id
   */
  id: bigint
  /**
   * 设备序号
   */
  deviceSn: number
  /**
   * 命令代码
   */
  commandCode: number
  /**
   * 创建时间
   */
  createTime: string
  /**
   * 更新时间
   */
  updateTime: string
  /**
   * 交互类型
   */
  type: number
  /**
   * 错误代码
   */
  errorCode: number
  /**
   * 响应
   */
  response: any
}

/**
 * 获取最后一条成功消息
 * @return Promise 报表分页响应
 */
function findLast(request: LastRequest): Promise<LastResponse> {
  return http({
    url: PREFIX + 'findLast',
    params: request
  })
}

export {type LastRequest, type LastResponse, findLast}
