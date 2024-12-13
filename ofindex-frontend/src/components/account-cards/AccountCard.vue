<script setup>
import AccountCardHeader from "@/components/account-cards/AccountCardHeader.vue";


</script>

<template>
  <div class="account-index">
    <div class="account-card" >
      <AccountCardHeader></AccountCardHeader>
      <transition name="el-fade-in">
        <RouterView></RouterView>
      </transition>
    </div>
  </div>
</template>

<style scoped>
.account-index {
  background-color: #292929;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.account-card {
  background-color: #000000;
  border-radius: 10px;
  border: 5px solid #AAAAAA;
  width: 25vw;
  height: 60vh;
}
</style>