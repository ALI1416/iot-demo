import dayjs, {Dayjs} from 'dayjs'

/**
 * 时间工具
 */

// region 常量

/**
 * 60分钟每隔1分钟
 * @return number[60] 返回[0,1,2...59]
 */
const dateHour1Minute: number[] = getDateHour1Minute()
/**
 * 24小时每隔1小时
 * @return number[24] 返回[0,1,2...23]
 */
const dateDay1Hour: number[] = getDateDay1Hour()
/**
 * 24小时每隔5分钟
 * @return string[288] 返回[00:00,00:05,00:10...23:55]
 */
const dateDay5Minute: string[] = getDateDay5Minute()
/**
 * 24小时每隔30分钟
 * @return string[48] 返回[00:30,01:00,01:30...23:30]
 */
const dateDay30Minute: string[] = getDateDay30Minute()
/**
 * 12个月每隔1个月
 * @return string[12] 返回[1,2,3...12]
 */
const dateYear1Month: number[] = getDateYear1Month()
/**
 * 平年每隔1天
 * @return string[365] 例如[01-01,01-02,01-03...02-28,03-01...12-31]
 */
const dateCommonYear1Day: string[] = getDateCommonYear1Day()
/**
 * 闰年每隔1天
 * @return string[366] 例如[01-01,01-02,01-03...02-28,02-29,03-01...12-31]
 */
const dateLeapYear1Day: string[] = getDateLeapYear1Day()

/**
 * 60分钟每隔1分钟
 * @return number[60] 返回[0,1,2...59]
 */
function getDateHour1Minute(): number[] {
  let result: number[] = []
  for (let i = 0; i < 60; i++) {
    result.push(i)
  }
  return result
}

/**
 * 24小时每隔1小时
 * @return number[24] 返回[0,1,2...23]
 */
function getDateDay1Hour(): number[] {
  let result: number[] = []
  for (let i = 0; i < 24; i++) {
    result.push(i)
  }
  return result
}

/**
 * 24小时每隔5分钟
 * @return string[288] 返回[00:00,00:05,00:10...23:55]
 */
function getDateDay5Minute(): string[] {
  let result: string[] = []
  for (let i = 0; i < 24; i++) {
    for (let j = 0; j < 12; j++) {
      result.push(i.toString().padStart(2, '0') + ':' + (j * 5).toString().padStart(2, '0'))
    }
  }
  return result
}

/**
 * 24小时每隔30分钟
 * @return string[48] 返回[00:30,01:00,01:30...23:30]
 */
function getDateDay30Minute(): string[] {
  let result: string[] = []
  for (let i = 0; i < 24; i++) {
    let hour = i.toString().padStart(2, '0') + ':'
    result.push(hour + '00')
    result.push(hour + '30')
  }
  return result
}

/**
 * 12个月每隔1个月
 * @return string[12] 返回[1,2,3...12]
 */
function getDateYear1Month(): number[] {
  let result: number[] = []
  for (let i = 1; i < 13; i++) {
    result.push(i)
  }
  return result
}

/**
 * 平年每隔1天
 * @return string[365] 例如[01-01,01-02,01-03...02-28,03-01...12-31]
 */
function getDateCommonYear1Day(): string[] {
  let result: string[] = []
  result.push(...getDateSpecifyMonth1Day(1, 31))
  result.push(...getDateSpecifyMonth1Day(2, 28))
  result.push(...getDateSpecifyMonth1Day(3, 31))
  result.push(...getDateSpecifyMonth1Day(4, 30))
  result.push(...getDateSpecifyMonth1Day(5, 31))
  result.push(...getDateSpecifyMonth1Day(6, 30))
  result.push(...getDateSpecifyMonth1Day(7, 31))
  result.push(...getDateSpecifyMonth1Day(8, 31))
  result.push(...getDateSpecifyMonth1Day(9, 30))
  result.push(...getDateSpecifyMonth1Day(10, 31))
  result.push(...getDateSpecifyMonth1Day(11, 30))
  result.push(...getDateSpecifyMonth1Day(12, 31))
  return result
}

/**
 * 闰年每隔1天
 * @return string[366] 例如[01-01,01-02,01-03...02-28,02-29,03-01...12-31]
 */
function getDateLeapYear1Day(): string[] {
  let result: string[] = getDateCommonYear1Day()
  result.splice(59, 0, '02-29')
  return result
}

// endregion

// region 方法

/**
 * 6小时每隔1分钟
 * @param group 组 0~3
 * @return string[360] 例如[00:00,00:01,00:02...05:59]
 */
function getDate6Hour1Minute(group: number): string[] {
  let result: string[] = []
  for (let i = group * 4; i < group * 4 + 6; i++) {
    for (let j = 0; j < 60; j++) {
      result.push(i.toString().padStart(2, '0') + ':' + (j).toString().padStart(2, '0'))
    }
  }
  return result
}

/**
 * 7天每隔1天
 * @param date 时间(第7天)
 * @return string[7] 例如[01-01,01-02,01-03...01-07]
 */
function getDate7Day1Day(date: Date | Dayjs): string[] {
  let result: string[] = []
  let d = dayjs(date)
  for (let i = 0; i < 7; i++) {
    result.push(d.format('MM-DD'))
    d = d.add(-1, 'day')
  }
  return result.reverse()
}

/**
 * 7天每隔1小时
 * @param date 时间(第7天)
 * @return string[168] 例如[01-01 00:00,01-01 01:00,01-01 02:00...01-07 23:00]
 */
function getDate7Day1Hour(date: Date | Dayjs): string[] {
  let result: string[] = []
  let v = getDate7Day1Day(date)
  for (let i = 0; i < 7; i++) {
    let day = v[i] + ' '
    for (let j = 0; j < 24; j++) {
      result.push(day + dateDay1Hour[j])
    }
  }
  return result
}

/**
 * 7天每隔30分钟
 * @param date 时间(第7天)
 * @return string[336] 例如[01-01 00:00,01-01 00:30,01-01 01:00...01-07 23:30]
 */
function getDate7Day30Minute(date: Date | Dayjs): string[] {
  let result: string[] = []
  let v = getDate7Day1Day(date)
  for (let i = 0; i < 7; i++) {
    let day = v[i] + ' '
    for (let j = 0; j < 48; j++) {
      result.push(day + dateDay30Minute[j])
    }
  }
  return result
}

/**
 * 本月每隔1天
 * @param year 年
 * @param month 月
 * @return number[28|29|30|31] 例如[1,2,3...31]
 */
function getDateMonth1Day(year: number, month: number): number[] {
  let result: number[] = []
  let d = dayjs().year(year).month(month)
  for (let i = 1; i < d.daysInMonth() + 1; i++) {
    result.push(i)
  }
  return result
}

/**
 * 指定月份每隔1天
 * @param month 月
 * @param days 本月天数
 * @return string[days] 例如[01-01,01-02,01-03...01-31]
 */
function getDateSpecifyMonth1Day(month: number, days: number) {
  let monthFix = month.toString().padStart(2, '0') + '-'
  let result: string[] = []
  for (let i = 1; i < days + 1; i++) {
    result.push(monthFix + i.toString().padStart(2, '0'))
  }
  return result
}

/**
 * 本年每隔1天
 * @param year 年
 * @return string[365|366] 例如[01-01,01-02,01-03...12-31]
 */
function getDateYear1Day(year: number): string[] {
  return dayjs().year(year).month(2).daysInMonth() === 28 ? dateCommonYear1Day : dateLeapYear1Day
}

// endregion

// region 特殊方法

/**
 * 获取起始时间
 * @param date 时间
 * @return string 例如2024-01-01 00:00:00.000
 */
function getStartDate(date: Date | Dayjs): string {
  return dayjs(date).format('YYYY-MM-DD 00:00:00.000')
}

/**
 * 获取结束时间
 * @param date 时间
 * @return string 例如2024-01-01 23:59:59.999
 */
function getEndDate(date: Date | Dayjs): string {
  return dayjs(date).format('YYYY-MM-DD 23:59:59.999')
}

/**
 * 获取时间差
 * @param startDate 起始时间
 * @param endDate 结束时间
 * @return string 例如<br>
 * `>=1天` 1天2小时3分钟4秒<br>
 * `>=1小时` 2小时3分钟4秒<br>
 * `>=1分钟` 3分钟4秒<br>
 * `<1分钟` 4秒
 */
function getDuration(startDate: string, endDate: string): string {
  let duration = dayjs.duration(dayjs(endDate).diff(dayjs(startDate)))
  let ms = duration.asMilliseconds()
  if (ms >= 24 * 60 * 60 * 1000) {
    return Math.trunc(duration.asDays()) + duration.format('天H小时m分钟s秒')
  } else if (ms >= 60 * 60 * 1000) {
    return duration.format('H小时m分钟s秒')
  } else if (ms >= 60 * 1000) {
    return duration.format('m分钟s秒')
  } else {
    return duration.format('s秒')
  }
}

// endregion

export {
  dateHour1Minute,
  dateDay1Hour,
  dateDay5Minute,
  dateDay30Minute,
  dateYear1Month,
  dateCommonYear1Day,
  dateLeapYear1Day,
  getDate6Hour1Minute,
  getDate7Day1Day,
  getDate7Day1Hour,
  getDate7Day30Minute,
  getDateMonth1Day,
  getDateSpecifyMonth1Day,
  getDateYear1Day,
  getStartDate,
  getEndDate,
  getDuration
}
