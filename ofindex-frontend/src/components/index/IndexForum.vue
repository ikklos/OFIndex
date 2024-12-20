<script setup>
 import {ref, onMounted} from 'vue'
 const additionReqSize = 40;
 //帖子列表
 const CommunityPostList = ref([]);
 const ReqForMore = function (){
   //计算页数
    let page = Math.floor(CommunityPostList.value.length / additionReqSize);
    //发送请求
   for(let i = 0; i < additionReqSize; i++){
     CommunityPostList.value.push({
       posterId: 2,
       avatar: "https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg",
       postId: page*40 + i,
       title:'脑袋为什么尖尖的',
       text:'那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你',
       pictures:[
         'https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg',
         'https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg',
       ]
     });
   }
   console.log(CommunityPostList.value.length);
 }

</script>

<template>
  <el-container>
    <el-main class="forum-main">
      <el-scrollbar>
        <ul v-infinite-scroll="ReqForMore" class="post-list">
          <li v-for="(item,index) in CommunityPostList" :key="index" class="post-item">
            <div class="item-inner">
              <el-row class="title-row">
                <div class="avatar-area">
                  <el-avatar size="large"></el-avatar>
                </div>
                {{item.title}}
              </el-row>
              <el-row class="text-row">
                {{item.text}}
              </el-row>
              <el-row class="pictures-row" v-if="item.pictures.length > 0">
                <div class="small-picture-container" v-for="(picture,index) in item.pictures" :key="index">
                  <el-image  :src="picture" class="post-picture" fit="contain">
                  </el-image>
                </div>
              </el-row>
            </div>
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
.el-scrollbar{
  height: calc(100vh - max(8vh,60px));
}
.post-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}
.post-item{
  display: flex;
  width: 100%;
  padding: 10px 5px 10px 5px;
  box-sizing: border-box;
  align-items: center;
  justify-content: center;
}
.item-inner{
  width: 50%;
  min-width: 600px;
  border: 1px solid #292929;
  border-radius: 24px;
  box-shadow: 1px 2px 2px rgba(0, 0, 0, 0.2);
}
.title-row{
  font-size: larger;
  box-sizing: border-box;
  padding: 20px 20px 20px 20px;
  text-align: center;
  align-items: center;      /*垂直方向居中*/
}
.avatar-area{
  box-sizing: border-box;
  padding: 0 20px 0 0;
}
.text-row{
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  box-sizing: border-box;
  padding: 0 20px 0 20px;
  text-align: left;
  text-overflow: ellipsis;
  line-height: calc(max(3vh,30px));
  height: calc(max(3vh,30px)*3);
  overflow: hidden;
}
.text-row:hover{
  cursor: pointer;
}
.pictures-row{
  box-sizing: border-box;
  padding: 5px 10px 5px 10px;
}
.small-picture-container{
  display: inline-block;
  padding: 5px 10px 5px 10px;
  box-sizing: border-box;
  width: 150px;
  height: 200px;
}
.post-picture{
  max-width: 100%;
  max-height: 100%;
}
.post-picture:hover{
  cursor: zoom-in;
}
</style>