<script setup>

import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import axiosApp from "@/main.js";
import VuePdfEmbed from 'vue-pdf-embed'
import {ElMessage} from "element-plus";
import {EditPen, Minus, Plus} from "@element-plus/icons-vue";

const route = useRoute();
const bookId = ref(route.params.bookId);
const packId = ref(route.params.packId);
const pdfWidth = ref(1241);
const pdfHeight = ref(1754);
const currentPage = ref(1);
const maxPage = ref(1);
const ebookUrl = ref('');
const handleChange = function (num) {

}
const handleLoaded = function ({numPages}){
  maxPage.value = numPages;
}
const fillWidth = function () {
  const main = document.getElementById("read-index-main");
  if(main){
    const rect = main.getBoundingClientRect();
    pdfHeight.value = null;
    pdfWidth.value = rect.width-30;
  }
}
const fillHeight = function () {
  const main = document.getElementById("read-index-main");
  if(main){
    const rect = main.getBoundingClientRect();
    pdfHeight.value = rect.height-20;
    pdfWidth.value = null;
  }
}
onMounted(()=>{
  axiosApp.get('/load/ebook/'+bookId.value,{
    responseType: 'blob',
  }).then((response)=>{
    let blobData = response.data;
    const url = URL.createObjectURL(blobData);
    ebookUrl.value = url;
    console.log(url);
  }).catch((err)=>{
    ElMessage.error('加载电子书失败');
  });
})
</script>

<template>
  <el-container class="reading-page">
    <el-header class="reading-page-header">
      <el-row style="height: 100%">
        <el-col :span="4" style="height: 100%">
          <div class="center-div">
            <el-input-number v-model="currentPage" :min="1" :max="maxPage" @change="handleChange" />
          </div>
          <div class="center-div" style="font-size: 18px;">共{{maxPage}}页</div>
        </el-col>
        <el-col :span="8" style="height: 100%">
          <div class="center-div">
            <el-button color="#3621ef" @click="fillWidth">横向填充</el-button>
          </div>
          <div class="center-div">
            <el-button color="#3621ef" @click="fillHeight">纵向填充</el-button>
          </div>
        </el-col>
        <el-col :span="12" style="height: 100%">
          <div style="height: 100%; display: flex; align-items: center; justify-content: flex-end;">
            <el-button color="#3621ef" style="border-radius: 50%; height: 40px; width: 40px; font-size: 20px">
              <el-icon>
                <EditPen/>
              </el-icon>
            </el-button>
          </div>
        </el-col>
      </el-row>
    </el-header>
    <el-main class="reading-page-main" id="read-index-main">
        <vue-pdf-embed :source="ebookUrl" :height="pdfHeight" :width="pdfWidth" :page="currentPage" @loaded="handleLoaded"/>
    </el-main>
  </el-container>
</template>

<style scoped>
.reading-page{
  height: 100vh;
  width: 100vw;
  min-height: 500px;
  min-width: 800px;
}
.reading-page-header{
  background: #edeb67;
  color: #1a1a1a;
  height: calc(max(8vh,60px))
}
.center-div{
  box-sizing: border-box;
  padding: 10px;
  height: 100%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  justify-items: center;
}
.reading-page-main{
  padding: 10px;
  align-items: center;
  justify-content: center;
  justify-items: center;
}
.reading-page-main::-webkit-scrollbar{
  width:10px;
  height:10px;
  /**/
}
.reading-page-main::-webkit-scrollbar-track{
  background: #FFFFFF;
  border-radius:2px;
}
.reading-page-main::-webkit-scrollbar-thumb{
  background: #3621ef;
  border-radius:10px;
}
.reading-page-main::-webkit-scrollbar-thumb:hover{
  background: #0012e2;
  cursor: pointer;
}
.reading-page-main::-webkit-scrollbar-corner{
  background: #0000dd;
}
</style>