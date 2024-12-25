<script setup>
import {ref, onMounted} from 'vue'
import {useRouter} from "vue-router";
import PostItem from "@/components/index/PostItem.vue";
import PostEditPage from "@/components/index/PostEditPage.vue";
import axiosApp from "@/main.js";
import {ElMessage} from "element-plus";

let router = useRouter();
const additionReqSize = 10;
//帖子列表
const CommunityPostList = ref([]);

const ReqForMore = function () {
  let page = Math.floor(CommunityPostList.value.length / additionReqSize);
  //发送请求
  axiosApp.get('/forum/posts', {
    params: {
      page: page,
      pagesize: additionReqSize,
    }
  }).then(response => {
    if (response.status === 200) {
      if (page * additionReqSize < CommunityPostList.value.length) {
        CommunityPostList.value.splice(page * additionReqSize, CommunityPostList.value.length - page * additionReqSize);
      }
      for (let i = 0; i < response.data.count; i++) {
        CommunityPostList.value.push({
          posterId: response.data.posts[i].posterId,
          avatar: response.data.posts[i].posterAvatar,
          postId: response.data.posts[i].postId,
          title: response.data.posts[i].title,
          text: response.data.posts[i].text,
          pictures: response.data.posts[i].images,
          liked: response.data.posts[i].liked,
          likes: response.data.posts[i].likes,
        });
      }
    }
  }).catch(error => {
    ElMessage.error('获取帖子列表失败');
    if (error.response.status === 601) {
      ElMessage.error('登录信息已过期');
      router.push('/account/login');
    }
  })
}
const refreshList = ()=>{
  CommunityPostList.value.splice(0,CommunityPostList.value.length);
  ReqForMore();
}
const jumpToDetail = function (id) {
  router.push('/index/detail/post-detail/' + id);
}
const handleFlagLike = function (id,option,index) {
  if(option){
    axiosApp.get('/forum/post/like/'+id).then(response => {
      CommunityPostList.value[index].liked = true;
      CommunityPostList.value[index].likes += 1;
      ElMessage.success('点赞成功，感谢支持');
    }).catch(error => {
      ElMessage.error('操作失败');
      if(error.response.status === 601){
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  }else{
    axiosApp.delete('/forum/post/like/'+id).then(response => {
      ElMessage.success('已取消点赞');
      CommunityPostList.value[index].liked = false;
      CommunityPostList.value[index].likes -= 1;
    }).catch(error => {
      ElMessage.error('操作失败');
      if(error.response.status === 601){
        ElMessage.error('登录信息已过期');
        router.push('/account/login');
      }
    })
  }
}
</script>

<template>
  <el-container>
    <el-main class="forum-main">
      <div class="post-area">
        <div class="post-edit-out">
          <post-edit-page @refresh="refreshList"></post-edit-page>
        </div>
        <ul v-infinite-scroll="ReqForMore" class="post-list" :infinite-scroll-distance="20">
          <li v-for="(item,index) in CommunityPostList" :key="index" class="post-item">
            <post-item :post-data="item" @jump-to-detail="jumpToDetail" @flag-like="(id,option)=>{handleFlagLike(id,option,index);}"></post-item>
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

.post-edit-out {
  display: flex;
  width: 100%;
  padding: 10px 5px 10px 5px;
  box-sizing: border-box;
  align-items: center;
  justify-content: center;
}

.post-area {
  height: calc(100vh - max(8vh, 60px));
}

.post-edit-out {
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

.forum-main::-webkit-scrollbar {
  width: 10px;
  height: 10px;
  /**/
}

.forum-main::-webkit-scrollbar-track {
  background: #FFFFFF;
  border-radius: 2px;
}

.forum-main::-webkit-scrollbar-thumb {
  background: #b198fa;
  border-radius: 10px;
}

.forum-main::-webkit-scrollbar-thumb:hover {
  background: #0012e2;
  cursor: pointer;
}

.forum-main::-webkit-scrollbar-corner {
  background: #179a16;
}

</style>