<script setup>
import {ref, onMounted, watch} from 'vue'
const name = ref('');
const props = defineProps({
  userInfo: {
    type: Object,
  }
})
onMounted(() => {
  name.value = JSON.parse(JSON.stringify(props.userInfo.userName));
});
watch(props.userInfo,(newVal, oldVal)=>{
  name.value = JSON.parse(JSON.stringify(props.userInfo.userName));
})
const calcPenPos = function(){
  if(!document.getElementById('index-container')){
    return {};
  }
  let rect = document.getElementById('index-container').getBoundingClientRect();
  return {
    left: `${rect.left + 100}px`,
    top: `${rect.top + 50}px`,
  };
}
const emit = defineEmits(['changeAvatar','editName','showMessagePage','changePassword'])
</script>

<template>
  <div class="menu-container" id="avatar-menu-container">
    <el-button icon="EditPen" link :style="calcPenPos()" class="edit-pen" @click="()=>{emit('changeAvatar')}" ></el-button>
    <div style="height:100px" class="name-row">
      <el-row style="color: #1a1a1a; font-size:30px; border: 1px solid rgba(0,0,0,0.2);
      border-radius: 30px; align-items: center; justify-content: center; justify-items: center"
              class="name-area" @click="()=>{emit('editName')}">{{name}}</el-row>
      <el-row style="padding: 10px 0 10px 0;align-items: center; justify-content: center; justify-items: center">
        <el-button color="#3621ef" @click="emit('changePassword')" class="reset-pass-button">修改密码</el-button>
      </el-row>
    </div>
    <el-button icon="Message" link class="message-button" @click="()=>{emit('showMessagePage')}"></el-button>
  </div>
</template>

<style scoped>
.menu-container {
  position: fixed;
  top: 0;
  left: 5vw;
  width: 20vw;
  height: 50vh;
  background: #ffffff;
  min-width: 300px;
  min-height: 400px;
  z-index: 99;
  box-sizing: border-box;
  border: 1px solid #ffffff;
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}
.edit-pen {
  font-size:30px;
  position: fixed;
}
.name-row{
  position: relative;
  top: 90px;
}
.name-area:hover{
  cursor: pointer;
}
.message-button{
  position: absolute;
  top: calc(100% - 50px);
  left: calc(100% - 50px);
  font-size: 30px;
}
</style>