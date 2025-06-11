/// <reference types="vite/client" />
// https://cn.vitejs.dev/guide/env-and-mode.html

interface ImportMeta {
  readonly env: ImportMetaEnv
}

interface ImportMetaEnv {
  /**
   * 网站标题
   */
  readonly VITE_WEB_TITLE: string
  /**
   * API URL
   */
  readonly VITE_API_URL: string
  /**
   * WebSocket URL
   */
  readonly VITE_WS_URL: string
}
