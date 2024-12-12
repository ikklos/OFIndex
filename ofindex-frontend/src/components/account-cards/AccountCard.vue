<script setup>
import AccountCardHeader from "@/components/account-cards/AccountCardHeader.vue";


</script>

<template>
  <div class="account-index">
    <el-row style="width: 100%">
      <el-col :span="8" :offset="8">
        <el-card class="account-card">
          <template v-slot:header>
            <AccountCardHeader></AccountCardHeader>
          </template>
          <RouterView></RouterView>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.account-index {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.account-card {
  width: 100%;
  height: 650px;
}
</style>