<script setup>
import {ref,watch} from 'vue'
import {useRoute} from 'vue-router'

const route = useRoute();
const sideButtonVisible = ref(route.name === 'r-account-card-login');
const headString = ref(route.name);

watch(()=>route.name,
    (newVal, oldVal) => {
        sideButtonVisible.value = (newVal === 'r-account-card-login');
        headString.value = newVal;
    })
</script>

<template>
  <el-row :span="24" class="center-layout">
    <el-col :span="5" v-show="sideButtonVisible">
      <el-button link type="primary">手机号登录</el-button>
    </el-col>
    <el-col :span="14">{{headString}}</el-col>
    <el-col :span="5" v-show="sideButtonVisible">
      <el-button link type="primary">管理员入口</el-button>
    </el-col>
  </el-row>
</template>

<style scoped>

</style>