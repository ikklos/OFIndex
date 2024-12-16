<script setup>
import {ref,reactive,onMounted} from "vue";
import BookItem from "@/components/index/BookItem.vue";
let BatchSize = 20;
const ClassList = reactive([
  {
    name: "全部",
    classId: 0,
  }
]);
for(let i = 1; i <= 100; i++){
  ClassList.push({name:"小说",classId:i});
}
const NowClass = ref(0);
const SearchString = ref("");
const BookItems = ref([]);
//总共的查询结果数量
const TotalCount = ref(0);
//目前已加载的页数
const LoadedPages = ref(0);
//发送带查询字符串的查询请求
let FilterByString = function (Page) {
    //清空数组
    TotalCount.value = LoadedPages.value = 0;
    BookItems.value.splice(0, BookItems.value.length);
    //获取查询结果
    let result={
      totalResult:1000,
      data:[],
    };
    for(let i = 0; i < 20; i++){
      result.data.push({
        bookId: i,
        name: "支柱霞：血本无归",
        author: "last炫、",
        description: "S6第一个王者的含金量  我！是！梓！神！ wtm顶死你~ S6第一个王者的含金量  我！是！梓！神！ wtm顶死你~ S6第一个王者的含金量  我！是！梓！神！ wtm顶死你~",
        cover: "https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg",
        tag:["搞笑","炫狗","皮套"],
      });
    }
    //更新结果数量
    TotalCount.value = result.totalResult;
    LoadedPages.value = 1;
    for(let i = 0; i < BatchSize; i++) {
      BookItems.value.push(
          result.data[i]
      );
    }
}
let ReqForMore = function (Page) {
  if(LoadedPages.value * BatchSize < TotalCount.value){
    
  }
}
onMounted(()=>{
  FilterByString("bookId");
})
</script>

<template>
  <el-container>
    <el-aside>
      <el-scrollbar>
        <el-menu default-active="0">
          <el-menu-item v-for="{name,classId} in ClassList" :key="classId" :index="String(classId)">
            {{name}}
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-main style="background: #292929" class="center-layout-column">
      <div class="div-main">
        <el-row>
          <el-col :span="16" :offset="4">
            <el-input v-model="SearchString" prefix-icon="Search" @keyup.enter="FilterByString">
            </el-input>
          </el-col>
        </el-row>
        <ul v-infinite-scroll="ReqForMore" class="infinite-list" style="overflow: auto; overflow-x:hidden;">
          <li v-for="{bookId,name,author,description,cover,tag} in BookItems" :key="bookId" class="infinite-list-item">
            <BookItem :cover-url="cover"
                      :book-author="author"
                      :book-description="description"
                      :book-name="name"
                      :id="bookId"
            ></BookItem>
          </li>
        </ul>

      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.el-container{
  height: 92vh;
}
.el-aside{
  width: 12vw;
  height: 100%;
}
:deep(.el-scrollbar__wrap) {
  overflow: scroll !important; /* 确保滚动条逻辑正常 */
}

.el-menu-item {
  width: 100%;
  color: #409EFF !important;
  i{
    color: #fff!important;
  }
  font-size: large;
  justify-content: center
}

.el-menu-item:hover {
  outline: 0 !important;
  color: #409EFF !important;
}

.el-menu-item.is-active {
  color: #fff !important;
  background: #409EFF !important;
}

.el-submenu :deep(.el-submenu__title)  {
  color: #fff !important;
}

.el-submenu :deep(.el-submenu__title)  i {
  color: #fff !important;
}

.el-submenu :deep(.el-menu-item)  {
  color: #fff !important;
}
.el-main{
  padding: 0 10vw 0 10vw;
}
.div-main{
  width: 100%;
  height: 100%;
  background: #FFFFFF;
}
.el-input{
  height: 40px;
}
:deep() .el-input__wrapper {
  background-color: #80aaff;
}
:deep() .el-input__inner {
  color: #FFFFFF !important;
}
:deep() .el-input__prefix {
  color: #FFFFFF;
}
.infinite-list{
  height: 84vh;
  padding: 0;
  margin: 0;
  list-style: none;
}
.infinite-list .infinite-list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 10px;
}
.infinite-list .infinite-list-item + .list-item {
  margin-top: 10px;
}
</style>