<script setup>

import {ref} from "vue";

const isHovered = ref(false);
const props = defineProps({
  postData: {},
  isDetail: {
    type: Boolean,
    default: false,
  }
});
const emit = defineEmits([
    'jumpToDetail'
]);
const jumpDetail = function(id){
  emit('jumpToDetail',id);
}
</script>

<template>
  <div class="item-inner" :class="{'half-width':!isDetail}">
    <el-row class="title-row">
      <div class="avatar-area">
        <el-avatar size="large" :src="postData.avatar" fit="cover"></el-avatar>
      </div>
      {{postData.title}}
    </el-row>
    <el-row class="text-row" :class="{'hovered':((!isDetail)&&isHovered)}"
            @click="jumpDetail(postData.postId)"
            @mouseover="isHovered = true"
            @mouseleave="isHovered = false">
      {{postData.text}}
    </el-row>
    <el-row class="pictures-row" v-if="postData.pictures.length > 0">
      <div class="small-picture-container" v-for="(picture,index) in postData.pictures" :key="index">
        <el-image  :src="picture" class="post-picture" fit="contain">
        </el-image>
      </div>
    </el-row>
  </div>
</template>

<style scoped>
.item-inner.half-width{
  width: 50%;
}
.item-inner{
  min-width: 600px;
  border: 1px solid #3c5cd7;
  border-radius: 24px;
  background: #f9fce8;
  box-shadow: 1px 2px 2px rgba(0, 0, 0, 0.2);
}
.title-row{
  font-size: larger;
  box-sizing: border-box;
  padding: 20px 20px 20px 20px;
  text-align: center;
  align-items: center;      /*垂直方向居中*/
  color: #000000;
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
.text-row.hovered{
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