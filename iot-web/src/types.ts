/**
 * 类型
 */

/**
 * 报表类型
 * 月|日|小时|分钟
 */
type ReportType = 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE'

/**
 * 菜单项类型
 */
type MenuItemType = {
  /**
   * 键
   */
  key: string,
  /**
   * 名称
   */
  name: string
}

export type {ReportType, MenuItemType}
