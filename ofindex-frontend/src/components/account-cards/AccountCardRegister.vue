<script setup>
import {ref} from 'vue'
import {Back, Right} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";

const router = useRouter();
const NickName = ref('');
const Password = ref('');
const ConfirmPassword = ref('');
const goBack = function () {
  router.back();
}
const sendRegisterReq = function () {
  //判断两次输入的密码是否相同
  if(Password.value !== ConfirmPassword.value) {
      //弹窗
      //清空输入框
      Password.value = ConfirmPassword.value = '';
  }
  else{
    //发送请求
    let AccountId = '123123';
    let token = 'ABCSDSJADKLJASJDKLSAJK';
    localStorage.setItem('Token', token);
    //跳转到成功页
    router.push({
      name: 'r-account-card-register-succeed',
      params:{
        AccountId:AccountId,
      }
    });
  }
}
</script>

<template>
  <div style="height: 80%; width:100%">
    <div class="card-body center-layout-column">
      <el-row>
        <el-col :span="6" class="text-right">
          昵称：
        </el-col>
        <el-col :span="18">
          <el-input v-model="NickName" placeholder="请输入昵称" class="input-bar"></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="6" class="text-right">
          密码：
        </el-col>
        <el-col :span="18">
          <el-input v-model="Password" placeholder="请输入密码" class="input-bar"  type="password" show-password></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="6" class="text-right">
          确认密码：
        </el-col>
        <el-col :span="18">
          <el-input v-model="ConfirmPassword" placeholder="请再次输入密码" class="input-bar"  type="password" show-password></el-input>
        </el-col>
      </el-row>
    </div>
    <div class="card-footer">
      <el-row>
        <el-col :span="8" class="center-layout-row">
          <el-button color="#80aaff" @click="goBack" :icon="Back">
          </el-button>
        </el-col>
        <el-col :span="8" :offset="8" class="center-layout-row">
          <el-button color="#80aaff" @click="sendRegisterReq" :icon="Right">
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