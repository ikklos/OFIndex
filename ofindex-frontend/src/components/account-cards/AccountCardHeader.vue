<script setup>
import {ref,watch,onMounted} from 'vue'
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
  }
}
onMounted(() => {SetHeaderInfo(route.name);});
watch(()=>route.name,
    (newVal, oldVal) => {
        SetHeaderInfo(newVal);
    })
</script>

<template>
  <el-row style="align-items: center;justify-items: center;justify-content: center; font-size: 30px; color: #000000;">
    {{headString}}
  </el-row>
</template>

<style scoped>
.el-button{
  text-align: center;
  font-size: 16px;
  color: #3621ef;
}
.el-col{
  text-align: center;
  color: #000000;
}
.el-row{
  height: 20%;
}
</style>