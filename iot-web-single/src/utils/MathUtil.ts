/**
 * 数学工具
 */

/**
 * 四舍五入保留1位小数
 * @param n 数字
 * @return number 1位小数数字
 */
function round1(n: number): number {
  return Math.round(n * 10) / 10
}

/**
 * 四舍五入保留2位小数
 * @param n 数字
 * @return number 2位小数数字
 */
function round2(n: number): number {
  return Math.round(n * 100) / 100
}

/**
 * 四舍五入保留3位小数
 * @param n 数字
 * @return number 3位小数数字
 */
function round3(n: number): number {
  return Math.round(n * 1000) / 1000
}

/**
 * 5倍上限
 * @param n 数字
 * @return number 整数<br>
 * `0` 0<br>
 * `1` 5<br>
 * `4` 5<br>
 * `5` 5<br>
 * `6` 10
 */
function ceil5(n: number): number {
  return Math.ceil(n / 5) * 5
}

/**
 * 5倍下限
 * @param n 数字
 * @return number 整数<br>
 * `0` 0<br>
 * `1` 0<br>
 * `4` 0<br>
 * `5` 5<br>
 * `6` 5
 */
function floor5(n: number): number {
  return Math.floor(n / 5) * 5
}

/**
 * 5倍上限
 * @param n 数字
 * @return number 整数<br>
 * `0` 5<br>
 * `1` 10<br>
 * `4` 10<br>
 * `5` 10<br>
 * `6` 15
 */
function ceil5Upper(n: number): number {
  return Math.ceil((n + 5) / 5) * 5
}

/**
 * 5倍下限
 * @param n 数字
 * @return number 整数<br>
 * `0` -5<br>
 * `1` -5<br>
 * `4` -5<br>
 * `5` 0<br>
 * `6` 0
 */
function floor5Lower(n: number): number {
  return Math.floor((n - 5) / 5) * 5
}

export {round1, round2, round3, ceil5, floor5, ceil5Upper, floor5Lower}
