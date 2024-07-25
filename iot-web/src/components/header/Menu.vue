<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router'
import {useGatewayStore} from '@/stores/gateway'
import {ref, watch} from 'vue'

const route = useRoute()
const router = useRouter()

const gatewayStore = useGatewayStore()

const selected = ref(['home'])

const options: any = ref([
  {
    value: 'home',
    label: '主页',
  },
  {
    value: 'config',
    label: '配置',
  },
  {
    value: 'debug',
    label: '调试',
  }
])

for (let gateway of gatewayStore.gatewayList) {
  let option: any = {
    value: String(gateway.sn),
    label: gateway.name,
    children: []
  }
  for (let device of gateway.deviceList) {
    option.children.push({
      value: String(device.sn),
      label: device.name
    })
  }
  options.value.push(option)
}

// 直接访问链接时选中(防止路由地址获取太早)
setTimeout(() => {
  itemSelected(route.path)
}, 500)

watch(() => route.path, (path) => {
  itemSelected(path)
})

function itemSelected(path: string) {
  let paths = path.split('/')
  let tag = paths[1]
  if (tag) {
    if (tag === 'config') {
      selected.value = ['config']
    } else if (tag === 'debug') {
      selected.value = ['debug']
    } else {
      selected.value = [paths[2], paths[3]]
    }
  } else {
    selected.value = ['home']
  }
}

function itemChange(selected: string[]) {
  let v0 = selected[0]
  let v1 = selected[1]
  if (v0 === 'home') {
    router.push('/')
  } else if (v0 === 'config') {
    router.push('/config')
  } else if (v0 === 'debug') {
    router.push('/debug')
  } else {
    router.push('/device/' + v0 + '/' + v1)
  }
}
</script>

<template>
  <div class="menu">
    <el-cascader v-model="selected" :options="options" :props="{expandTrigger: 'hover'}" @change="itemChange"/>
  </div>
</template>

<style scoped>
.menu {
  width: 180px;
  margin: 0 5px;
}
</style>
