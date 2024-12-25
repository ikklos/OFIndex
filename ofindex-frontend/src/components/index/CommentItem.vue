<script setup>

import {ref, watch} from "vue";
import {Icon} from "view-ui-plus";

const props = defineProps({
  type: {
    type: String,
  },
  id: {
    type: Number,
  },
  liked:{
    type: Boolean,
  },
  likes:{
    type: Number,
  },
  avatar:{
    type: String,
    default: '',
  },
  content: {
    type: String,
    default: '',
  },
  children_list: {
    type: Array,
    default: null,
  },
});
const emit = defineEmits(['addComment','flagLike']);
const commentText = ref('');
const handleClick = ()=>{
  emit('addComment',commentText.value,props.id);
}
const handleAddComment = (text,id)=>{
  emit('addComment',text,id);
}
const flagLike = (id)=>{
  emit('flagLike',id, !props.liked);
}
const handleFlagLike = (id, option)=>{
  emit('flagLike',id,option);
}
</script>

<template>
  <div class="comment-out">
    <el-row>
      <el-col class="user-avatar" :span="4">
        <el-avatar :src="avatar" :size="50"></el-avatar>
      </el-col>
      <el-col class="content" :span="20">
        <el-row>
          {{content}}
        </el-row>
        <el-row>
          <Icon type="ios-heart-outline" class="like-icon" @click="flagLike(props.id)" v-if="!props.liked"/>
          <Icon type="ios-heart" class="like-icon" @click="flagLike(props.id)" v-if="props.liked"/>
          {{props.likes}}
        </el-row>
        <el-row class="collapse-row">
          <el-collapse class="comment-collapse">
            <el-collapse-item v-if="props.type !== 'commentReply'"  title="展开回复" class="comment-collapse-item">
              <el-input type="textarea" v-model="commentText" :autosize="{minRows:3,maxRows:5}" class="text-input"></el-input>
              <el-row class="button-row">
                <el-button color="#3621ef" @click="handleClick">回复</el-button>
              </el-row>
              <CommentItem v-for="(child,index) in children_list" :key="index"
                          :avatar="child.userAvatar"
                          :children_list="child.comments"
                          :content="child.text"
                          :id="child.commentId" :type="'commentReply'"
                           :liked = child.liked
                           :likes = child.likes
                           @flag-like = 'handleFlagLike'
              @add-comment="handleAddComment"></CommentItem>
            </el-collapse-item>
          </el-collapse>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
  .user-avatar {
    box-sizing: border-box;
    padding: 0 20px 0 0;
    text-align: right;
  }
  .comment-collapse{
    width: 100%;
    border:none;
  }
  .comment-collapse-item :deep(.el-collapse-item__header){
    border-top:none;
    background: #f2f5c6;
    border-bottom-color:#d7b83c;
    color: #3c5cd7;
  }
  .comment-collapse-item :deep(.el-collapse-item__wrap){
    border: none;
    background: #f2f5c6;
  }
  .text-input{
    box-sizing: border-box;
    padding: 10px 0 10px 0;
  }
  .text-input:deep(.el-textarea__inner){
    background: #fcfce5;
  }
  .button-row{
    width: 100%;
    justify-content: flex-end;
  }
  .like-icon{
    font-size:20px;
  }
  .like-icon:hover{
    cursor: pointer;
  }
</style>