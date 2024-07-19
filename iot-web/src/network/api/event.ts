import {http} from '@/network/http'
import type {ReportType} from '@/types'

/**
 * 事件
 */
const PREFIX = 'event/'

/**
 * 报表分页请求
 */
type ReportPageRequest = {
  /**
   * 网关序号
   */
  gatewaySn: number
  /**
   * 设备序号
   */
  deviceSn?: number
  /**
   * 命令代码
   */
  commandCode?: number
  /**
   * 报表类型
   */
  reportType: ReportType
  /**
   * 报表-年
   */
  year: number
  /**
   * 报表-月
   */
  month?: number
  /**
   * 报表-日
   */
  day?: number
  /**
   * 报表-小时
   */
  hour?: number
  /**
   * 报表-分钟
   */
  minute?: number
  /**
   * 分页-页码
   */
  pages?: number
  /**
   * 分页-每页条数
   */
  rows?: number
  /**
   * 分页-排序
   */
  orderBy?: string
}

/**
 * 报表分页响应
 */
type ReportPageResponse = {
  /**
   * 总页数(从1开始)
   */
  pages: number
  /**
   * 当前页码(从1开始)
   */
  page: number
  /**
   * 总条数
   */
  total: bigint
  /**
   * 每页条数
   */
  rows: number
  /**
   * 当前页条数
   */
  row: number
  /**
   * 数据列表
   */
  list: ReportPageDataResponse[]
}

/**
 * 报表分页数据响应
 */
type ReportPageDataResponse = {
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
  createTime: Date
  /**
   * 报表-月
   */
  month: number
  /**
   * 报表-日
   */
  day?: number
  /**
   * 报表-小时
   */
  hour?: number
  /**
   * 报表-分钟
   */
  minute?: number
  /**
   * 事件
   */
  event: any
}

/**
 * 分页查询报表
 * @return Promise 报表分页响应
 */
function findReportPage(request: ReportPageRequest): Promise<ReportPageResponse> {
  return http({
    url: PREFIX + 'findReportPage',
    method: 'post',
    data: request
  })
}

export {
  type ReportPageRequest,
  type ReportPageResponse,
  type ReportPageDataResponse,
  findReportPage
}
