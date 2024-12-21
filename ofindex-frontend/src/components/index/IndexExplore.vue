<script setup>
import {ref, reactive, onMounted} from "vue";
import {useRouter} from "vue-router";
import BookItem from "@/components/index/BookItem.vue";
let router = useRouter();
let BatchSize = 20;
const ClassList = reactive([
  {
    name: "全部",
    classId: 0,
  }
]);
for (let i = 1; i <= 100; i++) {
  ClassList.push({name: "小说", classId: i});
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
  let result = {
    totalResult: 1000,
    data: [],
  };
  for (let i = 0; i < 20; i++) {
    result.data.push({
      bookId: i,
      name: "支柱霞：血本无归",
      author: "last炫、",
      description: "S6第一个王者的含金量  我！是！梓！神！ wtm顶死你~ S6第一个王者的含金量  我！是！梓！神！ wtm顶死你~ S6第一个王者的含金量  我！是！梓！神！ wtm顶死你~",
      cover: "https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg",
      tag: ["搞笑", "炫狗", "皮套"],
    });
  }
  //更新结果数量
  TotalCount.value = result.totalResult;
  LoadedPages.value = 1;
  for (let i = 0; i < BatchSize; i++) {
    BookItems.value.push(
        result.data[i]
    );
  }
}
let ReqForMore = function (Page) {
  console.log("ok")
  if (LoadedPages.value * BatchSize < TotalCount.value) {

  }
}
let handleJumpToDetail = function (id){
  router.push('/index/detail/book-detail/' + id);
}
onMounted(() => {
  FilterByString("bookId");
})
</script>

<template>
  <el-container>
    <el-aside>
      <el-scrollbar style="height:calc(100vh - max(8vh,60px))">
        <el-menu default-active="0">
          <el-menu-item v-for="{name,classId} in ClassList" :key="classId" :index="String(classId)">
            {{ name }}
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-main class="center-layout-column">
      <div class="div-main">
        <el-row style="padding: 20px 0 20px 0; box-sizing: border-box; height: 10vh">
          <el-col :span="16" :offset="4">
            <el-input v-model="SearchString" prefix-icon="Search" @keyup.enter="FilterByString">
            </el-input>
          </el-col>
        </el-row>
        <el-scrollbar style="height:calc(100vh - max(8vh,60px) - 10vh)">
          <ul v-infinite-scroll="ReqForMore" class="infinite-list"
              style="overflow-x:hidden;">
            <li v-for="{bookId,name,author,description,cover,tag} in BookItems" :key="bookId">
              <BookItem :cover-url="cover"
                        :book-author="author"
                        :book-description="description"
                        :book-name="name"
                        :id="bookId"
                        @jump-to-detail="handleJumpToDetail"
              ></BookItem>
            </li>
          </ul>
        </el-scrollbar>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.el-aside {
  width: 10vw;
}

.el-menu-item {
  width: 100%;
  color: #409EFF !important;

  i {
    color: #fff !important;
  }

  font-size: large;
  justify-content: center;
}

.el-menu-item:hover {
  outline: 0 !important;
  color: #409EFF !important;
}

.el-menu-item.is-active {
  color: #fff !important;
  background: #409EFF !important;
}

.el-submenu :deep(.el-submenu__title) {
  color: #fff !important;
}

.el-submenu :deep(.el-submenu__title) i {
  color: #fff !important;
}

.el-submenu :deep(.el-menu-item) {
  color: #fff !important;
}

.el-main {
  padding: 0 10vw 0 10vw;
}

.div-main {
  background: #FFFFFF;
}

.el-input {
  height: 40px;
}

:deep() .el-input__wrapper {
  background-color: rgba(128, 170, 255, 0.2);
  border-radius: 25px;
}

:deep() .el-input__inner {
  color: #000000 !important;
}

:deep() .el-input__prefix {
  color: #000000;
}

.infinite-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}
</style>