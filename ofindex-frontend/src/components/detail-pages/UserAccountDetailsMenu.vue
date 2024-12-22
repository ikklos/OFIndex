<script setup>
import {ref, onMounted} from 'vue'
const name = ref('');
onMounted(() => {
  name.value = 'ikklo';
});
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
</script>

<template>
  <div class="menu-container" id="avatar-menu-container">
    <el-button icon="EditPen" link :style="calcPenPos()" class="edit-pen"></el-button>
    <el-row style="height:50px" class="name-row">
      <el-col style="font-size:30px; border: 1px solid rgba(0,0,0,0.2); border-radius: 30px" :span="20" :offset="2">{{name}}</el-col>
    </el-row>
    <el-button icon="Message" link class="message-button"></el-button>
  </div>
</template>

<style scoped>
.menu-container {
  position: fixed;
  top: 0;
  left: 5vw;
  width: 20vw;
  height: 50vh;
  background: #fffbf0;
  min-width: 300px;
  min-height: 400px;
  z-index: 99;
  box-sizing: border-box;
  border: 1px solid #edd1d8;
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
.message-button{
  position: absolute;
  top: calc(100% - 50px);
  left: calc(100% - 50px);
  font-size: 30px;
}
</style>