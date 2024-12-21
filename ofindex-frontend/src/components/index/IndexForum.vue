<script setup>
import {ref, onMounted} from 'vue'
import {useRouter} from "vue-router";
import PostItem from "@/components/index/PostItem.vue";
import PostEditPage from "@/components/index/PostEditPage.vue";

let router = useRouter();
const additionReqSize = 40;
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
  console.log(CommunityPostList.value.length);
}
const jumpToDetail = function (id) {
  router.push('/index/detail/post-detail/' + id);
}
</script>

<template>
  <el-container>
    <el-main class="forum-main">
      <el-scrollbar>
        <div class="post-edit-out"><post-edit-page></post-edit-page></div>
        <ul v-infinite-scroll="ReqForMore" class="post-list">
          <li v-for="(item,index) in CommunityPostList" :key="index" class="post-item">
            <post-item :post-data="item" @jump-to-detail="jumpToDetail"></post-item>
          </li>
        </ul>
      </el-scrollbar>
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

.el-scrollbar {
  height: calc(100vh - max(8vh, 60px));
}
.post-edit-out{
  display: flex;
  width: 100%;
  padding: 10px 5px 10px 5px;
  box-sizing: border-box;
  align-items: center;
  justify-content: center;
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
</style>