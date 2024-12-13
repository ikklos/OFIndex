<script setup>

import {Avatar} from "@element-plus/icons-vue";
</script>

<template>
  <div class="index center-layout-column">
    <el-container>
      <el-header>
        <el-row :gutter=25>
          <el-col :span="2">
            <div class="avatar-left full-fix">
              <el-avatar size="large"></el-avatar>
            </div>
          </el-col>
          <el-col :span="1" :offset="18"></el-col>
          <el-col :span="1"></el-col>
          <el-col :span="1"></el-col>
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
  background-color: #AAAAAA;
  height:8vh;
}
.el-row{
  height: 100%;
}
.el-col{
  height: 100%;
}
.avatar-left{
  display: flex;
  justify-content: center;
  align-items: flex-start;
  text-align: center;
  flex-direction: column;
}
</style>