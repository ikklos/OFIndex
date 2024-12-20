<script setup>
import {ref,computed} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import UserAccountDetailsMenu from "@/components/index/UserAccountDetailsMenu.vue";
import {CaretLeft} from "@element-plus/icons-vue";

const HelloText = ref('hello');
const route = useRoute();
const router = useRouter();
const RouteName = computed(()=>route.name);
const IsAdmin = ref(false);
const showBackButton = computed(()=>{
  return (RouteName.value === 'r-index-book-detail' || RouteName.value === 'r-index-post-detail');
});
//控制账号设置目录折叠
const Fold = ref(true);
const Timeout = ref(null);

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
const jumpBack = function () {
  router.back();
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
                         @mouseover="unfoldMenu" @mouseleave="foldMenu">
              </el-avatar>
            </div>
            <transition name="el-fade-in-linear">
              <user-account-details-menu v-if="!Fold" @mouseover="unfoldMenu" @mouseleave="foldMenu" class=""/>
            </transition>
          </el-col>
          <el-col :span="4" :offset="8" style="font-size: 25px">
            <div class="text-center">{{HelloText}}</div>
          </el-col>
          <el-col :span="2" :offset="2">
            <div class="button-area center-layout-row">
              <el-button class="shift-button" icon="Upload" type="primary" v-if="IsAdmin && (!showBackButton)">
                上传
              </el-button>
            </div>
          </el-col>
          <el-col :span="2" >
            <div class="button-area center-layout-row">
              <el-button class="shift-button" icon="Search" type="primary" :disabled="RouteName === 'r-index-explore'" @click="jumpToExplore" v-if="!showBackButton">
                探索
              </el-button>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="button-area center-layout-row">
              <el-button class="shift-button" icon="Reading" type="primary" :disabled="RouteName === 'r-index-shelf'" @click="jumpToShelf" v-if="!showBackButton">
                书架
              </el-button>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="button-area center-layout-row">
              <el-button class="shift-button" icon="CoffeeCup" type="primary" :disabled="RouteName === 'r-index-forum'" @click="jumpToForum" v-if="!showBackButton">
                社区
              </el-button>
              <el-button class="back-button" link v-if="showBackButton" style="font-size: 25px" @click="jumpBack">
                <el-icon>
                  <CaretLeft></CaretLeft>
                </el-icon>
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
  background: #f0fcff;
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
  border-top: 5px solid #0099cc;
  height: 8vh;
  position: sticky;
  min-height: 60px;
  z-index: 11;
  background-color: #e9e7ef;
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