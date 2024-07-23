<script lang="ts" setup>
import {onMounted, ref} from 'vue'
import dayjs from 'dayjs'

const datetime = ref()
const week = ref()

onMounted(() => {
  setDatetimeWeek()
})

function setDatetimeWeek() {
  let datetimeWeek = dayjs().format('YYYY-MM-DD HH:mm:ss dddd')
  datetime.value = datetimeWeek.substring(0, 19)
  week.value = datetimeWeek.substring(19)
}

setInterval(() => {
  setDatetimeWeek()
}, 1000)
</script>

<template>
  <div class="datetimeWeek">
    <span class="datetime">{{ datetime }}</span>
    <span class="week">{{ week }}</span>
  </div>
</template>

<style scoped>
.datetimeWeek {
  width: 250px;
  height: 30px;
  border-radius: 3px;
  margin: 0 5px;
  border: 1px solid #CCC;
  padding: 5px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.datetime {
  font-size: 22px;
  line-height: 22px;
  font-family: Digital;
}

.week {
  font-size: 20px;
  line-height: 20px;
}
</style>
