<script setup>
import {ref, computed, reactive} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import UserAccountDetailsMenu from "@/components/detail-pages/UserAccountDetailsMenu.vue";
import {CaretLeft, Plus} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import axios from "axios";

const HelloText = ref('hello');
const route = useRoute();
const router = useRouter();
const RouteName = computed(()=>route.name);
const userAvatar = ref('https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg');
const IsAdmin = ref(true);
const showBackButton = computed(()=>{
  return (RouteName.value === 'r-index-book-detail' || RouteName.value === 'r-index-post-detail'
  || RouteName.value === 'r-index-messages');
});
//提供给Upload组件的响应数据
const uploadAvatarRawData = ref([]);

//表单控制
const nameFormRef = ref(null);
const nameFormRules = reactive({
  name:{
    required: true,
    message: '名字不能为空',
    trigger: 'blur',
  },
})

//控制账号设置目录折叠
const Fold = ref(true);
const Timeout = ref(null);

//控制对话框的出现消失
const showAvatarUploadPage = ref(false);
const showEditNamePage = ref(false);

//修改用户信息的结构体
const editInfoData = reactive({
  name:'',
  avatar:'',
  password:'',
});

//上传头像
const handleUploadAvatarSuccess = function (response,uploadFile,uploadFiles) {

}
const handleUploadAvatarRemove = function (response,uploadFile) {

}
const uploadAvatar = function (options) {
  let data = {
    smfile:options,
    format: "json",
  }
  axios.post('/api/upload',data,
      {headers:{"Content-Type":"multipart/form-data", "Authorization": 'WQe6xnSzY6sbQlH0YMmEdFIxTvx7PxzE'}}
  ).then(response => {
    if(response.status === 200){
      if(response.data.success === true || response.data.code === 'image_repeated'){

      }else{
        throw new Error('上传失败，疑似图片太大或格式不符');
      }
    }else{
      throw new Error('请求错误，疑似网络问题');
    }
  }).catch(error => {

  })
}
//折叠展开目录函数
const foldMenu = function () {
  Timeout.value = setTimeout(() => {
    Fold.value = true;
  }, 200)
}
const unfoldMenu = function () {
  if(Timeout.value){
    clearTimeout(Timeout.value);
  }
  Fold.value = false;
}
//表单控制函数
const submitForm = async (formEl) => {
  if(!formEl)return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      //submit
    } else {
      throw new Error();
    }
  })
}
const resetForm = (formEl) => {
  if(!formEl)return
  formEl.resetFields()
}
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
              <el-avatar id="user-avatar" class="user-avatar" size="large"  :class="{hovered:!Fold}"
                         @mouseover="unfoldMenu" @mouseleave="foldMenu" :src="userAvatar" fit="cover">
              </el-avatar>
            </div>
            <transition name="el-fade-in-linear">
              <user-account-details-menu v-if="!Fold" @mouseover="unfoldMenu" @mouseleave="foldMenu"
                                         @change-avatar="()=>{showAvatarUploadPage = true}"
                                         @edit-name="()=>{showEditNamePage = true}"
                                         @show-message-page="jumpToMessagePage"/>
            </transition>
          </el-col>
          <el-col :span="4" :offset="8" style="font-size: 25px">
            <div class="text-center">{{HelloText}}</div>
          </el-col>
          <el-col :span="2" :offset="2">
            <div class="button-area center-layout-row">
              <el-button class="shift-button" icon="Upload" type="primary" v-if="IsAdmin && (!showBackButton)"
                         :disabled="RouteName === 'r-index-upload-book'" @click="jumpToUpload" color="#3621ef">
                上传
              </el-button>
            </div>
          </el-col>
          <el-col :span="2" >
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
              <el-button class="back-button" link v-if="showBackButton" style="font-size: 25px; color:#3621ef" @click="jumpBack">
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
      }">
        <el-upload ref="uploadAvatarRef"
        v-model:file-list="uploadAvatarRawData"
        :limit="1"
        :on-success="handleUploadAvatarSuccess"
        :on-remove="handleUploadAvatarRemove"
        :on-exceed="()=>{ElMessage.warning('只能上传一张头像=(')}"
        :http-request="uploadAvatar"
        list-type="picture-card">
          <el-icon><Plus/></el-icon>
        </el-upload>
        <div style="padding: 10px 0 10px 0">
          <el-button icon="Upload" color="#3621ef">更改头像</el-button>
        </div>
      </el-dialog>
      <el-dialog v-model="showEditNamePage" title="修改昵称" width="500" @closed="()=>{
        resetForm(nameFormRef);
      }">
        <el-form ref="nameFormRef" :model="editInfoData" :rules="nameFormRules">
          <el-form-item label="新昵称" prop="name">
            <el-input v-model="editInfoData.name"></el-input>
          </el-form-item>
          <el-button color="#3621ef" @click="submitForm(nameFormRef)">修改昵称</el-button>
        </el-form>
      </el-dialog>
      <RouterView></RouterView>
    </el-container>
  </div>
</template>

<style scoped>

/*用户头像*/
.user-avatar{
  aspect-ratio: auto 1 / 1;
  transition: all 0.3s ease;
  z-index: 100;
  width: 60px;
  height: 60px;
}
.user-avatar.hovered{ /*悬停动画效果*/
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影 */
  transform: scale(1.1,1.1) translateY(10px) translateX(10px);
  cursor: pointer;
  transition: all 0.3s ease;
}
.avatar-left{
  display: flex;
  justify-content: center;
  align-items: flex-start;
  text-align: center;
  flex-direction: column;
}
/*主页外框*/
.index{
  width: 100vw;
  height: 100vh;
  background: #eee6fe;
  min-width: 800px;
  min-height: 600px;
}
.index-container {
  width: 90vw;
  height: 100%;
  min-width: 720px;
  background: #FFFFFF;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
.el-header{
  box-sizing: border-box;
  border-top: 5px solid #6f4bf8;
  height: 8vh;
  position: sticky;
  min-height: 60px;
  z-index: 11;
  background-color: #d1c2fb;
  color: #0219e7;
}
.el-col{
  height: 100%;
}
.el-row{
  height: 100%;
}

.text-center{
  height: 8vh;
  text-align: center;
  line-height: 8vh;
}

/*按钮样式*/
.button-area{
  height: 100%;
  width: 100%;
}
.shift-button{
  width: 5vw;
  height: 4vh;
}
</style>