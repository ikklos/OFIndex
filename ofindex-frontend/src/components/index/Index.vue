<script setup>
import {ref, computed, reactive, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import UserAccountDetailsMenu from "@/components/detail-pages/UserAccountDetailsMenu.vue";
import {CaretLeft, Plus} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import axios from "axios";
import axiosApp from "@/axiosApp.js";

const HelloText = ref('hello');
const route = useRoute();
const router = useRouter();
const RouteName = computed(() => route.name);
const userInfo = reactive({
  userAvatar: '',
  userName: '',
  IsAdmin: false,
})
const showBackButton = computed(() => {
  return (RouteName.value === 'r-index-book-detail' || RouteName.value === 'r-index-post-detail'
      || RouteName.value === 'r-index-messages');
});
//提供给Upload组件的响应数据
const uploadAvatarRawData = ref([]);

//表单控制
const nameFormRef = ref(null);
const nameFormRules = reactive({
  name: [{
    required: true,
    message: '名字不能为空',
    trigger: 'blur',
  }],
})

//控制账号设置目录折叠
const Fold = ref(true);
const Timeout = ref(null);

//控制对话框的出现消失
const showAvatarUploadPage = ref(false);
const showEditNamePage = ref(false);
const showChangePasswordPage = ref(false);
const avatarUploading = ref(false);
//修改用户信息的结构体
const editInfoData = reactive({
  name: '',
  avatar: '',
});
const resetPasswordFormData = reactive({
  oldPassword: '',
  newPassword: '',
  repeatNewPassword: '',
});
const confirmOldPass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入旧密码'));
  } else {
    callback();
  }
}
const confirmPass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入新密码'));
  } else {
    let allowed = /^[A-Za-z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]*$/;
    if (allowed.test(value)) {
      callback();
    } else {
      callback(new Error('新密码中含有非法字符（例如空格）'));
    }
  }
}
const confirmRepeatPass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请确认密码'));
  } else {
    if (value !== resetPasswordFormData.newPassword) {
      callback(new Error('两次输入密码不一样'));
    } else {
      callback();
    }
  }
}
const resetPassFormRules = reactive({
  oldPassword: [
    {
      validator: confirmOldPass,
      trigger: "blur",
    }
  ],
  newPassword: [
    {
      validator: confirmPass,
      trigger: "blur",
    }
  ],
  repeatNewPassword: [
    {
      validator: confirmRepeatPass,
      trigger: "blur",
    }
  ]
})
const resetPasswordForm = ref(null);
const handleResetPass = async (formEl) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      axiosApp().then(app => {
        app.get('/user').then(res => {
          app.post('/login', {
            userid: res.data.userId,
            passwd: resetPasswordFormData.oldPassword
          }).then(response => {
            if (response.status === 200) {
              app.post('/user/modify', {
                name: null,
                avatar: null,
                password: resetPasswordFormData.newPassword,
                phoneNum: null
              }).then(finRes => {
                if (finRes.status === 200) {
                  ElMessage.success('修改成功');
                  formEl.resetFields();
                  showChangePasswordPage.value = false;
                }
              }).catch(finError => {
                ElMessage.error('修改密码失败' + finError.message);
              })
            }
          }).catch(err => {
            ElMessage.error('旧密码验证失败');
          })
        }).catch(error => {
          ElMessage.error('登录信息过期:' + error.message);
          if (error.response.status === 601) {
            router.push('/account/login');
          }
        })
      })
    } else {
      throw new Error();
    }
  })
}
//上传头像
const handleUploadAvatarSuccess = function (response, uploadFile, uploadFiles) {
  if (response.data.success === true) {
    editInfoData.avatar = response.data.data.url;
  } else editInfoData.avatar = response.data.images;
  avatarUploading.value = false;
}
const handleUploadAvatarRemove = function (response, uploadFile) {
  editInfoData.avatar = '';
  avatarUploading.value = false;
}
const handleUploadAvatarError = function (error,uploadFile, uploadFiles) {
  avatarUploading.value = false;
}
const handleResetAvatar = function () {
  if (editInfoData.avatar === '') {
    ElMessage.error('头像还没上传呢');
  } else {
    axiosApp().then(app => {
      app.post('/user/modify', {
        name: null,
        avatar: editInfoData.avatar,
        password: null,
        phoneNum: null,
      }).then(res => {
        if (res.status === 200) {
          ElMessage.success('上传成功');
          userInfo.userAvatar = editInfoData.avatar;
          showAvatarUploadPage.value = false;
        }
      }).catch(error => {
        if (error.response.status === 601) {
          ElMessage('登录已过期');
          router.push('/account/login');
        }
      })
    })
  }
}
const uploadAvatar = function (options) {
  let data = {
    smfile: options,
    format: "json",
  }
  avatarUploading.value = true;
  axios.post('/api/upload', data,
      {headers: {"Content-Type": "multipart/form-data", "Authorization": 'WQe6xnSzY6sbQlH0YMmEdFIxTvx7PxzE'}}
  ).then(response => {
    if (response.status === 200) {
      if (response.data.success === true || response.data.code === 'image_repeated') {
        options.onSuccess(response);
      } else {
        throw new Error('上传失败，疑似图片太大或格式不符');
      }
    } else {
      throw new Error('请求错误，疑似网络问题');
    }
  }).catch(error => {
    options.onError(error);
    ElMessage.error(error.message);
  })
}
//折叠展开目录函数
const foldMenu = function () {
  Timeout.value = setTimeout(() => {
    Fold.value = true;
  }, 200)
}
const unfoldMenu = function () {
  if (Timeout.value) {
    clearTimeout(Timeout.value);
  }
  Fold.value = false;
}
//表单控制函数
const submitForm = async (formEl) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      //submit
      axiosApp().then(app=>{
        app.post('/user/modify', {
          name: editInfoData.name,
          avatar: null,
          password: null,
          phoneNum: null,
        }).then(res => {
          if (res.status === 200) {
            ElMessage.success('更改成功！');
            userInfo.userName = editInfoData.name;
            console.log(userInfo.userName);
            showEditNamePage.value = false;
            formEl.resetFields();
          }
        }).catch(err => {
          if (err.response.status === 601) {
            ElMessage.error('登录已过期');
            router.push('/account/login');
          } else {
            ElMessage.error('更改失败' + err.message);
          }
        })
      })
    } else {
      throw new Error();
    }
  })
}
const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
}
onMounted(() => {
  //获取用户信息
  axiosApp().then(app=>{
    app.get('/user').then(res => {
      userInfo.userName = res.data.userName;
      userInfo.userAvatar = (res.data.avatar === null) ? '' : res.data.avatar;
      userInfo.isAdmin = (res.data.level > 0);
    }).catch(error => {
      ElMessage.error('获取个人信息失败');
      if (error.response.status === 601) {
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  })
})
//跳转页面函数
const jumpToExplore = function () {
  router.push('/index/explore');
}
const jumpToShelf = function () {
  router.push('/index/shelf');
}
const jumpToForum = function () {
  router.push('/index/forum');
}
const jumpToUpload = function () {
  router.push('/index/upload-book');
}
const jumpBack = function () {
  router.back();
}
const jumpToMessagePage = function () {
  router.push('/index/detail/messages');
}
</script>

<template>
  <div class="index center-layout-column">
    <el-container id="index-container" class="index-container">
      <el-header>
        <el-row :gutter="10">
          <el-col :span="2">
            <div class="avatar-left full-fix">
              <el-avatar id="user-avatar" class="user-avatar" size="large" :class="{hovered:!Fold}"
                         @mouseover="unfoldMenu" @mouseleave="foldMenu" :src="userInfo.userAvatar" fit="cover">
              </el-avatar>
            </div>
            <transition name="el-fade-in-linear">
              <user-account-details-menu v-show="!Fold" :user-info="userInfo" @mouseover="unfoldMenu"
                                         @mouseleave="foldMenu"
                                         @change-avatar="()=>{showAvatarUploadPage = true}"
                                         @edit-name="()=>{showEditNamePage = true}"
                                         @show-message-page="jumpToMessagePage"
                                         @change-password="()=>{showChangePasswordPage = true}"/>
            </transition>
          </el-col>
          <el-col :span="4" :offset="8" style="font-size: 25px">
            <div class="text-center">{{ HelloText }}</div>
          </el-col>
          <el-col :span="2" :offset="2">
            <div class="button-area center-layout-row">
              <el-button class="shift-button" icon="Upload" type="primary" v-if="userInfo.isAdmin && (!showBackButton)"
                         :disabled="RouteName === 'r-index-upload-book'" @click="jumpToUpload" color="#3621ef">
                上传
              </el-button>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="button-area center-layout-row">
              <el-button class="shift-button" icon="Search" type="primary" :disabled="RouteName === 'r-index-explore'"
                         @click="jumpToExplore" v-if="!showBackButton" color="#3621ef">
                探索
              </el-button>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="button-area center-layout-row">
              <el-button class="shift-button" icon="Reading" type="primary" :disabled="RouteName === 'r-index-shelf'"
                         @click="jumpToShelf" v-if="!showBackButton" color="#3621ef">
                书架
              </el-button>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="button-area center-layout-row">
              <el-button class="shift-button" icon="CoffeeCup" type="primary" :disabled="RouteName === 'r-index-forum'"
                         @click="jumpToForum" v-if="!showBackButton" color="#3621ef">
                社区
              </el-button>
              <el-button class="back-button" link v-if="showBackButton" style="font-size: 25px; color:#f3f2fe"
                         @click="jumpBack">
                <el-icon>
                  <CaretLeft></CaretLeft>
                </el-icon>
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-header>
      <el-dialog v-model="showAvatarUploadPage" title="上传新头像" width="500" @closed="()=>{
        uploadAvatarRawData.splice(0,uploadAvatarRawData.length);
        editInfoData.avatar = '';
      }">
        <el-upload ref="uploadAvatarRef"
                   v-model:file-list="uploadAvatarRawData"
                   :limit="1"
                   :on-success="handleUploadAvatarSuccess"
                   :on-remove="handleUploadAvatarRemove"
                   :on-error="handleUploadAvatarError"
                   :on-exceed="()=>{ElMessage.warning('只能上传一张头像=(')}"
                   :http-request="uploadAvatar"
                   list-type="picture-card">
          <el-icon>
            <Plus/>
          </el-icon>
        </el-upload>
        <div style="padding: 10px 0 10px 0">
          <el-button v-loading.fullscreen.lock="avatarUploading" icon="Upload" color="#3621ef"
                     @click="handleResetAvatar">更改头像
          </el-button>
        </div>
      </el-dialog>
      <el-dialog v-model="showEditNamePage" title="修改昵称" width="500" @closed="()=>{
        resetForm(nameFormRef);
      }">
        <el-form ref="nameFormRef" :model="editInfoData" :rules="nameFormRules" @submit.native.prevent>
          <el-form-item label="新昵称" prop="name">
            <el-input v-model="editInfoData.name" placeholder="请输入新昵称" @keyup.enter="()=>{}"></el-input>
          </el-form-item>
          <el-button color="#3621ef" @click="submitForm(nameFormRef)">修改昵称</el-button>
        </el-form>
      </el-dialog>
      <el-dialog v-model="showChangePasswordPage" title="修改密码" width="600">
        <el-form ref="resetPasswordForm" :model="resetPasswordFormData" :rules="resetPassFormRules">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input placeholder="请输入旧密码" v-model="resetPasswordFormData.oldPassword" type="password"
                      show-password></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input placeholder="请输入新密码" v-model="resetPasswordFormData.newPassword" type="password"
                      show-password></el-input>
          </el-form-item>
          <el-form-item label="再次输入新密码" prop="repeatNewPassword">
            <el-input placeholder="确认新密码" v-model="resetPasswordFormData.repeatNewPassword" type="password"
                      show-password></el-input>
          </el-form-item>
          <el-button @click="handleResetPass(resetPasswordForm)" class="3621ef">更改密码</el-button>
        </el-form>
      </el-dialog>
      <RouterView></RouterView>
    </el-container>
  </div>
</template>

<style scoped>

/*用户头像*/
.user-avatar {
  aspect-ratio: auto 1 / 1;
  transition: all 0.3s ease;
  z-index: 100;
  width: 60px;
  height: 60px;
}

.user-avatar.hovered { /*悬停动画效果*/
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影 */
  transform: scale(1.1, 1.1) translateY(10px) translateX(10px);
  cursor: pointer;
  transition: all 0.3s ease;
}

.avatar-left {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  text-align: center;
  flex-direction: column;
}

/*主页外框*/
.index {
  width: 100vw;
  height: 100vh;
  min-width: 800px;
  min-height: 600px;
}

.index-container {
  width: 100vw;
  height: 100%;
  min-width: 720px;
  background: #e7e5fd;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.el-header {
  box-sizing: border-box;
  height: 8vh;
  position: sticky;
  min-height: 60px;
  z-index: 11;
  background-color: #06041c;
  color: #f3f2fe;
}

.el-col {
  height: 100%;
}

.el-row {
  height: 100%;
}

.text-center {
  height: 8vh;
  text-align: center;
  line-height: 8vh;
}

/*按钮样式*/
.button-area {
  height: 100%;
  width: 100%;
}

.shift-button {
  width: 5vw;
  height: 4vh;
}
</style>