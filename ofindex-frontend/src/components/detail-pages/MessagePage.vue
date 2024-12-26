<script setup>

import {ref, onMounted} from 'vue'
import {Check, DeleteFilled} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import axiosApp from "@/axiosApp.js"

const MessageList = ref([]);
const router = useRouter();
//从服务器获取用户的消息列表并装载进MessageList
const getMessages = function () {
  axiosApp().then(app=>{
    app.get('/forum/message').then(response => {
      for(let i = 0; i < response.data.count; i++){
        MessageList.value.push({
          messageId: response.data.messages[i].messageId,
          title: response.data.messages[i].senderName,
          time: response.data.messages[i].addTime,
          text: response.data.messages[i].text,
          read: response.data.messages[i].read,
        })
      }
    }).catch(error => {
      ElMessage.error('获取消息失败');
      if(error.response.status === 601) {
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  })
}
const flagAsRead = (id,index)=>{
  axiosApp().then(app=>{
    app.get('/forum/message/read/'+id).then(response => {
      MessageList.value[index].read = true;
    }).catch(error => {
      ElMessage.error('操作失败');
      if(error.response.status === 601){
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  })
}
const deleteMessage = (id,index)=>{
  axiosApp().then(app=>{
    app.delete('/forum/message/'+id).then(response => {
      MessageList.value.splice(index,1);
    }).catch(error => {
      ElMessage.error('操作失败');
      if(error.response.status === 601){
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  })
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
                <el-button link style="color:#4825f6"
                           v-if="!message.read" @click="flagAsRead(message.messageId,index)">
                  <el-icon><Check/></el-icon>
                </el-button>
                <el-button link style="color:#4825f6" @click="deleteMessage(message.messageId,index)">
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