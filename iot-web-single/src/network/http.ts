import axios, {type AxiosRequestConfig} from 'axios'
import {ElNotification} from 'element-plus'

function http(config: AxiosRequestConfig): Promise<any> {
  return create()(config)
}

function create() {
  const instance = axios.create({
    baseURL: import.meta.env.VITE_API_URL
  })
  instance.interceptors.request.use(config => {
    return config
  })
  instance.interceptors.response.use(res => {
    if (res.data.code !== 0) {
      ElNotification({
        title: '接口调用异常',
        message: res.data.message,
        type: 'error'
      })
      return Promise.reject(res)
    }
    return res.data.data
  }, error => {
    ElNotification({
      title: '接口调用失败',
      message: error,
      type: 'error'
    })
    return Promise.reject(error)
  })
  return instance
}

let baseUrl = import.meta.env.BASE_URL
if (baseUrl !== '/') {
  baseUrl += '/'
}

const baseUrlFix = baseUrl

function http2(url: string, params?: any): Promise<any> {
  return create2()({
    url,
    params
  })
}

function create2() {
  const instance = axios.create({
    baseURL: baseUrlFix
  })
  instance.interceptors.request.use(config => {
    return config
  })
  instance.interceptors.response.use(res => {
    return res.data
  }, error => {
    ElNotification({
      title: '静态数据加载失败',
      message: error,
      type: 'error'
    })
    return Promise.reject(error)
  })
  return instance
}

export {
  baseUrlFix,
  http,
  http2
}
