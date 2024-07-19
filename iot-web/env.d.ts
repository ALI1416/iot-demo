/// <reference types="vite/client" />
// https://cn.vitejs.dev/guide/env-and-mode.html

interface ImportMeta {
  readonly env: ImportMetaEnv
}

interface ImportMetaEnv {
  /**
   * APP标题
   */
  readonly VITE_APP_TITLE: string
  /**
   * Web URL
   */
  readonly VITE_WEB_URL: string
  /**
   * WebSocket URL
   */
  readonly VITE_WS_URL: string
}
