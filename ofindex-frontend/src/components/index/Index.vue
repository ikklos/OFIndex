<script setup>

import {Avatar, CoffeeCup, Reading, Search, Upload} from "@element-plus/icons-vue";
import {ref,computed} from 'vue'
import {useRoute, useRouter} from 'vue-router'
const HelloText = ref('hello');
const route = useRoute();
const router = useRouter();
const RouteName = computed(()=>route.name);
const IsAdmin = ref(false);
//跳转页面函数
const jumpToUserAccountIndex = function () {
  router.push('/index/user-account-index');
}
const jumpToExplore = function () {
  router.push('/index/explore');
}
const jumpToShelf = function () {
  router.push('/index/shelf');
}
const jumpToForum = function () {
  router.push('/index/forum');
}
</script>

<template>
  <div class="index center-layout-column">
    <el-container>
      <el-header height="8vh">
        <el-row :gutter="10">
          <el-col :span="2">
            <div class="avatar-left full-fix">
              <el-avatar class="user-avatar" size="large" @click="jumpToUserAccountIndex"></el-avatar>
            </div>
          </el-col>
          <el-col :span="4" :offset="8" style="font-size: 25px">
            <div class="text-center">{{HelloText}}</div>
          </el-col>
          <el-col :span="2" :offset="2">
            <div class="button-area center-layout-row">
              <el-button icon="Upload" type="primary" v-if="IsAdmin.value">
                上传
              </el-button>
            </div>
          </el-col>
          <el-col :span="2" >
            <div class="button-area center-layout-row">
              <el-button icon="Search" type="primary" :disabled="RouteName === 'r-index-explore'" @click="jumpToExplore">
                探索
              </el-button>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="button-area center-layout-row">
              <el-button icon="Reading" type="primary" :disabled="RouteName === 'r-index-shelf'" @click="jumpToShelf">
                书架
              </el-button>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="button-area center-layout-row">
              <el-button icon="CoffeeCup" type="primary" :disabled="RouteName === 'r-index-forum'" @click="jumpToForum">
                社区
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-header>
      <RouterView></RouterView>
    </el-container>
  </div>
</template>

<style scoped>
/*用户头像*/
.user-avatar{
  transition: all 0.3s ease;
}
.user-avatar:hover{ /*悬停动画效果*/
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影 */
  transform: scale(1.1,1.1);
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
  background-image:url('https://i.loli.net/2021/04/07/QgHvB7hlJyCnN8s.jpg');
}
.el-container {
  width: 90vw;
  background: #FFFFFF;
}
.el-header{
  position: sticky;
  background-color: #AAAAAA;
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
.el-button{
  width: 5vw;
  height: 4vh;
}
</style>