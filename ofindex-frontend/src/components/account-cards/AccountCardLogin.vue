<script setup>
import {ref} from 'vue'
import {Right} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import axiosApp from "@/axiosApp.js";
import {ElMessage} from "element-plus";

const router = useRouter();
const AccountId = ref('');
const Password = ref('');
const gotoRegister = function () {
  router.push('/account/register');
}
//发送登录请求
const SendLoginReq = function () {
  if(AccountId.value === null || Password.value === null || Password.value.length === 0
      || AccountId.value.length === 0) {
    ElMessage.error('账号密码不能为空');
    return;
  }
  axiosApp().then(app=>{
    app.post('/login',{
      userid: Number(AccountId.value),
      passwd: Password.value,
    }).then(res => {
      if(res.status === 200 && res.data.message === 'Login success!'){
        localStorage.setItem('Token', res.data.token);
        router.push('/index');
      }else throw new Error('登录失败');
    }).catch(err => {
      //弹窗显示
      ElMessage.error(err.message);
    })
  })
}
</script>

<template>
  <div style="height: 80%; width:100%">
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
          <el-input v-model="Password" placeholder="请输入密码" class="input-bar"  type="password" show-password></el-input>
        </el-col>
      </el-row>
    </div>
    <div class="card-footer">
      <el-row>
        <el-col :span="8" class="center-layout-row">
          <el-button color="#3621ef" @click="gotoRegister">
            注册
          </el-button>
        </el-col>
        <el-col :span="8" :offset="8" class="center-layout-row">
          <el-button color="#3621ef" @click="SendLoginReq" :icon="Right">
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