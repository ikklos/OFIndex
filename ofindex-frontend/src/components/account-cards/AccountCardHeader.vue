<script setup>
import {ref,watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'

const router = useRouter();
const route = useRoute();
const sideButtonVisible = ref(route.name === 'r-account-card-login');
const headString = ref('登录');
sideButtonVisible.value = (route.name === 'r-account-card-login');
const SetHeaderInfo = function (nowRoute){
  sideButtonVisible.value = (nowRoute === 'r-account-card-login');
  switch(nowRoute) {
    case 'r-account-card-login':
      headString.value = '登录';
      break;
    case 'r-account-card-register':
      headString.value = "注册";
      break;
    case 'r-account-card-admin':
      headString.value = "管理员";
  }
}
SetHeaderInfo(route.name);

watch(()=>route.name,
    (newVal, oldVal) => {
        SetHeaderInfo(newVal);
    })
//跳转到手机登录页面
const gotoPhoneLogin = function () {
  router.push('/account/phone-login');
}
//跳转到管理员登录页面
const gotoAdmin = function () {
  router.push('/account/admin');
}
</script>

<template>
  <el-row :span="24" class="center-layout-row">
    <el-col :span="5" v-show="sideButtonVisible">
      <el-button link type="primary" @click="gotoPhoneLogin">手机号登录</el-button>
    </el-col>
    <el-col :span="14" style="font-size: 48px">{{headString}}</el-col>
    <el-col :span="5" v-show="sideButtonVisible">
      <el-button link type="primary" @click="gotoAdmin">管理员入口</el-button>
    </el-col>
  </el-row>
</template>

<style scoped>
.el-button{
  text-align: center;
  font-size: 16px;
  color: #80aaff
}
.el-col{
  text-align: center;
  color: #FFFFFF
}
.el-row{
  height: 20%;
}
</style>