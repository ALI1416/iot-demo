<script setup lang="ts">
import HelloWorld from './components/HelloWorld.vue'
import {Check, Delete, Edit, Message, Search, Star} from '@element-plus/icons-vue'
</script>

<template>
  <HelloWorld msg="Vite + Vue"/>
  <el-row class="mb-4">
    <el-button plain>Plain</el-button>
    <el-button type="primary" plain>Primary</el-button>
    <el-button type="success" plain>Success</el-button>
    <el-button type="info" plain>Info</el-button>
    <el-button type="warning" plain>Warning</el-button>
    <el-button type="danger" plain>Danger</el-button>
  </el-row>
  <el-row>
    <el-button :icon="Search" circle/>
    <el-button type="primary" :icon="Edit" circle/>
    <el-button type="success" :icon="Check" circle/>
    <el-button type="info" :icon="Message" circle/>
    <el-button type="warning" :icon="Star" circle/>
    <el-button type="danger" :icon="Delete" circle/>
  </el-row>
</template>

<style scoped>

</style>
