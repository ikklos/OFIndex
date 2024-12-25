<script setup>

import {ref, onMounted} from 'vue'
import {DeleteFilled} from "@element-plus/icons-vue";

const MessageList = ref([]);

//从服务器获取用户的消息列表并装载进MessageList
const getMessages = function () {
  for (let i = 0; i < 20; i++) {
    MessageList.value.push({
      title: '消息',
      text: '这是一条消息',
      time: '2024/20/24'
    })
  }
}
//挂载到onMounted
onMounted(() => {
  getMessages();
})
</script>

<template>
  <el-container>
    <el-main class="message-page-main">
      <el-scrollbar class="half-width scroll-area">
        <div class="card-container" v-for="(message,index) in MessageList" :key="index">
          <el-card :body-style="{background:'#e3f2fd',
          minHeight: '100px',
          textAlign:'left'}"
          class="message-card">
            <template #header>
              <div style="text-align:left;">
                <span style="color:#000000;">{{ message.title }}</span>
              </div>
              <el-row style="justify-content: flex-end; justify-items: flex-end;">
                <el-button link style="color:#4825f6">
                  <el-icon><delete-filled/></el-icon>
                </el-button>
              </el-row>
            </template>
            <p style="color: #1a1a1a">{{ message.text }}</p>
            <template #footer>
              <div style="text-align:right;">
                <span style="color: #292929;">{{ message.time }}</span>
              </div>
            </template>
          </el-card>
        </div>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<style scoped>
.message-page-main {
  justify-items: center;
  height: calc(100vh - max(8vh, 60px));
}

.half-width {
  width: 50%;
}
.card-container{
  box-sizing: border-box;
  padding: 20px;
}
</style>