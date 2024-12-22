<script setup>
import {ref, onMounted} from 'vue'
import {useRouter} from "vue-router";
import PostItem from "@/components/index/PostItem.vue";
import PostEditPage from "@/components/index/PostEditPage.vue";

let router = useRouter();
const additionReqSize = 10;
//帖子列表
const CommunityPostList = ref([]);
const ReqForMore = function () {
  //计算页数
  let page = Math.floor(CommunityPostList.value.length / additionReqSize);
  //发送请求
  for (let i = 0; i < additionReqSize; i++) {
    CommunityPostList.value.push({
      posterId: 2,
      avatar: "https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg",
      postId: page * 40 + i,
      title: '脑袋为什么尖尖的',
      text: '那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你',
      pictures: [
        'https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg',
        'https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg',
      ]
    });
  }
}
const jumpToDetail = function (id) {
  router.push('/index/detail/post-detail/' + id);
}
</script>

<template>
  <el-container>
    <el-main class="forum-main">
      <div class="post-area">
        <div class="post-edit-out">
          <post-edit-page></post-edit-page>
        </div>
        <ul v-infinite-scroll="ReqForMore" class="post-list" :infinite-scroll-distance="20">
          <li v-for="(item,index) in CommunityPostList" :key="index" class="post-item">
            <post-item :post-data="item" @jump-to-detail="jumpToDetail"></post-item>
          </li>
        </ul>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.forum-main {
  width: 100%;
  min-width: 720px;
  padding: 0 20px 0 20px;
  box-sizing: border-box;
}

.post-edit-out{
  display: flex;
  width: 100%;
  padding: 10px 5px 10px 5px;
  box-sizing: border-box;
  align-items: center;
  justify-content: center;
}
.post-area{
  height: calc(100vh - max(8vh, 60px));
}
.post-edit-out{
  display: flex;
}
.post-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.post-item {
  display: flex;
  width: 100%;
  padding: 10px 5px 10px 5px;
  box-sizing: border-box;
  align-items: center;
  justify-content: center;
}
.forum-main::-webkit-scrollbar{
  width:10px;
  height:10px;
  /**/
}
.forum-main::-webkit-scrollbar-track{
  background: #FFFFFF;
  border-radius:2px;
}
.forum-main::-webkit-scrollbar-thumb{
  background: #b198fa;
  border-radius:10px;
}
.forum-main::-webkit-scrollbar-thumb:hover{
  background: #0012e2;
  cursor: pointer;
}
.forum-main::-webkit-scrollbar-corner{
  background: #179a16;
}

</style>