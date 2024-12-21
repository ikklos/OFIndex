<script setup>

const props = defineProps({
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
})
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
        <el-row class="collapse-row">
          <el-collapse class="comment-collapse">
            <el-collapse-item title="展开回复" v-if="children_list && children_list.length" class="comment-collapse-item">
              <CommentItem v-for="(child,index) in children_list" :key="index"
                          :avatar="child.userAvatar"
                          :children_list="child.children_array"
                          :content="child.text"></CommentItem>
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
  }
  .comment-collapse-item :deep(.el-collapse-item__wrap){
    border: none;
    background: #f2f5c6;
  }
</style>