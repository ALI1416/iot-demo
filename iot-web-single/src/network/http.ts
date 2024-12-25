import axios, {type AxiosRequestConfig} from 'axios'
import {ElNotification} from 'element-plus'

function http(config: AxiosRequestConfig<any>): Promise<any> {
  return create()(config)
}

function create() {
  const instance = axios.create({
    baseURL: import.meta.env.VITE_WEB_URL
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

export {http}
