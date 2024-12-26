<script setup>

import LittleBalls from "@/components/index/LittleBalls.vue";
import {ref, onMounted, onBeforeUnmount, reactive} from 'vue';
import {useRouter} from 'vue-router'
import {Picture} from "@element-plus/icons-vue";
import axiosApp from "@/main.js";
import {ElMessage} from "element-plus";

let router = useRouter();
const menuItems = ref([
  {name: "历史"}, {name: "书架"}, {name: "回退"},{name: '创建书单'}
]);
const DataList = ref([{
  books:[]
}]);
const nowBookList = ref(0);
const formRef = ref(null);
const formRules = reactive({
  name:{
    required: true,
    message: '名称不能为空',
    trigger: 'blur'
  }
});
const formData = reactive({
  name: ''
});
const dialogVisible = ref(false);
const jumpToDetail = function(bookId) {
  router.push('/index/detail/book-detail/'+bookId);
}
const jumpToBookList = function(index) {
  nowBookList.value = index;
}
const handleLittleBallsClicked = function (index){
  if(index === 0){
    loadHistory();
  }
  if(index === 1){
    reloadData();
  }
  if(index === 2){
    nowBookList.value = 0;
  }
  if(index === 3){
    dialogVisible.value = true;
  }
}
const submitForm = async (formEl) => {
  if(!formEl)return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      //submit
    } else {
      throw new Error();
    }
  })
}
const reloadData = ()=>{
  DataList.value.splice(0,DataList.value.length);
  axiosApp.get('/shelf').then(res => {
    nowBookList.value = 0;
    DataList.value = JSON.parse(JSON.stringify(res.data.items));
  }).catch(err => {
    ElMessage.error('获取书架信息失败');
    if(err.response.status === 601){
      ElMessage.error('登录信息已过期');
      router.push('/account/login');
    }
  })
}
const loadHistory = ()=>{
  DataList.value.splice(0,DataList.value.length);
  axiosApp.get('/shelf/history').then(res=>{
    nowBookList.value = 0;
    DataList.value.push({
      books: JSON.parse(JSON.stringify(res.data.items)),
    });
  }).catch(err => {
    ElMessage.error('获取浏览历史失败');
    if(err.response.status === 601){
      ElMessage.error('登录信息已过期');
      router.push('/account/login');
    }
  })
}
onMounted(() => {
  reloadData();
})
</script>

<template>
  <el-container class="shelf-page-container">
    <el-main style="z-index: 9" id="shelf-main-field">
      <el-scrollbar style="height: 80vh; min-height: 520px;">
        <div class="items-container">
          <div v-for="(item,index) in DataList.slice(1)" :key="index" v-if="nowBookList === 0" class="data-item" >
            <div class="item-inner" @click="jumpToBookList(index)" style="background: #eeec70">
              {{item.name}}
            </div>
          </div>
          <div v-for="(item,index) in DataList[nowBookList].books" :key="index" class="data-item">
            <div class="item-inner" @click="jumpToDetail(item.bookId)">
              <el-image :src="item.cover" fit="contain">
                <template #error>
                  <div class="error-image">
                    <el-icon size="45px" color="#758a99">
                      <Picture/>
                    </el-icon>
                  </div>
                </template>
              </el-image>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </el-main>
    <el-aside style="z-index: 10">
      <div class="full-fix center-layout-row">
        <LittleBalls :menu-items="menuItems" :disperse="1" @item-clicked="handleLittleBallsClicked"/>
      </div>
    </el-aside>
    <el-dialog v-model="dialogVisible" title="创建书单" width="500">
      <el-form ref="formRef" :rules="formRules" :model="formData">
        <el-form-item label="名称" prop="name">
          <el-input placeholder="请输入书单名称"></el-input>
        </el-form-item>
        <el-button color="#3621ef" @click="submitForm(formRef)">创建</el-button>
      </el-form>
    </el-dialog>
  </el-container>
</template>

<style scoped>
.shelf-page-container {
  background: #fcfce5;
  min-width: 720px;
}
.el-aside {
  width: 5vw;
  min-width: 50px;
}
.items-container{
  display: flex;
  flex-wrap: wrap;
  width: calc(6*max(12vw,90px) + 7*20px);
}
.data-item {
  display: inline-flex;
  width: 12vw;
  height: 36vh;
  min-height: 120px;
  min-width: 90px;
  padding: 15px 10px 15px 10px;
}
.item-inner{
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.2s ease-in-out;
  border: 1px solid #edd1d8;
  box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}
.item-inner:hover {
  transform: scale(1.1, 1.1);
  transition: all 0.2s ease-in-out;
  cursor: pointer;
}
.el-image{
  max-width: 100%;
  max-height: 100%;
}
</style>