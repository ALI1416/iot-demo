import {createApp} from 'vue'
import App from './App.vue'
import {createPinia} from "pinia";
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

createApp(App)
    .use(createPinia())
    .use(ElementPlus, {
        locale: zhCn,
    })
    .mount('#app')
