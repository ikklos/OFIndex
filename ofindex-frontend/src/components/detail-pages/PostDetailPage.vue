<script setup>
import{useRoute} from "vue-router";
import {reactive, onMounted, ref} from "vue";
import CommentItem from "@/components/index/CommentItem.vue";
import PostItem from "@/components/index/PostItem.vue";

let route = useRoute();
const editingComment = ref('');
const PageData = reactive({
  postId: route.params.postId,
  postData: {
    posterId: 2,
    avatar: "https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg",
    postId: 1,
    title:'脑袋为什么尖尖的',
    text:'那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你那我问你',
    pictures:[
      'https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg',
      'https://s2.loli.net/2024/12/15/hUJM5k97sNg8SIb.jpg',
    ]
  },
  comment: [
    {
      userId : 1,
      userAvatar: 'https://s2.loli.net/2024/12/20/3hNwzHfSvQJTbFM.jpg',
      commentId: 1,
      text: '嘻嘻',
      children_array:[
        {
          userId : 1,
          userAvatar: 'https://s2.loli.net/2024/12/20/3hNwzHfSvQJTbFM.jpg',
          commentId: 2,
          text: '哈哈',
        }
      ]
    },{
      userId : 1,
      userAvatar: 'https://s2.loli.net/2024/12/20/3hNwzHfSvQJTbFM.jpg',
      commentId: 3,
      text:'hh'
    }
  ]
})
onMounted(() => {
  //请求帖子详细信息
})
const submitComment = function (){

  //发送评论


}
</script>

<template>
  <el-container>
    <el-main class="post-detail-main">
      <div class="post-card">
        <el-scrollbar>
          <div class="post-inner">
            <el-row class="post-area" >
              <post-item :post-data="PageData.postData" :is-detail="true"></post-item>
            </el-row>
            <el-row class="edit-comment-row">
              <el-input v-model="editingComment" type="textarea"
                        placeholder="我要锐评@v@" class="comment-input"
                        :autosize="{minRows:4,maxRows:8}">
              </el-input>
            </el-row>
            <el-row class="button-row">
              <el-button color="#3621ef" @click="submitComment">发布评论</el-button>
            </el-row>
            <el-row class="comment-area">
              <div class="comment-area-inner">
                <el-collapse class="comment-collapse">
                  <el-collapse-item title="展开评论" class="comment-collapse-item">
                    <comment-item v-for="(comment,index) in PageData.comment" :key="index" :avatar="comment.userAvatar"
                                  :children_list="comment.children_array"
                                  :content="comment.text"></comment-item>
                  </el-collapse-item>
                </el-collapse>
              </div>
            </el-row>
          </div>
        </el-scrollbar>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.post-detail-main {
  height: calc(100vh - max(8vh,60px));
  align-items: center;
  text-align: center;
  justify-content: center;
  justify-items: center;
}
.post-card{
  width: 50%;
  height: 100%;
  min-height: 560px;
  min-width: 680px;
  box-sizing: border-box;
  padding: 20px;
  border-radius: 10px;
  border: 1px solid #c8cef4;
  box-shadow: 1px 1px 4px rgba(0,0,0,0.3);
  background: #f9fce8;
}
.post-inner{
  width: 100%;
}
.post-area{
  width: 100%;
}
.edit-comment-row{
  box-sizing: border-box;
  padding: 10px;
}
.comment-input:deep(.el-textarea__inner){
  background: #f2f5c6;
}
.button-row{
  box-sizing: border-box;
  padding: 10px;
  align-items: center;
  justify-items: flex-end;
  justify-content: flex-end;
}
.comment-area{
  box-sizing: border-box;
  padding: 20px 0 20px 0;
  width: 100%;
}
.comment-area-inner{
  width: 100%;
  background: #f2f5c6;
  border-radius: 10px;
  box-sizing: border-box;
  padding: 10px;
}
.comment-collapse-item{
  width: 100%;
}
.comment-collapse{
  border:none;
}
.comment-collapse-item :deep(.el-collapse-item__header){
  border-top:none;
  background: #f2f5c6;
  border-bottom-color:#d7b83c;
  color: #3c5cd7;
}
.comment-collapse-item :deep(.el-collapse-item__wrap){
  border:none;
  background: #f2f5c6;
}
</style>