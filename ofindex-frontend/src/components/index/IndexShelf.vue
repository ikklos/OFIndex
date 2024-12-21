<script setup>

import LittleBalls from "@/components/index/LittleBalls.vue";
import {ref, onMounted, onBeforeUnmount} from 'vue';
import {useRouter} from 'vue-router'
import {Picture} from "@element-plus/icons-vue";

let router = useRouter();
const menuItems = ref([
  {name: "1"}, {name: "2"}, {name: "3"},
]);
//包括书单和书目
const DataList = ref([
  {
    name: "书单",
    id: 1,
    type: "list",
  }
]);
for(let i = 0; i < 80; i++){
  DataList.value.push({
    name: "支柱虾：血本无归",
    id: 1,
    cover: "https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg",
    type: "book",
  })
}
const jumpToDetail = function(bookId, type) {
  if(type==='book'){
    router.push('/index/detail/book-detail/'+bookId);
  }else{
    //请求书单下的列表，覆盖当前的DataList
  }
}
</script>

<template>
  <el-container class="shelf-page-container">
    <el-main style="z-index: 9" id="shelf-main-field">
      <el-scrollbar style="height: 80vh; min-height: 520px;">
        <div class="items-container">
          <div v-for="(item,index) in DataList" :key="index" class="data-item">
            <div class="item-inner" @click="jumpToDetail(item.id,item.type)">
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
        <LittleBalls :menu-items="menuItems"/>
      </div>
    </el-aside>
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