<script setup>
import {ref} from 'vue'
import {Back, Right} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import axiosApp from "@/main.js";

const router = useRouter();
const NickName = ref('');
const Password = ref('');
const ConfirmPassword = ref('');
const goBack = function () {
  router.back();
}
const sendRegisterReq = function () {
  //判断两次输入的密码是否相同
  if (Password.value !== ConfirmPassword.value || (Password.value.length < 8)) {
    //弹窗提示两次输入密码不相同或者密码太短

    //清空输入框
    Password.value = ConfirmPassword.value = '';
  } else {
    //判断密码是否合规
    let allowed = /^[A-Za-z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]*$/;
    if (allowed.test(Password.value)) {
      //发送请求
      axiosApp.post('/register',{
        username: NickName.value,
        phoneNumber:null,
        passwd: Password.value,
      }).then(response => {
        if (response.status === 200) {
          localStorage.setItem('Token',response.data.token);
          const AccountId = response.data.id;
          console.log(response.data);
          //跳转到成功页
          router.push({
            name: 'r-account-card-register-succeed',
            params: {
              AccountId: AccountId,
            }
          });
        }else throw new Error(response.statusText);
      }).catch(error => {
          //弹窗提示注册失败
        console.log(error);
      })
    } else {
      //弹窗提示密码含有非法字符
    }
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
          <el-input v-model="Password" placeholder="请输入密码" class="input-bar" type="password"
                    show-password></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="6" class="text-right">
          确认密码：
        </el-col>
        <el-col :span="18">
          <el-input v-model="ConfirmPassword" placeholder="请再次输入密码" class="input-bar" type="password"
                    show-password></el-input>
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
.text-right {
  text-align: right;
  line-height: 5vh;
}

.el-row {
  width: 100%;
  margin-bottom: 50px;
  color: #FFFFFF;
  min-height: 30px
}

.full-fix {
  width: 100%;
  height: 100%;
}

.input-bar {
  width: 16vw;
  height: 5vh;
  min-width: 200px;
  min-height: 30px;
}

.el-button {
  width: 6vw;
  height: 4vh;
  font-size: 20px;
  min-width: 80px;
  min-height: 40px;
}

:deep() .el-input__inner {
  color: #FFFFFF;
}

:deep() .el-input__wrapper {
  background-color: #000000;
}

.card-body {
  height: 40vh;
  min-height: 250px;
}
</style>