<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router'
import {useGatewayStore} from '@/stores/gateway'
import {type Ref, ref, watch} from 'vue'
import type {MenuItemType} from '@/types'

const route = useRoute()
const router = useRouter()

const gatewayStore = useGatewayStore()

const list: Ref<MenuItemType[]> = ref([
  {
    key: 'SCADA',
    name: 'SCADA',
  },
  {
    key: 'Summary',
    name: '汇总',
  }
])

const selected = ref('SCADA')

for (let gateway of gatewayStore.gatewayList) {
  list.value.push({
    key: String(gateway.sn),
    name: gateway.name
  })
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
    if (tag === 'summary') {
      selected.value = 'Summary'
    } else if (tag === 'single') {
      selected.value = paths[2]
    }
  } else {
    selected.value = 'SCADA'
  }
}

function itemChange(key: string) {
  if (key === 'SCADA') {
    router.push('/')
  } else if (key === 'Summary') {
    router.push('/summary/income')
  } else {
    router.push('/single/' + key + '/scada')
  }
}
</script>

<template>
  <div class="menu">
    <el-select v-model="selected" @change="itemChange">
      <el-option
          v-for="item in list"
          :key="item.key"
          :label="item.name"
          :value="item.key"
      >
        <span style="float:left">{{ item.name }}</span>
        <span style="float:right;color:var(--el-text-color-secondary);font-size:12px;">{{ item.key }}</span>
      </el-option>
    </el-select>
  </div>
</template>

<style scoped>
.menu {
  width: 180px;
  margin-right: 5px;
}
</style>
