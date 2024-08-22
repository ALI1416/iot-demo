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

export {round1, round2, round3}
