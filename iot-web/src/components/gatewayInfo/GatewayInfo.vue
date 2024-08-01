<script lang="ts" setup>
import {useRouter} from 'vue-router'
import {type DeviceType, getDeviceTypeImgSrc, getDeviceTypeName} from '@/network/api/gateway'

const router = useRouter()

export type Device = {
  sn: number,
  name: string,
  type: DeviceType
}

export type Gateway = {
  sn: number,
  name: string,
  deviceList: Device[]
}

const props = defineProps<{
  gatewayList: Gateway[]
}>()
</script>

<template>
  <div class="box">
    <div v-for="gateway of props.gatewayList" class="row">
      <span class="title"
            @click="router.push('/device/' + gateway.sn + '/0')"
      >{{ gateway.name }}
      </span>
      <div class="content">
        <div v-for="device of gateway.deviceList" class="item">
          <img :alt="getDeviceTypeName(device.type)"
               :src="getDeviceTypeImgSrc(device.type)"
               :title="getDeviceTypeName(device.type)"
               class="img"
               @click="router.push('/device/' + gateway.sn + '/' + device.sn)"
          >
          <div :title="device.name" class="text">{{ device.name }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.box {
  height: 100%;
  overflow: auto;
}

.row {
  border: 1px solid #CCC;
  margin: 10px 0;
  padding: 10px;
}

.title {
  font-size: 24px;
  cursor: pointer;
}

.content {
  display: flex;
  flex-wrap: wrap;
}

.item {
  width: 100px;
  height: 100px;
  border-radius: 10px;
  margin: 5px;
  border: 1px solid #CCC;
  padding: 5px;
  text-align: center;
}

.img {
  width: 64px;
  height: 64px;
  cursor: pointer;
}

.text {
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
