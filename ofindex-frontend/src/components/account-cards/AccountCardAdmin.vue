<script setup>
import {ref} from 'vue'
import {Back, Right} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";

const router = useRouter();
const AccountId = ref(''); //账号
const Password = ref(''); //密码
const goBack = function () {
  router.back();
}
//发送登录请求
const SendLoginReq = function () {
  //如果成功，跳转到主页
  router.push('/index');
}

</script>

<template>
  <div style="height: 80%; width:100%">
    <!--账号密码输入区域-->
    <div class="card-body center-layout-column">
      <el-row>
        <el-col :span="6" class="text-right">
          账号：
        </el-col>
        <el-col :span="18">
          <el-input v-model="AccountId" placeholder="请输入账号" class="input-bar"></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="6" class="text-right">
          密码：
        </el-col>
        <el-col :span="18">
          <el-input v-model="Password" placeholder="请输入密码" class="input-bar" type="password" show-password></el-input>
        </el-col>
      </el-row>
    </div>
    <div class="card-footer">
      <el-row>
        <el-col :span="8" class="center-layout-row">
          <!--点击返回上一页-->
          <el-button color="#80aaff" @click="goBack" :icon="Back">
          </el-button>
        </el-col>
        <el-col :span="8" :offset="8" class="center-layout-row">
          <!--点击发送登录请求-->
          <el-button color="#80aaff" @click="SendLoginReq" :icon="Right">
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
  color:#FFFFFF;
}
.full-fix {
  width: 100%;
  height: 100%;
}
.input-bar{
  width: 16vw;
  height: 5vh;
}
.el-button{
  width: 6vw;
  height: 4vh;
  font-size: 20px;
}
:deep() .el-input__inner {
  color: #FFFFFF;
}
:deep() .el-input__wrapper {
  background-color: #000000;
}
.card-body {
  height: 40vh;
}
</style>