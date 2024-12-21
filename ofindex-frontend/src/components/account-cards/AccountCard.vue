<script setup>
import AccountCardHeader from "@/components/account-cards/AccountCardHeader.vue";
import {useRoute} from "vue-router";
const route = useRoute();
</script>

<template>
  <div class="account-index">
    <div class="account-card" >
      <AccountCardHeader></AccountCardHeader>
      <router-view v-slot="{ Component}">
        <transition name="el-fade-in-linear">
          <component :is="Component"/>
        </transition>
      </router-view>
    </div>
  </div>
</template>

<style scoped>
.account-index {
  background-color: #fcfce5;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 600px;
  min-width: 800px;
}
.account-card {
  background-color: #eee6fe;
  border-radius: 15px;
  border: 1px solid #3621ef;
  width: 23vw;
  height: 60vh;
  min-width: 360px;
  min-height: 480px;
  overflow: hidden;
  box-shadow: 4px 4px 8px rgba(0, 0, 0, 0.2);
}

</style>