<script setup>
import {ref,defineProps} from 'vue'
const bookId = ref(0);
const props = defineProps({
  id: Number,
  coverUrl: String,
  bookName: String,
  bookAuthor: String,
  bookDescription: String,
});
bookId.value = props.id;
const emit = defineEmits(['jumpToDetail']);
const JumpToDetail = function(){
    emit('jumpToDetail',bookId.value);
}
</script>

<template>
  <div class="book-item-container">
    <div class="book-item" @click="JumpToDetail">
      <el-row style="height: 100%">
        <el-col :span="4">
          <div style="height: 100%; display: flex; align-items: flex-start;">
            <img :src="coverUrl" alt="封面图" style="height: 100%;width: 5vw;object-fit: contain;">
          </div>
        </el-col>
        <el-col :span="20">
          <!--标题，作者-->
          <el-row style="height: 50%; width: 100%">
            <el-col :span="8" class="single-line-text" style="font-size: 25px">
              {{bookName}}
            </el-col>
            <el-col :span="4" :offset="12" class="single-line-text">
              作者：{{bookAuthor}}
            </el-col>
          </el-row>
          <!--介绍-->
          <el-row style="height: 50%; width: 100%; box-sizing: border-box; padding: 0 10px 10px 0;">
            <div class="description-field">
              {{bookDescription}}
            </div>
          </el-row>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.book-item-container {
  height: 20vh;
  width: 100%;
  box-sizing: border-box;
  padding: 10px;
}
.book-item {
  height: 100%;
  width: 100%;
  transition: all 0.3s ease;
  background: #d1c2fb;
  color: #000000;
}
.book-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transform: scale(1.05,1.05);
  cursor: pointer;
  transition: all 0.3s ease;
}
.el-col{
  height: 100%;
}
.single-line-text{
  text-align: left;
  overflow:hidden;
  white-space:nowrap;
  text-overflow:ellipsis;
}
.description-field{
  line-height: 3vh;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;
  height: 100%;
  width: 100%;
  padding : 5px;
  box-sizing: border-box;
  background-color: #eee6fe;
  color: #292929;
}
</style>