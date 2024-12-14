<script setup>

import {Avatar, CoffeeCup, Reading, Search, Upload} from "@element-plus/icons-vue";
import {ref} from 'vue'
import {useRoute} from 'vue-router'
const HelloText = ref('hello');
const route = useRoute();
const RouteName = route.name;
const IsAdmin = ref(false)
</script>

<template>
  <div class="index center-layout-column">
    <el-container>
      <el-header height="8vh">
        <el-row :gutter="10">
          <el-col :span="2">
            <div class="avatar-left full-fix">
              <el-avatar size="large"></el-avatar>
            </div>
          </el-col>
          <el-col :span="4" :offset="8">
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
              <el-button icon="Search" type="primary" :disabled="RouteName === 'r-index-explore'">
                探索
              </el-button>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="button-area center-layout-row">
              <el-button icon="Reading" type="primary">
                书架
              </el-button>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="button-area center-layout-row">
              <el-button icon="CoffeeCup" type="primary">
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
.avatar-left{
  display: flex;
  justify-content: center;
  align-items: flex-start;
  text-align: center;
  flex-direction: column;
}
.text-center{
  height: 8vh;
  text-align: center;
  line-height: 8vh;
}
.button-area{
  height: 100%;
  width: 100%;
}
.el-button{
  width: 5vw;
  height: 4vh;
}
</style>