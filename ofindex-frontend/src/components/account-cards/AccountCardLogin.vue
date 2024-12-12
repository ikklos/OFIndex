<script setup>
import {ref} from 'vue'
const AccountId = ref('');
const Password = ref('');
</script>

<template>
  <div class="center-layout full-fix">
    <div>
      <el-row>
          <el-input v-model="AccountId" placeholder="请输入账号" class="input-bar"></el-input>
      </el-row>
      <el-row>
          <el-input v-model="Password" placeholder="请输入密码" class="input-bar"></el-input>
      </el-row>
    </div>
  </div>

</template>

<style scoped>
.input-bar {
  height: 36px;
}

</style>