<script setup>
import {ref} from 'vue'
import {Back, Right} from "@element-plus/icons-vue";
import {useRoute,useRouter} from "vue-router";

const router = useRouter();
const route = useRoute();
const AccountId = ref("");
AccountId.value = String(route.params.AccountId);
const goBack = function () {
  router.back();
}
const gotoLogin = function () {
  router.push('/account/login');
}
</script>

<template>
  <div style="height: 80%; width:100%">
    <div class="card-body center-layout-column">
      <el-row style="align-items: center; justify-content: center; font-size: 24px">
        你的账号是：
      </el-row>
      <el-row style="align-items: center; justify-content: center;">
          <el-input v-model="AccountId" placeholder="给它删了干什么玩意" class="input-bar"></el-input>
      </el-row>
    </div>
    <div class="card-footer">
      <el-row>
        <el-col :span="8" class="center-layout-row">
          <el-button color="#3621ef" @click="goBack" :icon="Back">
          </el-button>
        </el-col>
        <el-col :span="8" :offset="8" class="center-layout-row">
          <el-button color="#3621ef" @click="gotoLogin" :icon="Right">
          </el-button>
        </el-col>
      </el-row>
    </div>
  </div>

</template>

<style scoped>
.text-right{
  text-align: right;
  line-height: 5vh;
}
.el-row {
  width: 100%;
  margin-bottom: 50px;
  color:#292929;
  min-height: 50px
}
.full-fix {
  width: 100%;
  height: 100%;
}
.input-bar{
  width: 16vw;
  height: 5vh;
  min-width: 200px;
  min-height: 30px;

}
.el-button{
  width: 6vw;
  height: 4vh;
  font-size: 20px;
  min-width: 80px;
  min-height: 40px;
}
:deep() .el-input__inner {
  font-size: 20px;
  color: #000000;
}
:deep() .el-input__wrapper {
  background-color: #d1c2fb;
}
.card-body {
  height: 40vh;
  min-height: 250px;
}
.text-right{
  color: #000000;
  font-size: 20px;
}
</style>