import {Client, type messageCallbackType} from '@stomp/stompjs'
import {ElNotification} from 'element-plus'

/**
 * stomp客户端
 */
const stomp = new Client({
  onConnect: connectCallback,
  onWebSocketError: errorCallback
})

/**
 * 连接成功回调
 */
function connectCallback() {
  connectCallbackFun()
  resubscribe()
  ElNotification({
    title: 'WebSocket连接成功',
    type: 'success'
  })
}

/**
 * 连接错误回调
 */
function errorCallback() {
  ElNotification({
    title: 'WebSocket连接错误',
    type: 'error'
  })
}

let connectCallbackFun: () => void

/**
 * 连接
 */
export function activate(connectCallbackF: () => void) {
  connectCallbackFun = connectCallbackF
  stomp.brokerURL = import.meta.env.VITE_WS_URL
  stomp.activate()
}

/**
 * 关闭连接
 * @return Promise void
 */
export function deactivate(): Promise<void> {
  return stomp.deactivate()
}

/**
 * 订阅组
 */
export type SubscribeGroup = {
  /**
   * 组
   */
  group: string,
  /**
   * 主题
   */
  topic: string,
  /**
   * ID
   */
  id: string,
  /**
   * 回调
   */
  callback: messageCallbackType
}

/**
 * 订阅组列表
 */
let subscribeGroupList: SubscribeGroup[] = []

/**
 * 订阅(内部)
 * @param group 组
 * @param topic 主题
 * @param callback 回调
 * @return SubscribeGroup
 */
function subscribeInner(group: string, topic: string, callback: messageCallbackType): SubscribeGroup {
  const stompSubscription = stomp.subscribe(topic, callback)
  return {
    group,
    topic,
    id: stompSubscription.id,
    callback
  }
}

/**
 * 订阅
 * @param group 组
 * @param topic 主题
 * @param callback 回调
 * @return SubscribeGroup
 */
export function subscribe(group: string, topic: string, callback: messageCallbackType): SubscribeGroup {
  const subscribeGroup = subscribeInner(group, topic, callback)
  subscribeGroupList.push(subscribeGroup)
  return subscribeGroup
}

/**
 * 取消组订阅
 * @param group 组
 */
export function unsubscribeGroup(group: string) {
  for (let subscribeGroup of subscribeGroupList.filter(s => s.group === group)) {
    stomp.unsubscribe(subscribeGroup.id)
  }
  subscribeGroupList = subscribeGroupList.filter(s => s.group !== group)
}

/**
 * 重新订阅
 */
function resubscribe() {
  let newSubscribeGroupList: SubscribeGroup[] = []
  for (let subscribeGroup of subscribeGroupList) {
    newSubscribeGroupList.push(subscribeInner(subscribeGroup.group, subscribeGroup.topic, subscribeGroup.callback))
  }
  subscribeGroupList = newSubscribeGroupList
}
