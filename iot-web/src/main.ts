import {createApp} from 'vue'
import App from '@/App.vue'
import {createPinia} from 'pinia'
import router from '@/router'

import '@/assets/css/base.css'
import '@/assets/fonts/fonts.css'

import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import localeData from 'dayjs/plugin/localeData'
import duration from 'dayjs/plugin/duration'

import * as echarts from 'echarts/core'
import {SVGRenderer} from 'echarts/renderers'
import {GridComponent, LegendComponent, MarkLineComponent, TooltipComponent} from 'echarts/components'
import {BarChart, GaugeChart, LineChart} from 'echarts/charts'
import lightTheme from '@/assets/json/echarts.light.theme.json'
import darkTheme from '@/assets/json/echarts.dark.theme.json'

import 'element-plus/es/components/notification/style/css'

dayjs.locale('zh-cn')
dayjs.extend(localeData)
dayjs.extend(duration)

echarts.use([
  SVGRenderer,
  GridComponent,
  LegendComponent,
  TooltipComponent,
  MarkLineComponent,
  BarChart,
  LineChart,
  GaugeChart
])
echarts.registerTheme('lightTheme', lightTheme)
echarts.registerTheme('darkTheme', darkTheme)

createApp(App)
  .use(createPinia())
  .use(router)
  .mount('#app')
