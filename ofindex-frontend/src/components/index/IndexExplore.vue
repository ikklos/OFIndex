<script setup>
import {ref, reactive, onMounted} from "vue";
import {useRouter} from "vue-router";
import BookItem from "@/components/index/BookItem.vue";
import axiosApp from "@/main.js";
import {ElMessageBox, ElMessage} from 'element-plus'

let router = useRouter();
const ClassList = ref([]);
const filter = reactive({
  text: '',
  bookClass: 0,
  count: 20,
  page: 0,
})
const BookItems = ref([]);
//总共的查询结果数量
const TotalCount = ref(0);
//是否禁用加载
const noMore = ref(false);
//发送查询请求
let firstReq = function () {
  //清空数组
  TotalCount.value = filter.page = 0;
  BookItems.value.splice(0, BookItems.value.length);
  noMore.value = false;
  //发送请求部分
  ReqForMore();
}
let ReqForMore = function () {
  axiosApp.post('/search', filter).then(response => {
    if (response.status === 200) {
      for (let i = 0; i < response.data.count; i++) {
        BookItems.value.push(response.data.items[i]);
      }
      filter.page += 1;
      if (response.data.totalResult <= filter.count * filter.page) {
        noMore.value = true;
      }
    } else if (response.status === 601) {
      throw new Error('tokenFailed');
    }
  }).catch(error => {
    ElMessage.error('哎怎么似了');
    if (error.message === 'tokenFailed') {
      ElMessageBox.alert('哥们怎么不登录', '不是哥们').then(() => {
        router.push('/account/login');
      }).catch(() => {
        router.push('/account/login');
      })
    }
  })
}
let handleJumpToDetail = function (id) {
  router.push('/index/detail/book-detail/' + id);
}
let handleSelect = function (id) {
  filter.bookClass = id;
  firstReq();
}
let getClassList = function () {
  axiosApp.get('/class').then(response => {
    if (response.status === 200) {
      if (response.data.message === 'BookClass Found') {
        for (let i = 0; i < response.data.count; i++) {
          ClassList.value.push(response.data.items[i]);
        }
      }
    } else {
      if (response.status === 601) {
        throw new Error('tokenFailed');
      }
    }
  }).catch(error => {
    ElMessage.error('哎怎么似了');
    if (error.message === 'tokenFailed') {
      ElMessageBox.alert('哥们怎么不登录', '不是哥们').then(() => {
        router.push('/account/login');
      }).catch(() => {
        router.push('/account/login');
      })
    }
  })
}
onMounted(() => {
  getClassList()
  firstReq();
})
</script>

<template>
  <el-container>
    <el-aside class="explore-aside">
      <el-scrollbar style="height:calc(100vh - max(8vh,60px))">
        <el-menu default-active="0">
          <el-menu-item v-if="ClassList.length === 0" :disabled="true" key="no-class">No List</el-menu-item>
          <el-menu-item v-for="(item,index) in ClassList" :key="index" :index="item.id.toString()"
                        @click="handleSelect(item.id)">
            {{ item.name }}
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-main class="center-layout-column">
      <div class="div-main">
        <el-row style="padding: 20px 0 20px 0; box-sizing: border-box; height: 10vh">
          <el-col :span="16" :offset="4">
            <el-input v-model="filter.text" prefix-icon="Search" @keyup.enter="firstReq">
            </el-input>
          </el-col>
        </el-row>
        <div class="infinite-list">
          <ul v-infinite-scroll="ReqForMore"
              style="overflow-x:hidden;" :infinite-scroll-distance="20"
              :infinite-scroll-disabled="noMore">
            <li v-for="(item,index) in BookItems" :key="index">
              <BookItem :cover-url="item.cover"
                        :book-author="item.author"
                        :book-description="item.description"
                        :book-name="item.name"
                        :id="item.id"
                        @jump-to-detail="handleJumpToDetail"
              ></BookItem>
            </li>
          </ul>
          <p v-if="noMore">No more</p>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.el-aside {
  width: 10vw;
}

.explore-aside {
  background: #fcfce5;
}

.el-menu-item {
  width: 100%;
  color: #3621ef !important;

  i {
    color: #fff !important;
  }

  font-size: large;
  justify-content: center;
}

.el-menu-item:hover {
  outline: 0 !important;
  color: #3621ef !important;
  background: #b198fa;
}

.el-menu-item.is-active {
  color: #fff !important;
  background: #3621ef !important;
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
  background-color: #fcfce5;
  border-radius: 25px;
}

:deep() .el-input__inner {
  color: #000000 !important;
}

:deep() .el-input__prefix {
  color: #000000;
}

.infinite-list {
  height: calc(100vh - max(8vh, 60px) - 10vh);
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.infinite-list::-webkit-scrollbar {
  width: 10px;
  height: 10px;
  /**/
}

.infinite-list::-webkit-scrollbar-track {
  background: #FFFFFF;
  border-radius: 2px;
}

.infinite-list::-webkit-scrollbar-thumb {
  background: #8e6df9;
  border-radius: 10px;
}

.infinite-list::-webkit-scrollbar-thumb:hover {
  background: #0012e2;
  cursor: pointer;
}

.infinite-list::-webkit-scrollbar-corner {
  background: #179a16;
}
</style>